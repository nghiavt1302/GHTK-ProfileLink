package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.constants.Provider;
import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.dto.UserRegister;
import com.example.ghtkprofilelink.model.entity.UserEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.model.response.Pagination;
import com.example.ghtkprofilelink.repository.UserRepository;
import com.example.ghtkprofilelink.security.CustomUserDetails;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    private MailServiceImpl mailService;

    @Override
    public ListData getAll(int page, int pageSize) {
        Page<UserDto> userEntities = userRepository.findByStatusEquals(StatusEnum.ACTIVE, PageRequest.of(page, pageSize))
                .map(userEntity -> mapper.map(userEntity, UserDto.class));

        return new ListData(true, "success", userEntities.getContent(),
                new Pagination(userEntities.getNumber(), userEntities.getSize(), userEntities.getTotalPages(), (int) userEntities.getTotalElements()));
    }

    @Override
    public Data getById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new Data(true, "success", mapper.map(userEntity, UserDto.class));
    }

    @Override
    public Data getByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        return new Data(true, "success", mapper.map(userEntity, UserDto.class));
    }

    @Override
    public Data add(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) throw new EntityExistsException();
        UserEntity user = new UserEntity().mapUserDto(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        //        * Mac dinh de Role la 0
        user.setRole(0);
        return new Data(true, "success", mapper.map(userRepository.save(user), UserDto.class));
    }

    @Override
    public Data update(UserDto userDto) {
        if (!userRepository.existsByUsername(userDto.getUsername())) throw new EntityNotFoundException();
        UserEntity userRepo = userRepository.findByUsername(userDto.getUsername()).get();
        userRepo.setPassword(passwordEncoder.encode(userRepo.getPassword()));

        return new Data(true, "success", mapper.map(userRepository.save(userRepo), UserDto.class));
    }

    @Override
    public Data deleteById(Long id) {
        if (!userRepository.existsById(id)) throw new EntityNotFoundException();
        UserEntity user = userRepository.getById(id);
        user.setStatus(StatusEnum.INACTIVE);
        return new Data(true, "success", mapper.map(userRepository.save(user), UserDto.class));
    }

    @Override
    public Data deleteByUsername(String username) {
        if (!userRepository.existsByUsername(username)) throw new EntityNotFoundException();
        UserEntity user = userRepository.findByUsername(username).get();
        user.setStatus(StatusEnum.INACTIVE);
        return new Data(true, "success", mapper.map(userRepository.save(user), UserDto.class));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(user);
    }

    // JWTAuthenticationFilter sẽ sử dụng hàm này
    @Transactional
    public UserDetails loadUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return new CustomUserDetails(user);
    }

    @Override
    public Data register(UserRegister userRegister, StringBuffer siteURL)
            throws UnsupportedEncodingException, MessagingException {
        Optional<UserEntity> optional = userRepository.findByMail(userRegister.getMail());
        if (optional.isPresent()) return new Data(false, "mail already exist", null);

        UserEntity user = new UserEntity().mapUserRegister(userRegister);
        user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        user.setEnabled(false);
        user.setRole(0);// * Mac dinh de Role la 0
        user.setVerificationCode(RandomString.make(64));
        userRepository.save(user);

        // Gui mail
        mailService.sendMail(user, siteURL.append(user.getVerificationCode()).toString(), "sendMail", "Xác thực tài khoản");
        return new Data(true, "send mail success", siteURL);
    }

    @Override
    public Data verify(String verificationCode) {
        Optional<UserEntity> optionalUser = userRepository.findByVerificationCode(verificationCode);
        if (!optionalUser.isPresent()) return new Data(false, "verification code not found", null);

        UserEntity user = optionalUser.get();
        user.setVerificationCode(null);
        user.setEnabled(true);
        userRepository.save(user);
        return new Data(true, "verify success", null);
    }

    @Override
    public Data updatePasswordToken(String mail, StringBuffer siteUrl) throws MessagingException {
        Optional<UserEntity> optionalUser = userRepository.findByMail(mail);
        if (!optionalUser.isPresent()) return new Data(false, "mail not found", null);

        UserEntity user = optionalUser.get();
        user.setUpdatePasswordToken(RandomString.make(64));
        userRepository.save(user);
//
        mailService.sendMail(user, siteUrl.append(user.getUpdatePasswordToken()).toString(), "updatePassword", "Đổi mật khẩu");
        return new Data(true, "update password success", siteUrl);
    }

    @Override
    public Data updatePassword(String code, String password) {
        Optional<UserEntity> optionalUser = userRepository.findByUpdatePasswordToken(code);
        if (!optionalUser.isPresent()) {
            return new Data(false, "password token not found", null);
        }
        UserEntity user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdatePasswordToken(null);
        userRepository.save(user);
        return new Data(true, "update password success", null);
    }

    // Them user vao database khi login bang Facebook
    public void processOAuthPostLogin(String username) {
        UserEntity existUser = userRepository.getUserByUsername(username);
        if (existUser == null) {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(username);
            newUser.setProvider(Provider.FACEBOOK);
            newUser.setStatus(StatusEnum.ACTIVE);
            userRepository.save(newUser);
        }

    }
}

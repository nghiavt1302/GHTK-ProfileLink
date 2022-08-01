package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.constants.ProviderEnum;
import com.example.ghtkprofilelink.constants.RoleEnum;
import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.dto.UserRegister;
import com.example.ghtkprofilelink.model.entity.UserEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.repository.UserRepository;
import com.example.ghtkprofilelink.security.CustomUserDetails;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

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

//    @Override
//    Chua dung
//    public ListData getAll(int page, int pageSize) {
//        Page<UserDto> userEntities = userRepository.findByStatusEquals(StatusEnum.ACTIVE, PageRequest.of(page, pageSize))
//                .map(userEntity -> mapper.map(userEntity, UserDto.class));
//
//        return new ListData(true, "success", userEntities.getContent(),
//                new Pagination(userEntities.getNumber(), userEntities.getSize(), userEntities.getTotalPages(), (int) userEntities.getTotalElements()));
//    }

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
        //        * Mac dinh de Role la USER
        user.setRole(RoleEnum.USER);
        user.setProvider(ProviderEnum.DEFAULT);
        user.setIsProfile(false);
        return new Data(true, "success", mapper.map(userRepository.save(user), UserDto.class));
    }

    @Override
    public Data update(UserDto userDto,Long id) {
        UserEntity userEntity = userRepository.findByUsername(userDto.getUsername()).orElseThrow(()->new EntityNotFoundException());
        userEntity.setRole(userDto.getRole());
        userEntity.setIsProfile(userDto.getIsProfile());

        return new Data(true, "success", mapper.map(userRepository.save(userEntity), UserDto.class));
    }

    @Override
    public Data deleteById(Long id) {
        if (!userRepository.existsById(id)) throw new EntityNotFoundException();
        UserEntity user = userRepository.getById(id);
//        user.setStatus(StatusEnum.INACTIVE);
        user.setEnabled(false);
        return new Data(true, "success", mapper.map(userRepository.save(user), UserDto.class));
    }

    @Override
    public Data deleteByUsername(String username) {
        if (!userRepository.existsByUsername(username)) throw new EntityNotFoundException();
        UserEntity user = userRepository.findByUsername(username).get();
//        user.setStatus(StatusEnum.INACTIVE);
        user.setEnabled(false);
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
    public Data register(UserRegister userRegister, StringBuffer siteUrl)
            throws UnsupportedEncodingException, MessagingException {
        Optional<UserEntity> optional = userRepository.findByUsername(userRegister.getUsername());
        if (optional.isPresent()) return new Data(false, "username already exists", null);

        optional = userRepository.findByMail(userRegister.getMail());
        if (optional.isPresent()) return new Data(false, "mail already exist", null);

        UserEntity user = new UserEntity().mapUserRegister(userRegister);
        user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        user.setEnabled(false);
        user.setIsProfile(false);
        user.setRole(RoleEnum.USER);// * Mac dinh de Role la USER
        user.setVerificationCode(RandomString.make(64));


        // Gui mail
        Map<String, Object> props = new HashMap<>();
        props.put("name", user.getUsername());
        props.put("url", siteUrl.append(user.getVerificationCode()).toString());

        mailService.sendMail(props, user.getMail(), "sendMail", "Xác thực tài khoản");
        return new Data(true, "send mail success", mapper.map(userRepository.save(user),UserDto.class));
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
        Map<String, Object> props = new HashMap<>();
        props.put("name", user.getUsername());
        props.put("url", siteUrl.append(user.getUpdatePasswordToken()).toString());

        mailService.sendMail(props, user.getMail(), "updatePassword", "Đổi mật khẩu");
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

    @Override
    public Data forgotPassword(String mail) throws MessagingException {
        Optional<UserEntity> optionalUser = userRepository.findByMail(mail);
        if (!optionalUser.isPresent()) return new Data(false, "mail not found", null);

        String pass = RandomString.make(10);

        UserEntity user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(pass));
        userRepository.save(user);
//
        Map<String, Object> props = new HashMap<>();
        props.put("name", user.getUsername());
        props.put("pass", pass);

        mailService.sendMail(props, user.getMail(), "forgotPassword", "Quên mật khẩu");
        return new Data(true, "forgot password success", pass);
    }

    // Convert FB username (Vu Trong Nghia -> vutrongnghia)
    public String convertUsername(String name){
        int index=name.indexOf("@");
        String username;
        if(index>0){
            username=name.substring(0,index);
        } else username=name;
        String temp = Normalizer.normalize(username, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ","");
    }

    // Them so tu 0, 1, 2, ... vao sau username neu bi trung
    public String duplicateUsernameHandle(String nameConverted){
        String addInt = nameConverted;
        int i = 0;
        do {
            UserEntity existUser = userRepository.getUserByUsername(addInt);
            if (existUser == null){
                return addInt;
            }else {
                addInt = nameConverted.concat(String.valueOf(i));
                i++;
            }
        }while (true);
    }

    // Them user vao database khi login bang Facebook
    @Override
    public UserEntity processOAuthPostLogin(UserEntity userEntity,ProviderEnum provider) {
        UserEntity existEmail = userRepository.getUserByEmail(userEntity.getMail());
        if (existEmail == null) {
            String nameConverted = convertUsername(userEntity.getUsername());
            UserEntity existUsername = userRepository.getUserByUsername(nameConverted);
            if (existUsername == null) {
                UserEntity newUser = new UserEntity();
                newUser.setUsername(nameConverted);
                newUser.setMail(userEntity.getMail());
                newUser.setProvider(ProviderEnum.FACEBOOK);
//                newUser.setStatus(StatusEnum.ACTIVE);
                newUser.setRole(RoleEnum.USER); // * Mac dinh de Role la USER
                newUser.setEnabled(true);
                newUser.setIsProfile(false);
                return userRepository.save(newUser);
            } else {
                String nameFix = duplicateUsernameHandle(nameConverted);
                UserEntity newUser = new UserEntity();
                newUser.setUsername(nameFix);
                newUser.setMail(userEntity.getMail());
                newUser.setProvider(provider);
//                newUser.setStatus(StatusEnum.ACTIVE);
                newUser.setRole(RoleEnum.USER); // * Mac dinh de Role la USER
                newUser.setEnabled(true);
                newUser.setIsProfile(false);
                return userRepository.save(newUser);
            }

        } else return existEmail;
    }
}

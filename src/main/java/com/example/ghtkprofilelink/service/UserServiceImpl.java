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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
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
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

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
    public Data register(UserRegister userRegister, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        UserEntity user = new UserEntity().mapUserRegister(userRegister);
        user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
        user.setEnabled(false);
//        * Mac dinh de Role la 0
        user.setRole(0);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);


        userRepository.save(user);

        // Gui mail

        String url = siteURL + "/verify?code=" + user.getVerificationCode();

        Map<String, Object> props = new HashMap<>();
        props.put("name", user.getUsername());
        props.put("url", url);

        Context context = new Context();
        context.setVariables(props);
        String html = templateEngine.process("sendMail", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        helper.setTo(user.getMail());
        helper.setSubject("Xác thực tài khoản Profile Link");
        helper.setText(html, true);

        mailSender.send(message);

        return new Data(true, "send mail success", url);
    }

    @Override
    public Data verify(String verificationCode) {
        Optional<UserEntity> optionalUser = userRepository.findByVerificationCode(verificationCode);
        if (!optionalUser.isPresent()) {
            return new Data(false, "verify fail", null);
        }

        UserEntity user = optionalUser.get();
        user.setVerificationCode(null);
        user.setEnabled(true);
        userRepository.save(user);
        return new Data(true, "verify success", null);
    }

    // Them user vao database khi login bang Facebook
    public void processOAuthPostLogin(String username){
        UserEntity existUser = userRepository.getUserByUsername(username);
        if (existUser == null){
            UserEntity newUser = new UserEntity();
            newUser.setUsername(username);
            newUser.setProvider(Provider.FACEBOOK);
            newUser.setStatus(StatusEnum.ACTIVE);
            newUser.setEnabled(true);
            userRepository.save(newUser);
        }

    }
}

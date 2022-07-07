package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.entity.UserEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.model.response.Pagination;
import com.example.ghtkprofilelink.repository.UserRepository;
import com.example.ghtkprofilelink.security.CustomUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public ListData getAll(int page, int pageSize) {
        Page<UserDto> userEntities = userRepository.findByStatusEquals(StatusEnum.ACTIVE, PageRequest.of(page, pageSize))
                .map(userEntity -> mapper.map(userEntity, UserDto.class));

        return new ListData(true, "success", userEntities.getContent(),
                new Pagination(userEntities.getNumber(), userEntities.getSize(), userEntities.getTotalPages(), (int) userEntities.getTotalElements()));
    }

    @Override
    public Data getById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", mapper.map(userEntity, UserDto.class));
    }

    @Override
    public Data getByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", mapper.map(userEntity, UserDto.class));
    }

    @Override
    public Data add(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) throw new EntityExistsException();
        UserEntity user = new UserEntity();
        user.mapUserDto(userDto).setPassword(passwordEncoder.encode(userDto.getPassword()));
        return new Data(true, "success", mapper.map(userRepository.save(user), UserDto.class));
    }

    @Override
    public Data update(UserDto userDto) {
        if (!userRepository.existsById(userDto.getId())) throw new EntityNotFoundException();
        UserEntity userRepo = userRepository.getById(userDto.getId());
        if (userRepository.existsByUsername(userDto.getUsername()) && !userRepo.getUsername().equals(userDto.getUsername()))
            throw new EntityExistsException();

        UserEntity user = mapper.map(userDto, UserEntity.class);
        user.setId(userRepo.getId());
        user.setStatus(userRepo.getStatus());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return new Data(true, "success", mapper.map(userRepository.save(user), UserDto.class));
    }

    @Override
    public Data delete(Long id) {
        if (!userRepository.existsById(id)) throw new EntityNotFoundException();
        UserEntity user = userRepository.getById(id);
        user.setStatus(StatusEnum.INACTIVE);
        return new Data(true, "success", mapper.map(userRepository.save(user), UserDto.class));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(user);
    }
}

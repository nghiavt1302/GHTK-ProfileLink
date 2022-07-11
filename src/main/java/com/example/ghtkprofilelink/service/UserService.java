package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;

public interface UserService {
    ListData getAll(int page, int pageSize);

    Data getById(Long id);

    Data getByUsername(String username);

    Data add(UserDto userDto);

    Data update(UserDto userDto);

    Data deleteById(Long id);

    Data deleteByUsername(String username);
}

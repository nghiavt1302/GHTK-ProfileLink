package com.example.ghtkprofilelink.service;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.ghtkprofilelink.model.dto.ChartsDto;
import com.example.ghtkprofilelink.model.entity.ChartsEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.repository.ChartsRepository;

public class ChartsServiceImpl implements ChartsService{

    @Autowired
    ChartsRepository chartsRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Data getById(Long id) {
        // TODO Auto-generated method stub
        ChartsEntity charts = chartsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", modelMapper.map(charts, ChartsDto.class));
    }

    @Override
    public Data add(ChartsDto chartsDto) {
        // TODO Auto-generated method stub
        ChartsEntity charts = modelMapper.map(chartsDto, ChartsEntity.class);
        return new Data(true, "success", modelMapper.map(chartsRepository.save(charts), ChartsDto.class));
    }

    @Override
    public Data update(ChartsDto chartsDto, Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Data delete(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListData getAll(int page, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }
    
}

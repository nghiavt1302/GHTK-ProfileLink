package com.example.ghtkprofilelink.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.example.ghtkprofilelink.constants.StatusEnum;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ghtkprofilelink.model.dto.ChartsDto;
import com.example.ghtkprofilelink.model.entity.ChartsEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.model.response.Pagination;
import com.example.ghtkprofilelink.repository.ChartsRepository;

@Service
public class ChartsServiceImpl implements ChartsService {

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
        if (!chartsRepository.existsById(chartsDto.getId()))
            throw new EntityNotFoundException();
        ChartsEntity chartsRepo = chartsRepository.getById(chartsDto.getId());
        ChartsEntity charts = modelMapper.map(chartsDto, ChartsEntity.class);
        charts.setId(chartsRepo.getId());
        return new Data(true, "success", modelMapper.map(chartsRepository.save(charts), ChartsDto.class));
    }

    @Override
    public Data delete(Long id) {
        // TODO Auto-generated method stub
        ChartsEntity charts = chartsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        charts.setStatus(StatusEnum.INACTIVE);
        chartsRepository.save(charts);
        return new Data(true, "success", modelMapper.map(charts, ChartsDto.class));
    }

    @Override
    public ListData getAll(int page, int pageSize) {
        // TODO Auto-generated method stub
        Page<ChartsEntity> chartsEntities = chartsRepository.findAll(PageRequest.of(page, pageSize));
        return new ListData(true, "success", chartsEntities.getContent(),
                new Pagination(chartsEntities.getNumber(), chartsEntities.getSize(), chartsEntities.getTotalPages(),
                        (int) chartsEntities.getTotalElements()));
    }

    @Override
    public ListData getTopProfileToMonth(int page, int pageSize) {
        // TODO Auto-generated method stub
        Page<ChartsEntity> chartsEntities = chartsRepository.getTopProfileToMonth(PageRequest.of(page, pageSize));
        return new ListData(true, "success", chartsEntities.getContent(),
                new Pagination(chartsEntities.getNumber(), chartsEntities.getSize(), chartsEntities.getTotalPages(),
                        (int) chartsEntities.getTotalElements()));
    }

    // @Override
    // public ListData getByProfileId(Pageable pageable,Integer profileId) {
    // // TODO Auto-generated method stub
    // Page<ChartsEntity> pageDesign = chartsRepository.findByProfileId(pageable,
    // profileId);
    // Pagination pagination = new Pagination(pageDesign.getNumber(),
    // pageDesign.getSize(), pageDesign.getTotalPages(), (int)
    // pageDesign.getTotalElements());
    // List<ChartsDto> listChartsDto = pageDesign.stream().map(d ->
    // modelMapper.map(d, ChartsDto.class)).collect(Collectors.toList());
    // return new ListData(true, "success", listChartsDto, pagination);
    // }

}

package com.example.ghtkprofilelink.service.impl;

import javax.persistence.EntityNotFoundException;

import com.example.ghtkprofilelink.constants.StatusEnum;
import com.example.ghtkprofilelink.service.StatisticService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ghtkprofilelink.model.dto.StatisticDto;
import com.example.ghtkprofilelink.model.entity.StatisticEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;
import com.example.ghtkprofilelink.model.response.Pagination;
import com.example.ghtkprofilelink.repository.StatisticRepository;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    private final ModelMapper modelMapper;

    public StatisticServiceImpl(StatisticRepository statisticRepository, ModelMapper modelMapper) {
        this.statisticRepository = statisticRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Data getById(Long id) {
        // TODO Auto-generated method stub
        StatisticEntity charts = statisticRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", modelMapper.map(charts, StatisticDto.class));
    }

    @Override
    public Data add(StatisticDto statisticDto) {
        // TODO Auto-generated method stub
        StatisticEntity charts = modelMapper.map(statisticDto, StatisticEntity.class);
        return new Data(true, "success", modelMapper.map(statisticRepository.save(charts), StatisticDto.class));
    }

    @Override
    public Data update(StatisticDto statisticDto, Long id) {
        // TODO Auto-generated method stub
        if (!statisticRepository.existsById(statisticDto.getId()))
            throw new EntityNotFoundException();
        StatisticEntity chartsRepo = statisticRepository.getById(statisticDto.getId());
        StatisticEntity charts = modelMapper.map(statisticDto, StatisticEntity.class);
        charts.setId(chartsRepo.getId());
        return new Data(true, "success", modelMapper.map(statisticRepository.save(charts), StatisticDto.class));
    }

    @Override
    public Data delete(Long id) {
        // TODO Auto-generated method stub
        StatisticEntity charts = statisticRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        charts.setStatus(StatusEnum.INACTIVE);
        statisticRepository.save(charts);
        return new Data(true, "success", modelMapper.map(charts, StatisticDto.class));
    }

    @Override
    public ListData getAll(int page, int pageSize) {
        // TODO Auto-generated method stub
        Page<StatisticEntity> chartsEntities = statisticRepository.findAll(PageRequest.of(page, pageSize));
        return new ListData(true, "success", chartsEntities.getContent(),
                new Pagination(chartsEntities.getNumber(), chartsEntities.getSize(), chartsEntities.getTotalPages(),
                        (int) chartsEntities.getTotalElements()));
    }

    @Override
    public ListData getTopProfileToMonth(int page, int pageSize, int month, int year) {
        // TODO Auto-generated method stub
        Page<StatisticDto> chartsEntities = statisticRepository.getTopProfileToMonth(PageRequest.of(page, pageSize), month, year);
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

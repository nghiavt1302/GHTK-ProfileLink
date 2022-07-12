package com.example.ghtkprofilelink.service;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ghtkprofilelink.model.dto.LinkDto;
import com.example.ghtkprofilelink.model.entity.LinkEntity;
import com.example.ghtkprofilelink.model.entity.ProfileEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.Pagination;
import com.example.ghtkprofilelink.repository.LinkRepository;


@Service
public class LinkServiceImpl implements LinkService{
    @Autowired
    LinkRepository linkRepository;

    @Autowired
    Cloudinary cloudinary;
    
    @Autowired
    ModelMapper mapper;

    LinkDto linkDto;
    
    @Override
    public Data getById(Long id) {
        LinkEntity link = linkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return new Data(true, "success", link);
    }

    @Override
    public Data add(LinkDto linkDTO, MultipartFile file) {
        LinkEntity link = mapper.map(linkDTO, LinkEntity.class);
        if (!file.isEmpty()) {
            try {
                Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                link.setPicture(x.get("url").toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return new Data(true, "success", mapper.map(linkRepository.save(link), LinkDto.class));
    }

    @Override
    public Data update(LinkDto linkDTO, MultipartFile file, Long id) {
        // TODO Auto-generated method stub
        LinkEntity link = linkRepository.findById(linkDTO.getId()).get().setValueFromDto(linkDTO);
        link.setId(id);
        if (!file.isEmpty()) {
            try {
                Map x = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                link.setPicture(x.get("url").toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return new Data(true, "success", mapper.map(linkRepository.save(link), LinkDto.class));
    }

    @Override
    public Data delete(Long id) {
        // TODO Auto-generated method stub
        LinkEntity link = linkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        linkRepository.deleteById(id);
        return new Data(true, "success", link);
    }

    @Override
    public ResponseEntity<Data> getAll() {
        List<LinkEntity> linkEntity = linkRepository.findAll();
        // TODO Auto-generated method stub
        if(linkEntity.isEmpty()==false){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Data(true, "successful delete", linkEntity));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Data(false, "not found", ""));
        }
    }

    

    
}

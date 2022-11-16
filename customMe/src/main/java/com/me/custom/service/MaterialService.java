package com.me.custom.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.me.custom.domain.Material;
import com.me.custom.mapper.MaterialMapper;
import com.me.custom.utils.ComUtils;

//@RequiredArgsConstructor
@Service
public class MaterialService {

	@Autowired
	MaterialMapper materialMapper;
	
    public List<Material> findAll() {
        return materialMapper.findAll();
    }
    
    public Material findByName(String name) {
    	return materialMapper.findByName(name);
	}
 
    public void save(Material material) {
    	materialMapper.save(material);
    }
 
    public void updateAmount(Material material) {
    	materialMapper.updateAmount(material);
    }
    
    public void updateStatus(Material material) {
    	materialMapper.updateStatus(material);
    }
 
    public void deleteByName(String name) {
    	materialMapper.deleteByName(name);
    }
	
}

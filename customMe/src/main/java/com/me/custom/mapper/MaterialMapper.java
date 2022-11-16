package com.me.custom.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.me.custom.domain.Material;
 
@Repository
public interface MaterialMapper {
 
    List<Material> findAll();
 
    Material findByName(String name);
 
    void save(Material material);
 
    void updateAmount(Material material);
    
    void updateStatus(Material material);
    
    void updateSupplyInfo(Material material);
 
    void deleteByName(String name);

	int findByStatus(String string);
	
	LocalDateTime findMaxSupplyDateByStatus(String string);
 
	Material findBySupplyDate();
}
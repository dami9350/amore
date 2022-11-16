package com.me.custom.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me.custom.domain.Material;
import com.me.custom.service.MaterialService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(value = "/material")
public class MaterialController {

    private final MaterialService materialService;

    @GetMapping(value = "")
    public ResponseEntity<List<Material>> findAll() {
        List<Material> material = materialService.findAll();
        return ResponseEntity.ok(material);
    }
    
    @GetMapping(value = "/{name}")
    public ResponseEntity<Material> findByName(@PathVariable(value = "name") String name) {
        Material material = materialService.findByName(name);
        return ResponseEntity.ok(material);
    }
 
    @PostMapping(value = "")
    public ResponseEntity<?> save(@RequestBody Material material) {
    	materialService.save(material);
        
        HashMap<String, String> saveResult = new HashMap<>();
        saveResult.put("msg", "등록되었습니다.");
        return new ResponseEntity(saveResult, HttpStatus.OK);
    }
 
    @DeleteMapping(value = "/{name}")
    public ResponseEntity<?> delete(@PathVariable(value = "name") String name) {
    	materialService.deleteByName(name);
        
    	HashMap<String, String> saveResult = new HashMap<>();
        saveResult.put("msg", "삭제되었습니다.");
        return new ResponseEntity(saveResult, HttpStatus.OK);
    }
 
    @SuppressWarnings("unchecked")
	@PutMapping(value = "/{name}")
    public ResponseEntity<?> updateAmount(@PathVariable(value = "name") String name, @RequestBody Material material) {
    	//Department oldDepartment = departmentService.findByCode(code);
    	material.setName(name);
    	materialService.updateAmount(material);
        
        HashMap<String, String> saveResult = new HashMap<>();
        saveResult.put("msg", "수정되었습니다.");
        return new ResponseEntity(saveResult, HttpStatus.OK);
    }

}

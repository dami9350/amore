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

import com.me.custom.domain.Order;
import com.me.custom.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(value = "/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping(value = "")
    public ResponseEntity<List<Order>> findAll() {
        List<Order> order = orderService.findAll();
        return ResponseEntity.ok(order);
    }
    
    @GetMapping(value = "/{orderNumber}")
    public ResponseEntity<Order> findByOrderNumber(@PathVariable(value = "orderNumber") String orderNumber) {
        Order order = orderService.findByOrderNumber(orderNumber);
        return ResponseEntity.ok(order);
    }
 
	@PostMapping(value = "")
    public ResponseEntity<?> save(@RequestBody Order order) {
    	orderService.orderCheck(order);
        
    	if(!"".equals(order.getOrder_status()) && !"0".equals(order.getOrder_status())) {
    		orderService.save(order);
    	}
    	log.info("============================");
    	log.info("주문 내역 : " + order.toString());
    	log.info("============================");
    	
        return new ResponseEntity<Object>(order, HttpStatus.OK);
    }
 
    @DeleteMapping(value = "/{orderNumber}")
    public ResponseEntity<?> delete(@PathVariable(value = "orderNumber") String orderNumber) {
    	orderService.deleteByOrderNumber(orderNumber);
        
    	HashMap<String, String> saveResult = new HashMap<>();
        saveResult.put("msg", "삭제되었습니다.");
        return new ResponseEntity(saveResult, HttpStatus.OK);
    }

}

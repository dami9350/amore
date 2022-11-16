package com.me.custom.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.me.custom.domain.Order;
 
@Repository
public interface OrderMapper {
 
    List<Order> findAll();
    
    List<Order> findWaitList(int todayCnt);
 
    Order findByOrderNumber(String orderNumber);
    
    int findCountByOrderNumber(String orderNumber);
    
    int findByToday(String today);
    
    int findByStatus(String orderStatus);
 
    void save(Order order);
 
    void updateSendDate(Order order);
    
    void updateOrderStatus(Order order);
 
    void deleteByOrderNumber(String orderNumber);
    
    void updateSendStatus();
    
    void updateProduceStatus();
 
}
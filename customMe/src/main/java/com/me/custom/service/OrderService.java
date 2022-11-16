package com.me.custom.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.me.custom.domain.Material;
import com.me.custom.domain.Order;
import com.me.custom.mapper.MaterialMapper;
import com.me.custom.mapper.OrderMapper;
import com.me.custom.utils.ComUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final OrderMapper orderMapper;
	
	private final MaterialMapper materialMapper;
	
    public List<Order> findAll() {
        return orderMapper.findAll();
    }
 
    public Order findByOrderNumber(String orderNumber) {
        return orderMapper.findByOrderNumber(orderNumber);
    }
 
    public void save(Order order) {
    	orderMapper.save(order);
    }
    
    public void orderCheck(Order order) {
    	// 주문번호 중복 체크
    	if(orderMapper.findCountByOrderNumber(order.getOrder_number()) > 0) {
    		order.setOrder_status("0");
    		order.setError("주문번호 중복");
    		return;
    	}

    	if(!ComUtils.checkWorkTime()) {
    		// 근무 외 시간
    		// 대기 후 다음날 생산
    		order.setOrder_status("1");
    		
    		log.info("============================");
			log.info("[근무 외 시간으로 생산 대기] " + order.toString());
			log.info("============================");
    		return;
    	}
    	
    	int todayCnt = orderMapper.findByStatus("2");
		if(todayCnt >= 30) {
			// 금일 생산 갯수가 30개 이상일 경우 대기 후 다음날 생산
			order.setOrder_status("1");
			
			log.info("============================");
			log.info("[금일 생산 건수 초과로 생산 대기] " + order.toString());
			log.info("============================");
			return;
		}
    	
		// 근무 시간 일때 금일 생산 갯수 확인
		//LocalDateTime now = LocalDateTime.now();
    	//order.setOrder_date(now);
		//String nowDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		//int todayCnt = orderMapper.findByToday(nowDate);
		
		materialCheck(order);
    }
    
    public void materialCheck(Order order) {
    	
    	// 효능 validation 체크 및 재고 체크
		String orderCode = order.getOrder_code();
		
		String order1Code = "";
		String order2Code = "";
		
		int order1Amount = 0;
		int order2Amount = 0;
		
		try {
			order1Code = orderCode.substring(0, 1);
			order1Amount = Integer.parseInt(orderCode.substring(1, 2));
			if(order1Amount == 1 && "0".equals(orderCode.substring(2, 3))) {
				order1Amount = Integer.parseInt(orderCode.substring(1, 3));
			} else {
				order2Code = orderCode.substring(2, 3);
				order2Amount = Integer.parseInt(orderCode.substring(3, 4));
			}
		} catch(Exception e) {
			order.setOrder_status("0");
			order.setError("주문코드 에러");
			return;
		}
		
		if((order1Amount + order2Amount) != 10) {
			order.setOrder_status("0");
			order.setError("주문코드 에러");
			return;
		}
		
    	// 주문코드 원료가 있는지 체크
		Material material = materialMapper.findByName(order1Code);
		if(material == null) {
			// 효능 단종
			order.setOrder_status("0");
			order.setError("효능 단종");
			return;
		}
		
		if(material.getAmount() - order1Amount <= 0) {
			// 원료 보충
			LocalDateTime maxSupplyDate = materialMapper.findMaxSupplyDateByStatus("2");
			material.setSupply_date(maxSupplyDate.plusSeconds(40));
			material.setStatus("2");
			materialMapper.updateStatus(material);
			
			order.setOrder_status("1");
			
			log.info("============================");
			log.info("[원료 재고 부족으로 생산 대기] " + order.toString());
			log.info("============================");
			return;
		}
		
		// 주문 원료가 1개면
		if("".equals(order2Code)) {
			// 원료 보충중인게 있는지 체크
			int supplyCnt = materialMapper.findByStatus("2");
			if(supplyCnt > 0) {
				order.setOrder_status("1");
				
				log.info("============================");
				log.info("[원료 보충 중으로 생산 대기] " + order.toString());
				log.info("============================");
			} else {
				// 원료1 재고 차감
				material.setAmount(material.getAmount() - order1Amount);
				materialMapper.updateAmount(material);
				
				order.setOrder_status("2");
				order.setSend_date(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
				log.info("============================");
				log.info("[제품 생산 중] " + order.toString());
				log.info("============================");
			}
			return;
		}
		
		Material material2 = materialMapper.findByName(order2Code);
		if(material2 == null) {
			// 효능 단종
			order.setOrder_status("0");
			order.setError("효능 단종");
			return;
		}
		
		if(material2.getAmount() - order2Amount <= 0) {
			// 원료 보충
			LocalDateTime maxSupplyDate = materialMapper.findMaxSupplyDateByStatus("2");
			material2.setSupply_date(maxSupplyDate.plusSeconds(40));
			material2.setStatus("2");
			materialMapper.updateStatus(material2);
			
			order.setOrder_status("1");
			
			log.info("============================");
			log.info("[원료 재고 부족으로 생산 대기] " + order.toString());
			log.info("============================");
			return;
		}
		
		// 원료 보충중인게 있는지 체크
		int supplyCnt = materialMapper.findByStatus("2");
		if(supplyCnt > 0) {
			order.setOrder_status("1");
			
			log.info("============================");
			log.info("[원료 보충 중으로 생산 대기] " + order.toString());
			log.info("============================");
		} else {
			// 원료1 재고 차감
			material.setAmount(material.getAmount() - order1Amount);
			materialMapper.updateAmount(material);
			
			// 원료2 재고 차감
			material2.setAmount(material2.getAmount() - order2Amount);
			materialMapper.updateAmount(material2);
			order.setOrder_status("2");
			order.setSend_date(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
			
			log.info("============================");
			log.info("[제품 생산 중] " + order.toString());
			log.info("============================");
		}
    }
 
    public void updateSendDate(Order order) {
    	orderMapper.updateSendDate(order);
    }
    
    public void updateOrderStatus(Order order) {
    	orderMapper.updateOrderStatus(order);
    }
 
    public void deleteByOrderNumber(String orderNumber) {
    	orderMapper.deleteByOrderNumber(orderNumber);
    }
	
}

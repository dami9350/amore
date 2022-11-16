package com.me.custom.batch;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.me.custom.domain.Material;
import com.me.custom.domain.Order;
import com.me.custom.mapper.MaterialMapper;
import com.me.custom.mapper.OrderMapper;
import com.me.custom.service.OrderService;
import com.me.custom.utils.ComUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class Batch {
	
	private final OrderService orderService;
	
	private final OrderMapper orderMapper;
	
	private final MaterialMapper materialMapper;
	
	// 근무외 시간 1번만 실행 되도록 처리할 변수
	private static boolean checkOffTimeOnlyOne = false;
	
	@Scheduled(fixedRate=5000) // 단위: ms
	public void fixedRateScheduler() {
		
		// 근무외 시간 처리
		if(!ComUtils.checkWorkTime()) {
			if(!checkOffTimeOnlyOne) {
				orderMapper.updateSendStatus();
				log.info("============================");
				log.info("[근무외 시간] 제품 생산 중인 건들 -> 발송 완료");
				log.info("============================");
				
				orderMapper.updateProduceStatus();
				log.info("============================");
				log.info("[근무외 시간] 주문 접수 -> 제품 생산 중(최대 30개)");
				log.info("============================");
				checkOffTimeOnlyOne = true;
			}
		} else {
			if(checkOffTimeOnlyOne) {
				checkOffTimeOnlyOne = false;
			}
			
			// 원료 보츙 처리
			Material material =  materialMapper.findBySupplyDate();
			if(material != null) {
				material.setAmount(200);
				material.setStatus("1");
				material.setSupply_date(null);
				materialMapper.updateSupplyInfo(material);
				log.info("============================");
				log.info("[ " + material.getName() + " 원료 보충 완료] " + material.toString());
				log.info("============================");
			} else {
				// 원료 보충 할게 없으면, 원료 보충 때문에 생산 대기 일 수 있는 주문 건 확인
				// 생산 건수 30 건 확인 후 30건 이하면 주문접수 건 생산 재개
				int todayCnt = orderMapper.findByStatus("2");
				if(todayCnt < 30) {
					List<Order> orderList = orderMapper.findWaitList(30 - todayCnt);
					for(Order order : orderList) {
						orderService.materialCheck(order);
						if("2".equals(order.getOrder_status())) {
							orderMapper.updateOrderStatus(order);
						}
					}
				}
			}
		}
	}
	
	@Scheduled(fixedRate=10000) // 단위: ms
	public void displayScheduler() {
		List<Order> orderList = orderMapper.findAll();
		String orderStatus = "";
		log.info("============================");
		log.info("[생산/대기 중인 주문 목록]");
		for(Order order : orderList) {
			orderStatus = ComUtils.getOrderStatusText(order.getOrder_status());
			
			log.info("주문번호 : " + order.getOrder_number() + ", 상태 : " + orderStatus);
		}
		
		List<Material> materialList = materialMapper.findAll();
		log.info("");
		log.info("[원료 잔량 목록]");
		for(Material material : materialList) {
			
			log.info("원료명 : " + material.getName() + ", 잔량 : " + material.getAmount());
		}
		log.info("============================");
		
		
	}
}

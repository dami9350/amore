package com.me.custom.utils;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComUtils implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final LocalDateTime startDate = LocalDateTime.of(2022, 11, 15, 11, 0);
	private static final long workSeconds = 480;
	private static final long offSeconds = 30;
	
	public static boolean checkWorkTime() {
		// 하루 8시간 근무 : 1분을 1초로 간주 (480초)
		// 근무시간 외 16시간은 30초로 간주
		// 하루 510초
		// 하루 중 근무외시간 비중 30 / 480 = 0.0625
		LocalDateTime now = LocalDateTime.now();
		
		Duration duration = Duration.between(startDate, now);
		long durationSeconds = duration.getSeconds();
		
		log.info("## 근무시간 : 480초 중 " + durationSeconds % (workSeconds + offSeconds));
		
		// 근무외 시간
		if((durationSeconds % (workSeconds + offSeconds)) >= workSeconds) {
			return false;
		}
		
		return true;
	}
	
	public static String getOrderStatusText(String orderStatus) {
		String orderStatusText = "";
		if("0".equals(orderStatus)) {
			orderStatusText = "주문 취소";
		} else if("1".equals(orderStatus)) {
			orderStatusText = "주문 접수";
		} else if("2".equals(orderStatus)) {
			orderStatusText = "제품 생산 중";
		} else if("3".equals(orderStatus)) {
			orderStatusText = "제품 생산 완료";
		} else if("4".equals(orderStatus)) {
			orderStatusText = "발송 준비 중";
		} else if("5".equals(orderStatus)) {
			orderStatusText = "발송 완료";
		}
		
		return orderStatusText;
	}
}

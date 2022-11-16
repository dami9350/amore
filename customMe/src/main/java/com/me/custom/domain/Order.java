package com.me.custom.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class Order {
	
	private String order_number;
	
	private String order_code;
	
	private String order_date;
	
	private String send_date;
	
	private String order_status;
	
	@JsonInclude(Include.NON_NULL)
	private String error;

}

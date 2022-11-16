package com.me.custom.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Material {
	
	private String name;
	private Integer amount;
	private String status;
	private LocalDateTime supply_date;

}

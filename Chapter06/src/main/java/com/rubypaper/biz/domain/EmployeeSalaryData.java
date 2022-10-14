package com.rubypaper.biz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
// 모든 멤버 변수를 초기화하는 생성자.
@AllArgsConstructor
public class EmployeeSalaryData {

	private Long id;
	private Double salary;
	private Double commissionPct;
}

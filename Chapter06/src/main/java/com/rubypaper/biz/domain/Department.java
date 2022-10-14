package com.rubypaper.biz.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "employeeList")
@Entity
@Table(name = "S_DEPT")
public class Department {

	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DEPT_ID")
	private Long deptId;

	private String name;

	// 하나의 부서는 여러 직원 정보를 가질 수 있도록 컬렉션 변수를 추가하였다.
	// 직원과 부서의 관계를 양방향으로 설정했는데, 다대일 일방향 관계에서는 다에 해당하는 엔티티가 연관관계의 주인이 된다.
	@OrderColumn(name="EMP_IDX")
	@OneToMany(mappedBy = "dept", cascade = CascadeType.PERSIST)
	private List<Employee> employeeList = new ArrayList<Employee>();
}

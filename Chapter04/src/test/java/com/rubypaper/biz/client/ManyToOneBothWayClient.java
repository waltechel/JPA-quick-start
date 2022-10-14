package com.rubypaper.biz.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class ManyToOneBothWayClient {

	public static void main(String[] args) {
		EntityManagerFactory emf = 
			Persistence.createEntityManagerFactory("Chapter04");
		try {		
			dataInsert(emf);
			dataDelete(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}
	
	private static void dataDelete(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 부서 검색
		Department departmemt = em.find(Department.class, 1L);

		// 직원의 부서 정보 수정
		List<Employee> employeeList = departmemt.getEmployeeList();		
		for (Employee employee : employeeList) {
			employee.standby();
		}

		// 부서 삭제
		em.remove(departmemt);


		em.getTransaction().commit();
		em.close();
	}

	private static void dataSelect(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		Department department = em.find(Department.class, 1L);
		
		System.out.println("검색된 부서 : " + department.getName());
		System.out.println("부서에 소속된 직원 명단");
		for (Employee employee : department.getEmployeeList()) {
			System.out.println(employee.getName() + "(" + 
				employee.getDept().getName() + ")");
		}
	}


	private static void dataInsert(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();		
		
		// 부서 등록
		Department departmemt = new Department();
		departmemt.setName("개발부");
//		em.persist(departmemt);
		
		// 직원 여러명 등록
		for (int i = 1; i <= 5; i++) {
			Employee employee = new Employee();
			employee.setName("직원-" + i);
			// 직원에 부서 정보 설정
			employee.setDept(departmemt);
//			em.persist(employee);
		}
		em.persist(departmemt);
		
		em.getTransaction().commit();
		em.close();
	}
}

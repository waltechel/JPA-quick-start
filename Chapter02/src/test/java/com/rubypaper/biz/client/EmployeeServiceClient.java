package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.rubypaper.biz.domain.Employee;
import com.rubypaper.biz.domain.EmployeeId;

public class EmployeeServiceClient {
	
	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		EntityManager em = emf.createEntityManager();
		try {
			// 회원 정보 검색 요청
			EmployeeId empId = new EmployeeId(1L, "guest123");
			Employee findEmployee = em.find(Employee.class, empId);
			
			System.out.println("검색된 직원 정보 : " + findEmployee.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
			emf.close();
		}
	}
}
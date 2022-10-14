package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class ManyToOneOneWayClient {

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
		
		Department department = em.find(Department.class, 1L);
		em.remove(department);
		em.getTransaction().commit();
	}	
	
	
	private static void dataUpdate(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 신규 부서 등록
		Department departmemt = new Department();
		departmemt.setName("영업부");
		em.persist(departmemt);

		// 부서 변경
		Employee employee = em.find(Employee.class, 1L);
		employee.setDept(departmemt);
		em.getTransaction().commit();
	}

	
	private static void dataSelect(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		Employee employee = em.find(Employee.class, 2L);
		System.out.println(employee.getName() + "의 부서 : " + 
			employee.getDept().getName());
	}

	private static void dataInsert(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 부서 등록
		Department departmemt = new Department();
		departmemt.setName("개발부");
		em.persist(departmemt);
		
		// 직원 등록
		Employee employee1 = new Employee();
		employee1.setName("둘리");
		employee1.setDept(departmemt);
		em.persist(employee1);
		
		// 직원 등록
		Employee employee2 = new Employee();
		employee2.setName("도우너");
		employee2.setDept(departmemt);
		em.persist(employee2);
		
		em.getTransaction().commit();
		em.close();
	}
}

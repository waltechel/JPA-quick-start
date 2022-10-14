package com.rubypaper.biz.client;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class CriteriaSearchClient {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
		try {
//			// 사용자가 입력한 검색 조건과 검색 단어를 이용한다.
//			Scanner keyboard = new Scanner(System.in);
//			System.out.println("검색 조건을 입력하세요. : name 혹은 mailid");
//			String searchCondition = keyboard.nextLine();
//			System.out.println("검색어를입력하세요.");
//			String searchKeyword = keyboard.nextLine();

			dataInsert(emf);
//			dataSelect(emf, searchCondition, searchKeyword);
			dataSelect(emf);
			
//			keyboard.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}

	private static void dataSelect(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Department> criteriaQuery = builder.createQuery(Department.class);

		// FROM Department dept
		Root<Department> dept = criteriaQuery.from(Department.class);
		// SELECT dept
		criteriaQuery.select(dept).distinct(true);

		// JOIN FETCH dept.employeeList
		dept.fetch("employeeList");
		// WHERE employeeList.size > 2
		criteriaQuery.where(builder.ge(builder.size(dept.<List<Employee>>get("employeeList")), 3));

		TypedQuery<Department> query = em.createQuery(criteriaQuery);
		List<Department> resultList = query.getResultList();
		for (Department department : resultList) {
			System.out.println(department.getName());
		}
		em.close();
	}

//	private static void dataSelect(EntityManagerFactory emf, 
//					String searchCondition, String searchKeyword) {
//		EntityManager em = emf.createEntityManager();
//
//		CriteriaBuilder builder = em.getCriteriaBuilder();
//		CriteriaQuery<Employee> criteriaQuery = 
//			builder.createQuery(Employee.class);
//
//		// FROM Emplyee emp
//		Root<Employee> emp = criteriaQuery.from(Employee.class);
//
//		// SELECT emp
//		criteriaQuery.select(emp);
//
//		// JOIN FETCH emp.dept dept
//		emp.fetch("dept");
//
//		if(searchCondition.equals("mailid")) {
//			// WHERE emp.mailId like %searchKeyword%
//			criteriaQuery.where(builder.like(emp.<String>get("mailId"), 
//			"%" + searchKeyword  + "%"));
//		} else if(searchCondition.equals("name")) {
//			// WHERE emp.name like %searchKeyword%
//			criteriaQuery.where(builder.like(emp.<String>get("name"), 
//			"%" + searchKeyword  + "%"));
//		}
//
//
//		TypedQuery<Employee> query = em.createQuery(criteriaQuery);
//		List<Employee> resultList = query.getResultList();
//		if(resultList.size() == 0) {
//			System.out.println("검색 결과가 없습니다.");
//		} else {
//			for (Employee result : resultList) {
//				System.out.println("---> " + result.toString());
//			}
//		}
//	
//		em.close();
//	}

	private static void dataInsert(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		// 부서 정보 등록
		Department devDept = new Department();
		devDept.setName("개발부");
		em.persist(devDept);

		Department salseDept = new Department();
		salseDept.setName("영업부");
		em.persist(salseDept);

		// 직원 정보 등록
		for (int i = 1; i <= 3; i++) {
			Employee employee = new Employee();
			employee.setName("개발맨 " + i);
			employee.setMailId("Corona" + i);
//			employee.setDeptName("개발부");
			employee.setDept(devDept);
			employee.setSalary(12700.00 * i);
			employee.setStartDate(new Date());
			employee.setTitle("사원");
			employee.setCommissionPct(10.00);
			em.persist(employee);
		}

		for (int i = 1; i <= 3; i++) {
			Employee employee = new Employee();
			employee.setName("영업맨 " + i);
			employee.setMailId("Virus" + i);
//			employee.setDeptName("영업부");
			employee.setDept(salseDept);
			employee.setSalary(23800.00 * i);
			employee.setStartDate(new Date());
			employee.setTitle("과장");
			employee.setCommissionPct(15.00);
			em.persist(employee);
		}

		// 부서 정보가 없는 직원 등록
		Employee employee = new Employee();
		employee.setName("아르바이트");
		employee.setMailId("Alba-01");
		employee.setSalary(10000.00);
		em.persist(employee);

		em.getTransaction().commit();
		em.close();
	}
}

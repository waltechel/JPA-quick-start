package com.rubypaper.biz.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class JPQLSubqueryClient {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter06");
		try {
			dataInsert(emf);
			dataSelect(emf);
			dataSelect1(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}

	private static void dataSelect(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT e, e.dept FROM Employee e ORDER BY e.id";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		int pageNumber = 2;
		int pageSize = 5;
		int startNum = (pageNumber * pageSize) - pageSize;
		query.setFirstResult(startNum);
		query.setMaxResults(pageSize);

		List<Object[]> resultList = (List<Object[]>) query.getResultList();
		System.out.println(pageNumber + "페이지에 해당하는 직원 목록");
		for (Object[] result : resultList) {
			Employee employee = (Employee) result[0];
			Department department = (Department) result[1];
			System.out.println(employee.getId() + " : " + employee.getName() +
 			"의 부서 " + department.getName());
		}

		em.close();
	}
	
	private static void dataSelect1(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT d FROM Department d WHERE (SELECT COUNT(e) FROM Employee e WHERE d.id = e.dept.deptId) >= 3";
		TypedQuery<Department> query = em.createQuery(jpql, Department.class);

		List<Department> resultList = query.getResultList();
		System.out.println("소속된 직원이 3명 이상인 부서 목록");
		for (Department result : resultList) {
			System.out.println(result.getName());
		}

		em.close();
	}

	/**
	 * 평균 급여보다 많은 급여를 받는 직원 목록
	 * @param emf
	 */
	private static void dataSelect2(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT e FROM Employee e WHERE e.salary > (SELECT AVG(e.salary) FROM Employee e)";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);

		List<Employee> resultList = query.getResultList();
		System.out.println("평균 급여보다 많은 급여를 받는 직원 목록");
		for (Employee result : resultList) {
			System.out.println(result.getName());
		}

		em.close();
	}
	
	/**
	 * 평균 급여보다 많은 급여를 받는 직원 목록
	 * @param emf
	 */
	private static void dataSelect3(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT e FROM Employee e JOIN FETCH e.dept WHERE e.salary > (SELECT AVG(e.salary) FROM Employee e)";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);

		List<Employee> resultList = query.getResultList();
		System.out.println("평균 급여보다 많은 급여를 받는 직원 목록");
		for (Employee result : resultList) {
			System.out.println(result.getName());
		}

		em.close();
	}
	
	/**
	 * 평균 급여보다 많은 급여를 받는 직원 목록
	 * @param emf
	 */
	private static void dataSelect4(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT e FROM Employee e WHERE NOT EXISTS (SELECT d FROM Department d WHERE d = e.dept)";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);

		List<Employee> resultList = query.getResultList();
		System.out.println("평균 급여보다 많은 급여를 받는 직원 목록");
		for (Employee result : resultList) {
			System.out.println(result.getName());
		}

		em.close();
	}

	private static void dataInsert(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Department department1 = new Department();
		department1.setName("개발부");
		for (int i = 1; i <= 3; i++) {
			Employee employee = new Employee();
			employee.setName("개발직원 " + i);
			employee.setSalary(i * 12700.00);
			employee.setMailId("Dev-" + i);
			employee.setDept(department1);
		}
		em.persist(department1);

		Department department2 = new Department();
		department2.setName("영업부");
		for (int i = 1; i <= 3; i++) {
			Employee employee = new Employee();
			employee.setName("영업직원 " + i);
			employee.setSalary(27300.00 * i);
			employee.setMailId("Sale-" + i);
			employee.setDept(department2);
		}
		em.persist(department2);

		Department department3 = new Department();
		department3.setName("인재개발부");
		em.persist(department3);

		// 부서 정보가 없는 새로운 직원 추가
		Employee employee = new Employee();
		employee.setName("아르바이트");
		employee.setMailId("Alba-01");
		employee.setSalary(10000.00);
		em.persist(employee);

		// 이름이 영업부인 새로운 직원 추가
		Employee employee2 = new Employee();
		employee2.setName("영업부");
		em.persist(employee2);

		em.getTransaction().commit();
		em.close();
	}

}

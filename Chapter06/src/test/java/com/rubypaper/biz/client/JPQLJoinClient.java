package com.rubypaper.biz.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class JPQLJoinClient {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter06");
		try {
			dataInsert(emf);
			dataSelect(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}

	private static void dataSelect(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT e FROM  Employee e " 
			     + "WHERE NOT EXISTS (SELECT d " 
			     + "                    FROM Department d " 
			     + "                    WHERE d = e.dept)";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
		
		List<Employee> resultList = query.getResultList();
		System.out.println("부서에 속해있지 않는 직원 명단");
		for (Employee employee : resultList) {
			System.out.println(employee.getName());
		}		

		em.close();
	}
	
	private static void dataSelect1(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT e FROM Employee e";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
		
		List<Employee> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for (Employee employee : resultList) {
			System.out.println(employee.getName());
		}		

		em.close();
	}

	private static void dataSelect2(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// jpql에 반스디 연관관계에 있는 객체가 언급되어야 한다. 
		// 묵시적 조인
		String jpql = "SELECT e, e.dept FROM Employee e";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		
		List<Object[]> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for (Object[] result : resultList) {
			Employee employee = (Employee) result[0];
			Department department = (Department) result[1];
			System.out.println(employee.getName() + "의 부서 " + department.getName());
		}		

		em.close();
	}

	private static void dataSelect3(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// jpql에 반드시 연관관계에 있는 객체가 언급되어야 한다. 
		// 명시적 조인
		// 명시적 조인을 사용할 때 INNER 라는 키워드는 생략할 수 있다.
		String jpql = "SELECT e, d FROM Employee e INNER JOIN e.dept d";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		
		List<Object[]> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for (Object[] result : resultList) {
			Employee employee = (Employee) result[0];
			Department department = (Department) result[1];
			System.out.println(employee.getName() + "의 부서 " + department.getName());
		}		

		em.close();
	}

	private static void dataSelect4(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// jpql에 반드시 연관관계에 있는 객체가 언급되어야 한다. 
		// 명시적 조인
		// 명시적 조인을 사용할 때 INNER 라는 키워드는 생략할 수 있다.
		String jpql = "SELECT e, d FROM Employee e LEFT OUTER JOIN e.dept d";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		
		List<Object[]> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for (Object[] result : resultList) {
			Employee employee = (Employee) result[0];
			Department department = (Department) result[1];
			if(department != null) {
				System.out.println(employee.getName() + "의 부서 " + department.getName());
			}else {
				// 이 아르바이트는 대기중이 조회되어야 한다.
				System.out.println(employee.getName() + "는 대기중");				
			}
		}		

		em.close();
	}

	private static void dataSelect5(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// jpql에 반드시 연관관계에 있는 객체가 언급되어야 한다. 
		// 세타 조인
		String jpql = "SELECT e, d FROM Employee e, Department d where e.name = d.name";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		
		List<Object[]> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for (Object[] result : resultList) {
			Employee employee = (Employee) result[0];
			Department department = (Department) result[1];
			System.out.println(employee.getName() + "의 부서 " + department.getName());
			// 이름이 영업부인 사람은 들어가겠다.
		}		

		em.close();
	}
	
	private static void dataSelect6(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// jpql에 반드시 연관관계에 있는 객체가 언급되어야 한다. 
		// 세타 조인
		String jpql = "SELECT e FROM Employee e";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		
		List<Object[]> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for (Object[] result : resultList) {
			Employee employee = (Employee) result[0];
			Department department = (Department) result[1];
			System.out.println(employee.getName() + "의 부서 " + department.getName());
			// 이름이 영업부인 사람은 들어가겠다.
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

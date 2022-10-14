package com.rubypaper.biz.client;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.rubypaper.biz.domain.Employee;
import com.rubypaper.biz.domain.EmployeeSalaryData;

public class JPQLBasicClient {
	
	public static void main(String[] args) {
		EntityManagerFactory emf = 
			Persistence.createEntityManagerFactory("Chapter06");
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

		// 첫 번째는 영속 컨테이너에 전송할 JPQL, 두 번째는 실행 결과를 매핑할 엔티티 타입
		String jpql = "SELECT e FROM Employee e WHERE e.id=1L";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
		
		// 1번 직원 검색
		Employee findEmp1 = query.getSingleResult();
		
		// 1번 직원 검색
		Employee findEmp2 = query.getSingleResult();
		
		if(findEmp1 == findEmp2) {
			System.out.println("두 객체의 주소는 동일하다.");
		}

		em.close();
	}
	
	private static void dataSelect1(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// 첫 번째는 영속 컨테이너에 전송할 JPQL, 두 번째는 실행 결과를 매핑할 엔티티 타입
		String jpql = "SELECT e FROM Employee AS e";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
		List<Employee> resultList = query.getResultList();
		
		// 검색 결과 처리
		System.out.println("검색된 직원 목록");
		for(Employee result : resultList) {
			System.out.println("---> " + result.toString());
		}

		em.close();
	}

	private static void dataSelect2(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// 첫 번째는 영속 컨테이너에 전송할 JPQL, 두 번째는 실행 결과를 매핑할 엔티티 타입
		String jpql = "SELECT id, name, deptName, salary FROM Employee";
		Query query = em.createQuery(jpql);
		List<Object[]> resultList = query.getResultList();
		
		// 검색 결과 처리
		System.out.println("검색된 직원 목록");
		for(Object[] result : resultList) {
			System.out.println("---> " + Arrays.toString(result));
		}
		
		em.close();
	}
	
	private static void dataSelect3(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// 첫 번째는 영속 컨테이너에 전송할 JPQL, 두 번째는 실행 결과를 매핑할 엔티티 타입
		String jpql = "SELECT NEW com.rubypaper.biz.domain.EmployeeSalaryData(id, salary, commissionPct) FROM Employee";
		
		// JPQL 전송
		TypedQuery<EmployeeSalaryData> query = em.createNamedQuery(jpql, EmployeeSalaryData.class);
		
		// 검색 결과 처리
		List<EmployeeSalaryData> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for(EmployeeSalaryData result : resultList) {
			System.out.println("---> " + result.toString());
		}
		
		em.close();
	}
	
	private static void dataSelect4(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// JPQL
		String jpql = "SELECT id, name, title, deptName, salary FROM Employee WHERE id = ?1 AND name = ?2";
		
		// JPQL 전송
		Query query = em.createQuery(jpql);
		query.setParameter(1, 1L);
		query.setParameter(2, "직원 1");
		
		// 검색 결과 처리
		Object[] result = (Object[]) query.getSingleResult();
		System.out.println(result[0] + "번 직원의 정보");
		System.out.println(Arrays.toString(result));
		
		em.close();
	}
	
	private static void dataSelect5(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// JPQL
		String jpql = "SELECT id, name, title, deptName, salary FROM Employee WHERE id = :employeeId AND name = :employeeName";
		
		// JPQL 전송
		Query query = em.createQuery(jpql);
		query.setParameter("employeeId", 1L);
		query.setParameter("employeeName", "직원 1");
		
		// 검색 결과 처리
		Object[] result = (Object[]) query.getSingleResult();
		System.out.println(result[0] + "번 직원의 정보");
		System.out.println(Arrays.toString(result));
		
		em.close();
	}
	
	private static void dataSelect6(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// 1번 직원 검색
		Employee findEmp1 = em.find(Employee.class, 1L);
		
		// 1번 직원 검색
		Employee findEmp2 = em.find(Employee.class, 1L);
		
		// 캐시에 있는 1번 엔티티가 재사용되게 되어 있다.
		if(findEmp1 == findEmp2) {
			System.out.println("두 객체의 주소는 동일하다.");
		}

		em.close();
	}
	
	private static void dataSelect7(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// 첫 번째는 영속 컨테이너에 전송할 JPQL, 두 번째는 실행 결과를 매핑할 엔티티 타입
		String jpql = "SELECT e FROM Employee e WHERE e.id=1L";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
		
		// 만약 직원이 없을 경우 nullpointerException이 발생한다.
		// 1번 직원 검색
		Employee findEmp1 = query.getSingleResult();
		
		// 1번 직원 검색
		Employee findEmp2 = query.getSingleResult();
		
		if(findEmp1 == findEmp2) {
			System.out.println("두 객체의 주소는 동일하다.");
		}

		em.close();
	}
	
	private static void dataInsert(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		// 10 명의 직원 정보 등록
		for (int i = 1; i <= 10; i++) {
			Employee employee = new Employee();
			employee.setName("직원 " + i);
			employee.setMailId("anti-corona" + i);
			employee.setDeptName("개발부");
			employee.setSalary(12700.00 * i);
			employee.setStartDate(new Date());
			employee.setTitle("사원");
			employee.setCommissionPct(15.00);
			em.persist(employee);
		}

		em.getTransaction().commit();
		em.close();
	}
}

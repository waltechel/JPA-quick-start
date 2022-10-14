# 06장 JPQL

## section 01 JPQL 기본

SQL과 JPQL의 차이
- SQL은 관계형 데이터베이스에 전달되어 직접 데이터를 조작
- JPQL은 데이터베이스가 아닌 영속 컨테이너에 전달되어 영속 컨테이너에 등록된 엔티티를 조작

# JPQL 기초

목록을 검색하기 위해서는 createQuery라는 별도의 메서드를 사용해야 한다. 이때, createQuery 메서드 인자로 JPQL을 전달한다.

## 1.1 JPQL 기본 구조

EntityManager의 createQuery 메서드를 호출할 때 인자로 JPQL에 해당하는 문자열을 전달하면 영속 컨테이너는 JPQL을 우리가 알고 있는 일반적인 SQL로 변환한다. 당연히 이때 변환되는 SQL은 `persistence.xml` 파일에 설정된 방언 설정에 영향을 받는다.

- SQL로 표현

```sql
SELECT D.NAME, MAX(E.SALARY)
FROM EMP E JOIN DEPT D ON E.DEPT_ID = D.DEPT_ID
GROUP BY D.NAME
HAVING AVG(E.SALARY) >= 300000.0
ORDER BY D.NAME DESC
```

- 같은 내용을 JPQL로 표현

```sql
SELECT D.NAME, MAX(E.SALARY)
FROM Employee E JOIN E.dept D
GROUP BY D.NAME
HAVING AVG(E.SALARY) >= 300000.0
ORDER BY D.NAME DESC
```

위 내용에서 가장 중요한 차이점은 JPQL의 FROM 절에 서술된 것이 테이블 이름이 아닌 엔티티 이름이라는 점이다. SQL에서 검색 대상은 당연히 테이블이지만, JPQL은 영속성 컨테이너에 영속 상태로 있는 엔티티들을 검색 대상으로 한다.

## 1.3 JPQL 사용 시 주의사항

JPQL에서는 FROM절에 테이블 이름이 아닌 검색할 엔티티 이름을 사용해야 하는데, 엔티티 이름은 `@Entity`의 name 속성으로 지정한 이름을 의미한다.

JPQL은 SELECT 절을 생략할 수도 있다. 

## 1.6 상세 조회와 엔티티 캐시

find 메서드의 중요한 특징은 find 메서드로 검색한 엔티티가 영속 컨테이너가 관리하는 캐시에 등록된다. 그래서 동일한 엔티티에 대해 반복적으로 find가 실행되는 경우에 캐시에 등록된 엔티티를 재사용한다. 반면 createQuery 메서드를 이용하면 캐시에 엔티티가 존재하는 것과 무관하게 반복적으로 SELECT 를 수행한다.

















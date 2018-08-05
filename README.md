
### 1. 잘못된 예외코드 작성 
#### 예외 블랙홀 
```
try{
}
catch(SQLException e){
}
```
예외가 발생하면 그것을 catch 블록을 써서 잡아내는 것까지는 좋은데 그리고 **아무것도 하지 않고 별문제 없는 것처럼 넘어가 버리는 건 정말 위험한 일**이다. 
어떤 경우에도 위와 같은 코드를 만들면 안된다. 습관이 무서우니 코딩연습이나 예제를 잠깐 만드는 경우도 안된다. 

```
}catch (SQLException e){
  System.out.println(e);
}
...
}catch (SQLException e){
  e.printStackTrace();
}
```
예외가 발생하면 화면에 출력해준다. 하지만 예외는 처리되어야 한다. 단순하게 화면에 메세지를 출력한 것은 예외를 처리한 것이 아니다. 
**모든 예외는 적절하게 복구되든지 아니면 작업을 중단시키고 운영자 또는 개발자에게 분명하게 통보**돼야 한다. 

SQLException이 발생하는 이유는 SQL에 문법 에러가 있거나 DB에서 처리할 수 없을 정도로 데이터 액세스 로직에 심각한 버그가 있거나, 
서버가 죽거나 네트워크가 끊기는 등의 심각한 상황이 발생했기 때문이다. 그런데 예외를 무시하고 정상동작 하고 있는 것 처럼 모들 척 
다음 코드로 실행을 이어간다는 건 말이 되지 않는다. 

```
} catch (SQLException e){
	e.printStackTrace();
	System.exit(1);
}
```
운영환경에서 이러한 프로그램을 짜는것은 매우 위험하다. 하지만 예외를 무시하거나 잡아먹어버리는 코드보다는 낫다. 
굳이 예외를 잡아서 뭔가 조치를 취할 방법이 없다면 잡지 말고 throws SQLException을 선언해서 메소드 밖으로 던지고 자신을 호출한 코드에 책임을 전가해라. 


#### 무책임한 throws
```
public void method1() throws Exception { method2(); }
public void method2() throws Exception { method3(); }
public void method3() throws Exception { .... }

```
catch 블록으로 예외를 잡아봐야 해결할 방법도 없고 JDK API나 라이브러리가 던지는 각종 이읆도 긴 예외들을 처리하는 코드를 매번 throws 로 선언하기도 귀찮으면
메소드 선언에 throws Exception을 기계적으로 붙이는 개발자도 있다. 이런 메소드 선언에서는 의미 있는 정보를 얻을 수 없다. 

### 2. 예외의 종류와 특징 
* java.lang.Error 
	* 시스템에서 비정상적인 상황이 발생했을 경우에 사용.  주로 자바 VM에서 발생시키는 OutOfMemoryError, ThreadDeath 것이고 애플리케이션 코드에서 잡으려고 하면 안된다. 
catch로 잡아봤자 아무런 대응방법이 없다. 따라서 시스템 레벨의 프로그램을 작성하는 것이 아니라면 이런 에러에 대한 처리는 신경쓰지 않아도 된다. 

* java.long.Exception 
	* 개발자들이 만든 어플리케이션 코드의 작업 중 예외상황이 발생했을 경우에 사용된다. Exception 클래스는 다시 *checked exception*과 *unchecked exception* 으로 나눌 수 있다. 
	 checked Exception은 Exception 클래스의 서브클래스이면서 RuntimeException 클래스를 상속하지 않은 것들이고, unchecked exception는 RuntimeException을 상속한 클래스를 말한다. 
	 
	* 일반적으로 예외라고 하면 Exception 클래스의 서브클래스 중 checked exception을 말하는 것이고 checked exception이 발생한다면 반드시 예외를 처리하는 코드를 함께 작성해야 한다. 
	catch 문으로 처리하던지, throws 를 정의해서 메소드 밖으로 던져야 한다. 그렇지 않으면 컴파일 에러가 발생한다. 
	
	* java.lang.RuntimeException 클래스를 상속한 예외들은 명시적인 예외처리를 강제하지 않기 때문에 unchecked exception 이라고 불리고, runtime exception 이라고도 한다. 
	runtime exception은 개발자의 부주의로 인해 발생할 수 있는 경우를 의미한다. NullPointerException이나 IllegalArgumentException등이 있다. 
	
	* checked exception의 예외처리를 강제하는 것 때문에 예외 블랙홀이나 무책임한 throws 가 남발했고, 최근의 자바 표준 스펙들은 가능한 checked exception 으로 만들지 않고 있다. 
	

### 3. 예외 처리방법 
예외 처리 방법에는 예외복구, 예외처리 회피, 예외 전환이 있다. 
* 예외 복구 
	* 예외상황을 파악하고 문제를 해결에서 정상상태로 돌려놓는 것. 예외가 처리됐으면 비록 기능적으로는 사용자에게 예외상황으로 비쳐도 애플리케이션에서는 정상적으로 설계된 흐름을 따라 진행되어야 한다. 
	예외처리 코드를 강제하는 체크 예외들은 이렇게 예외를 어떤 식으로든 복구할 가능성이 있는 경우에 사용된다. 
* 예외처리 회피 
	* 예외처리를 자신이 담당하지 않고 자신을 호출한 쪽으로 던져버리는 것.  catch 로 예외를 잡은 다음 로그를 남기고 다시 예외를 던지는 방법이다. 
	```
	public void add() throws SQLException {
		try {
			// JDBC API
		} catch (SQLException e){
			// 로그 출력 
			throw e;
		}
	}
	```
	* SQLException은 콜백 오브젝트의 역할이 아니라고 보기 때문에, 콜백 오브젝트의 메소드는 SQLException에 대한 예외를 회피하고 템플릿 레벨에서 처리하도록 던져준다. 
	하지만 만약 DAO가 SQLException을 던져버린다면 DAO를 사용하는 서비스 계층이나 웹컨트롤러에서 이를 처리할 수 없다. 
* 예외전환 
	* 예외 회피와는 달리, 발생한 예외를 그대로 넘기는 게 아니라 적절한 예외로 전환해서 던진다. 이는 다음과 같이 두가지 목적을 위해서 사용된다. 
	보통 전환하는 예외에 원래 발생한 예외를 담아서 중첩 예외로 만드는 것이 좋다. 
		1. 적절한 의미를 부여하기 위해서 
		```
		catch(SQLException e){
			throw DuplicateUserIdException(e)
		}
		```
		
		2. 예외를 처리하고 쉽고 단순하게 만들기 위해서 포장 
		ckecked exception을 unchecked exception(runtime) 으로 변환하여 전달한다. 
		아래와 같이 EJBException으로 만들어서 전달하면 EJB는 이를 시스템 익셉션으로 인식하고 트랜잭션을 자동으로 롤백해준다. 
		```
		try { 
			OrderHome orderHome = EJBHomeFactory.getInstance().getOrderHome();
			Order order = orderHome.findByPromaryKey(Integer id); 
		} catch (NamingExcetion ne) {
			throw new EJBException(ne); 
		} catch (SQLException se) {
			throw new EJBException(se);
		} catch (RemoteException re){
			throw new EJBException(re);
		}
		```

### 4. 예외 처리 전략 
예외를 효과적으로 사용하고 예외가 발생하는 코드를 깔끔하게 정리하는 데는 여러 가지 신경 써야 할 사항이 많다. 

* 서버 특정 계층에서 예외가 발생했을 때 작업을 일시 중지하고 사용자와 바로 커뮤니케이션하면서 예외상황을 복부할 수 있는 방법이 없다. 
예외가 발생하지 않도록 차단하는 것이 가장 좋다. 만약 어쩔 수 없이 예외가 발생하는 경우 요청된 작업을 취소하고 서버 관리자나 개발자에게 통보해주는 편이 낫다. 
자바 환경이 서버로 이동하면서 checked Exception 활용도와 가치는 점점 떨어지고 있따. 그래서 대응이 불가능한 예외라면 빠르게 runtime exception으로 전환해서 던지는게 낫다. 

#### 4.1 어플리케이션 예외 
* 어플리케이션 자체의 로직에 의해 의도적으로 발생시키고, 반드시 catch 해서 무엇인가 조치를 취하도록 요구하는 예외를 말한다. 
이를 처리하는 방식은 크게 2가지로 나타낼 수 있다. 

	1. 발생할 수 있는 모든 경우를 리턴 값을 명확하게 코드화 하고해서 사용한다.  값의 표준을 정하는 것이 어려우며, 의사소통이 제대로 되지 않는다면 동작하지 않을 수 있다. 
	코드가 if 블록으로 범벅이 될 수도 있어 코드가 지저분해지고 흐름을 파악하고 이해하기 어려워진다. 
	
	2. 정상적인 코드는 그대로 두고 예외 상황에서 비즈니스적인 의미를 띤 예외를 던지도록 한다. try~catch 블록을 사용함으로 깔끔하고 코드를 이해하기 쉬우며, if 문을 남발하지 않아도 된다.  
	 이때 사용하는 예외는 의도적으로 checked exception으로 생성하는데, runtime exception으로 만들어두는 것 보다 상대적으로 안전하다. 
	 

####  4.2 SQLException의 처리 
* 대부분의 SQLException은 복구가 불가능하다. 그리고 DAO 밖에서 SQLException을 다룰 수 있는 가능성은 거의 없다. 
따라서 예외처리 전략을 적용해야 한다. 필요도 없는 기계적인 throws 선언이 등장하도록 방치하지 말고 가능한 빨리 언체크/런타임 예외로 전환해서 시스템이 중지되지 않도록 한다. 

* Spring의 JdbcTemplate은 바로 이 전략을 사용하는데, JdbcTemplate 템플릿과 콜백 안에서 발생하는 모든 SQLEXception 을 DataAccessException으로 포장해서 던진다. 
따라서 JdbcTemplate을 사용하는 UserDao 메소드에선 꼭 필요한 경우에만 런타임 예외인 DataAccessException을 잡아서 처리하면 되고 그 외의 경우에는 무시해도 된다. 

### 5. 예외 전환 
앞서 말한 것 처럼 예외 전환의 목적은 굳이 필요하지 않은 catch/throws 를 줄여 주는 것이고, 좀 더 의미있고 추상화된 예외로 바꿔 던져주는 것이다. 

#### 5.1 JDBC 한계
JDBC는 자바 표준 JDK 중에서 가장 많이 사용되는 기능 중 하나이다.  JDBC는 자바를 이용해 DB에 접근하는 방법을 추상화된 API 형태로 정의해놓고, 표준에 따라 만들어진 드라이버를 제공한다. 
인터페이스를 사용하는 객체지향 프로그래밍 방법의 장점을 잘 경험할 수 있는 것이 바로 이 JDBC 이다. 하지만 DB 종류와 상관없이 자유롭게 변경해서 사용할 수 있는 유연한 코드를 보장하지는 못한다. 
DB를 자유롭게 바꾸어 사용할 수 있는 DB 프로그램을 작성하는데 2가지 문제점이 있다. 
	1. 비표준SQL : DB에서 특별한 기능을 사용하거나 최적화된 SQL을 만들어서 표준을 따르지 않는다. 이렇게 작성된 SQL이 DAO에 들어가게 되면 결국 DAO가 DB에 종속된다. 
	이를 해결하기 위해서는 DAO를 DB별로 만들어 사용하거나 *SQL을 외부에서 독립시켜 바꿔 쓸 수 있게 하는 것*이다. 
	2.  호환성 없는 SQLException : JDBC는 데이터 처리 중에 발생하는 다양한 예외를 SQLEXception 하나에 모두 담는다. 예외 발생 원인은 SQLException 안에 담긴 에러코드/SQL상태정보를 가지고 알 수 있는데
	이는 DB 벤더마다 정의한 고유한 에러코드를 사용한다.  그리고 더 문제는 DB의 JDBC 드라이버에서 SQLException을 담을 상태코드를 정확하게 만들어주지 않는다는 것이다. 이를 가지고 DB에 독립적인 코드를 작성하는건 불가능하다. 
	
#### 5.2 JDBC 한계 극복 방안 
	1. DB 에러코드 맵핑을 통한 전환 
	```
	<bean id="Oracle" class="org.springframework.jdbc.support.SQLErrorCodes">
		<property name="badSQLGrammerCodes">
			<value>900,903,904,917,936,942,17006</value>
		</property>
		<property name="invalidResultSetAccessCodes">
			<value>17003</value>
		</property>
		<property name="duplicateKeyCodes">
			<value>1</value>
		</property>
		<property name="dataIntegrityViolationCodes">
			<value>1400,1722,2291,2292</value>
		</property>
		<property name="dataAccessResourceFailureCodes">
			<value>17002,17447</value>
		</property>
		...
	```
	스프링은 DB별 에러 코드를 분류해서 스프링이 정의한 예외 클래스와 매핑해놓은 에러 코드 매핑정보 테이블을 만들어두고 이를 이용한다. 
	JdbcTemplate은 SQLException을 단순 런타임 예외인 DataAccessException으로 포장하는 것이 아니라 DB에러코드를 DataAccessException 계층구조의 클래스 중 하나로 맵핑해준다. 
	
	필요하다면 아래와 같이 직접 정의한 코드로 전환하여 사용할 수도 있다. 
	```
	public void add() throws DuplicateUserIdException{
		try { 
			// jdbcTemplate을 이용해 user를 add하는 코드 
		}
		catch(DuplicateKeyException e){ 
			//  로그를 남기는 등의 필요한 작업 
			// 예외를 전환할 때 원인이 되는 예외를 중첩하는 것이 좋다. 
			throw new DuplicateuserIdException(e);
		}
	}
	```
	JDK 1.6에 포함된 JDBC 4.0 부터는 단일 예외 클래스였던 SQLException을 스프링의 DataAccessException과 비슷한 방식으로 좀 더 세분화 해서 정의하고 있다. 
	하지만 SQLException의 서브 클래스 임으로 여전히 checked exception 이라는 점과 세분화 기준이 SQL 상태정보를 이용한다는 점에서 여전히 문제가 있다. 
	그렇기 때문에 Spring 에러코드 매핑을 통한 DataAccessException 방식을 사용하는 것이 이상적이다. 
	
	
#### 5.3 DataAccessException 계층구조 
* DataAccessException은 JDBC의 SQLException을 단순히 전환하는 용도로 만들어진것은 아니고 JDBC 외의 자바 데이터 액세스 기술(JDO, DPA 등)에서 발생하는 예외에도 적용되는데, 
데이터 액세스 기술 종류와 상관없이 의미가 같은 예외라면 일관된 예외가 발생하도록 만들어준다. 

* 우리가 DAO를 굳이 따로 만들어서 사용하는 이유 중 가장 중요한 것은 데이터 액세스 로직을 담은 코드를 성격이 다른 코드에서 분리해 놓기 위해서이다. 
그런데 DAO 사용 기술과 구현코드는 전략패턴과 DI를 통해서 DAO를 사용하는 클라이언트에게 감출 수 있지만, 메소드 선언에 나타나는 예외정보가 문제가 될 수 있다. 
인터페이스 메소드 선언에는 없는 예외를 구현 클래스 메소드의 throws 에 넣을 수 없다. 따라서 인터페이스 메소드도 다음과 같이 선언되어야 한다. 

```
public interface UserDao{
	public void add(User user) throws SQLException;
	
	// 하지만 아래와 같이 사용할 수 없다.
	// public void add(User user) throws PersistenException;
	// public void add(User user) throws HibernateException;
	// public void add(User user) throws JdoException;
	...
}
```
* 결국 인터페이스 메소드로 구현하여 추상화 했지만 구현 기술마다 던지는 예외가 다르기 때문에 메소드 선언이 달라지게 된다. 가장 단순한 해결 방법은 
모든 예외를 다 받아주는 throws Exception으로 선언하는 것이다. 하지만 이는 앞서 말했듯 너무 무책임하다. 

* 다행히도 JDBC 보다 늦게 등장한 JDO, Hibernate, JPA 등은 SQLException 같은 checked exception 대신에 unchecked exception을 사용하기 때문에 
throws 부분을 삭제해도 된다. 하지만 runtime exception으로 모든 예외를 다 무시해야 하는 경우만 있지 않고, 비즈니스 로직에 따라서 데이터 액세스 예외를 의미있게 분류할 수도 있기 때문에 
DAO 를 사용하는 클라이언트 입장에서는 DAO 사용 기술에 따라서 구현이 달라져야 한다. 결국 클라이언트가 DAO 기술에 의존적이 될 수 밖에 없다. 

* 스프링은 자바의 다양한 데이터 액세스 기술을 사용할 때 발생하는 예외들을 추상화해서 DataAccessException 계층 구조 안에 정리해 놓았다. 
DataAccessException 클래스들이 단지 JDBC SQLException 전환하는 용도로만 사용하는 것이 아니라 JPA, 하이버네이트, JDBC가 공통적으로 발생시키는 예외를 포함하여 각각의 기술에서 
발생 가능한 대부분의 예외를 계층구조로 분류해놓았다. 

* 결론적으로 JdbcTemplate과 같이 스프링의 데이터 액세스 지원 기술을 이용해 DAO를 만드면 사용 기술에 독립적인 일관성 있는 예외를 던질 수 있다. 
결국 인터페이스 사용, 런타임 예외 전환과 함께 DataAccessException 예외 추상화를 적용하면 데이터 액세스 기술과 구현 방법에 독립적이고 이상적인 DAO 를 만들 수 있다. 







		
	
	




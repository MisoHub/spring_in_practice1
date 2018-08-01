
### 잘못된 예외코드 작성 
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

### 예외의 종류와 특징 
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
	

### 예외 처리방법 
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

### 예외 처리 전략 
예외를 효과적으로 사용하고 예외가 발생하는 코드를 깔끔하게 정리하는 데는 여러 가지 신경 써야 할 사항이 많다. 

* 서버 특정 계층에서 예외가 발생했을 때 작업을 일시 중지하고 사용자와 바로 커뮤니케이션하면서 예외상황을 복부할 수 있는 방법이 없다. 
예외가 발생하지 않도록 차단하는 것이 가장 좋다. 만약 어쩔 수 없이 예외가 발생하는 경우 요청된 작업을 취소하고 서버 관리자나 개발자에게 통보해주는 편이 낫다. 
자바 환경이 서버로 이동하면서 checked Exception 활용도와 가치는 점점 떨어지고 있따. 그래서 대응이 불가능한 예외라면 빠르게 runtime exception으로 전환해서 던지는게 낫다. 

#### 어플리케이션 예외 
* 어플리케이션 자체의 로직에 의해 의도적으로 발생시키고, 반드시 catch 해서 무엇인가 조치를 취하도록 요구하는 예외를 말한다. 
이를 처리하는 방식은 크게 2가지로 나타낼 수 있다. 

	1. 발생할 수 있는 모든 경우를 리턴 값을 명확하게 코드화 하고해서 사용한다.  값의 표준을 정하는 것이 어려우며, 의사소통이 제대로 되지 않는다면 동작하지 않을 수 있다. 
	코드가 if 블록으로 범벅이 될 수도 있어 코드가 지저분해지고 흐름을 파악하고 이해하기 어려워진다. 
	
	2. 정상적인 코드는 그대로 두고 예외 상황에서 비즈니스적인 의미를 띤 예외를 던지도록 한다. try~catch 블록을 사용함으로 깔끔하고 코드를 이해하기 쉬우며, if 문을 남발하지 않아도 된다.  
	 이때 사용하는 예외는 의도적으로 checked exception으로 생성하는데, runtime exception으로 만들어두는 것 보다 상대적으로 안전하다. 
	 

### SQLException의 처리 
* SQLException은 과연 복구가 가능한 예외인가?

	





		
	
	




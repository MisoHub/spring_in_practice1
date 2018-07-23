# spring_in_practice1
2018 Spring 공부 - 토비, 프레임워크 실제 등 


기존 소스에서는 예외처리를 위해서 코드가 많이 중복되어, 변하지 않는 부분에 메소드 추출이 필요. 
-> 템플릿 메소드 패턴을 적용한다. 

* Template method pattern 
  변하지 않는 부분은 슈퍼클래스super class에 두고 변하는 부분은 추상 메소드abstract method로 정의해둬서 
 서브클래스sub class에서 오버라이드override하여 새롭게 정의해 쓰도록 하는 방식. 
 
 개방폐쇄원칙(OCP, open close principal)에는 부합하지만,
 1) DAO 로직마다 상속을 통해 새로운 클래스를 생성해야 한다. 
 2) 확장구조가 클래스를 설계하는 시점에서 고정되어 버린다. 
 
 -> 전략 패턴을 적용한다. 
 
 * Strategy Pattern 
   OCP를 유지하면서 템플릿 메소드 패턴보다 유연하고 확장성이 뛰어남
  contextMethod를 통해서 변하지 않는 부분을 수행하고, 전략 인터페이스interface를 통해 특정 확장기능을 제공한다. 
  contextMethod에서 직접 전략 인터페이스를 생성하는 것이 아니라, client에서 생성하여 contextMethod에 전달해 준다. 
  이것은 기존 소스에서 사용하던 팩토리 패턴, DI를 사용한 것이다. 
  
  전략strategy의 확장 기능을 위해 지속적으로 전략인터페이스를 구현implements 하게 되면 위와 마찬가지로 기능 별 class가 늘어난다. 
  이를 극복하기 위해서 클라이언트에서 전략 인터페이스를 중첩 클래스로nested class 로 직접 구현한다. 
  
  * Nested Class
    > Static Class
  
    > Inner Class 
      > 멤버 내부 클래스 member inner class
      > 로컬 내부 클래스 local inner class
      > 익명 내부 클래스 anonymous inner class
      
  이 branch 에 작성된 소스코드는 익명 내부 클래스로 작성이 되었다. 
  작성된 코드의 한계점은 전략 패턴으로 수행된 결과가 서로다른 return 타입을 가지고 있다고 한다면 별도의 상속관계를 갖어야 한다는 것이다. 
  (뭐.. 당연한건가)
  
   

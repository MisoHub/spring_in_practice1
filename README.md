# 다양한 컨택스트Context DI 방식 

지난번까지 공통 부분을 메소드 추출을 통하여 코드 중복을 줄이고, 전략패턴을 사용하여 적절한 Statement를 DI함으로써
정상동작 할 수 있도록 하였다. (get/getCount와 같은 결과를 ResultSet 으로 받아야 하는 경우는 제외) 

JDBC의 일반적인 작업 흐름을 담고 있는 jdbcContextWithStatementStrategy() 메소드는 다른 DAO에서도 사용 가능하다. 
그렇기 때문에 UserDao 클래스 밖으로 독립시켜 모든 DAO가 사용할 수 있게 하는 것이 좋다. JdbcContext라는 클래스를 생성하고, 
그 안에서 DataSource 인스턴스를 DI 받을 수 있도록 수정자setter를 설정한다. 그리고 DataSource로 받는 connection을 이용해 
PreparedStatement를 수행 후 각 인스턴스들을 close 시켜 리소스를 종료하게 된다.  

* 스프링 빈 DI 
- UserDao에서 JdbcContext를 DI받아 사용하기 위해서는 빈Bean 의존관계를 생성해야 한다. 만약 UserDao에서 JdbcContext를 주입 받지 않고 
직접 생성하여 사용하더라도 JdbcContext에서 DataSource를 DI받아 사용하기 때문에 빈 의존관계를 생성해야 한다. 
여기서 문제가 될 수 있는 것은 JdbcContext가 인터페이스interface가 아닌 구체클래스concrete class 라는 것이다. 
스프링DI는 기본적으로 인터페이스를 사이에 두고 의존 클래스를 바꿔서 사용하도록 하는게 목적이다. 
하지만 JdbcContext의 경우 그 자체로 독립적인 JDBC 컨텍스트를 제공하는 서비스 오브젝트service object 이기 때문에 구현 방법이 바뀔 가능성이 없다. 
결론적으로 인터페이스가 아니지만 xml 파일에 JdbcContext에 대한 의존관계를 설정해도 무방하다. (UserDao --> JdbcContext --> DataSource) 

- 위와 같이 UserDao는 인터페이스를 거치지 않고 코드에서 바로 JdbcContext 클래스를 사용하고 있다. 
그래서 xml 파일에 의존관계를 설정하여 런타임에 DI 방식으로 외부에서 오브젝트 주입을 해주긴 하지만, 
의존 오브젝트(JdbcContext)의 구현클래스를 변경할 수 없다. 
DI의 개념을 충실히 따르면 인터페이스를 둬서 코드에서 클래스 의존관계가 고정되지 않게 해야하기 때문에, 위에서 생성한 JdbcContext 의존관계는 
온전한 DI라고 볼 수는 없다. 그러나 스프링DI의 의미를 확장하여 객체 생성 및 제어권한을 외부로 위임했다는 IoC 개념으로 볼 수도 있다. 

- 그렇다면 굳이 JdbcContext를 DI 구조로 만들어야 하는 이유는 무엇일까? 
1) JdbcContext를 싱글톤 빈으로 만들기 위해서이다. 
2) JdbcContext가 다른 빈에 의존하고 있기 때문이다. DI를 위해 주입 하는 쪽과 받는 쪽 모두 스프링 빈으로 등록되어야 한다. 

- 구체 클래스로 DI를 하는 경우 이렇게 사족이 많이 붙는데.. 왜 인터페이스를 사용하지 않았을까?
1) JdbcContext는 DataSource와 달리 테스트/운영 환경에 따라 다른 구현으로 대체해서 사용할 이유가 없다. 


* 수동 DI 적용 
- JdbcContext 스프링 빈으로 등록해서 UserDao 에 DI 하는 대신 UserDao 내부에서 직접 DI를 적용하는 방법도 있다. 
이렇게 한다면 JdbcContext가 싱글톤으로 생성되지 않는데, 조금 느슨하게 DAO 마다 하나의 JdbcContext 오브젝트를 갖고 있게 한다고 해도 
DAO가 수백개가 되어 JdbcContext가 수백개 만들어져도 메모리 부담은 크지 않다. 
그러면 DataSource와의 의존관계로 인한 DI는 어떻게 해결할까? UserDao 에서 DataSource를 주입받아 JdbcContext 생성 시 전달해 주면 된다. 

- 또 궁금한 것이.. 이런 방법의 장점은 무엇일까? 
1) 

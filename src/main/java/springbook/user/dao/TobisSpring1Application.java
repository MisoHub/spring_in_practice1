package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

@SpringBootApplication
public class TobisSpring1Application {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//		SpringApplication.run(TobisSpring1Application.class, args);
//		UserDao dao = new DaoFactory().userDao();
		
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//		UserDao dao = context.getBean("userDao",UserDao.class);
		
		// 1.7.3 의존관계 검색(Dependency Lookup)
//		ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);

		ApplicationContext context = new GenericXmlApplicationContext("tobiAppContext.xml");
		UserDao dao = context.getBean("userDao",UserDao.class);

		User user = new User("whiteship","가나다","pw1234");
		
		dao.add(user);
		System.out.println(user.getId() + "등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		System.out.println(user2.getId() + "조회 성공");
		
//		CountingConnectionMaker ccm = context.getBean("connectionMaker",CountingConnectionMaker.class);
//		System.out.println("Connection counter :" + ccm.getCounter());
		
	}
}

package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TobisSpring1Application {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//		SpringApplication.run(TobisSpring1Application.class, args);
		
		UserDao dao = new UserDao();
		
		User user = new User();
		user.setId("whiteship");
		user.setName("가나다");
		user.setPassword("pw1234");
		
		dao.add(user);
		System.out.println(user.getId() + "등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		System.out.println(user2.getId() + "조회 성공");
		
	}
}

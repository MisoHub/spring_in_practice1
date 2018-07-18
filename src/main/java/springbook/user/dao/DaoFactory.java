package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

	@Bean
	public UserDao userDao() {
		//1.7.5 메소드를 이용한 의존관계주입 
		UserDao userDao = new UserDao();
		userDao.setUserDaoConnectionMaker(connectionMaker());
		return userDao;
	}

	@Bean
	public AdminDao adminDao() {
		return new AdminDao(connectionMaker());
	}

	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}

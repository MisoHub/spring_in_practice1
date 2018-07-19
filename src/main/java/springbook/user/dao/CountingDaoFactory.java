package springbook.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class CountingDaoFactory {
	

	@Bean
	public UserDao userDao() {
		//1.7.5 메소드를 이용한 의존관계주입 
		UserDao userDao = new UserDao();
//		userDao.setUserDaoConnectionMaker(connectionMaker());
		userDao.setDataSource(dataSource());
//		this.dataSource.getConnection();
		return userDao;
	}

	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/springbook");
		dataSource.setUsername("spring");
		dataSource.setPassword("book");
		
		return dataSource;
	}
//	
//	
//	@Bean
//	public AdminDao adminDao() {
//		return new AdminDao(connectionMaker());
//	}
//	
//	@Bean
//	public ConnectionMaker connectionMaker() {
//		return new CountingConnectionMaker(realConnectionMaker());
//	}
//	
//	@Bean
//	public ConnectionMaker realConnectionMaker() {
//		return new DConnectionMaker();
//	}
}


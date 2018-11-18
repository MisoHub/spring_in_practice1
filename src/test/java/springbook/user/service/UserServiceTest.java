package springbook.user.service;

import org.junit.Before;
import org.junit.Test;

import org.hamcrest.Matchers;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.model.Level;
import springbook.user.model.User;
import springbook.user.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/tobiAppContext.xml")

public class UserServiceTest {

	@Autowired
	ApplicationContext context; 
	
	@Autowired
	UserDao userDao;

	@Autowired
	UserService userService;

	@Autowired
	UserService testUserService;
	
	@Autowired 
	DataSource dataSource;
	
	List<User> users;


	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("agregory", "그레고리포터", "password", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
				new User("bdesmond", "폴데스몬드", "password", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER, 10),
				new User("cjessy", "제시존스", "password", Level.SILVER, 60, UserServiceImpl.MIN_RECOMMEND_FOR_GOLD - 1),
				new User("dartpaper", "아트페퍼", "password", Level.SILVER, 100, UserServiceImpl.MIN_RECOMMEND_FOR_GOLD),
				new User("echalie", "찰리파커", "password", Level.GOLD, 160, 129));
	}

	@Test
	public void bean() {
		assertThat(this.userService, Matchers.is(Matchers.notNullValue()));
	}

	@Test
	public void upgradeLevels() {
		userDao.deleteAll();
		for (User user : users) {
			userDao.add(user);
		}

		try {
			userService.upgradeLevels();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	}

	@Test
	public void add() {
		userDao.deleteAll();

		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);

		userService.add(userWithLevel);
		userService.add(userWithoutLevel);

		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

		assertThat(userDao.get(userWithLevelRead.getId()).getLevel(), Matchers.is(userWithLevel.getLevel()));
		assertThat(userDao.get(userWithoutLevelRead.getId()).getLevel(), Matchers.is(Level.BASIC));

	}

	@Test
	@DirtiesContext	 // 컨텍스트 무효화 애노테이션 
	public void upgradeAllOrNothing() throws Throwable {

		
		// upgradeAllOrNothing 메소드는 다른 테스트 다른 UserUpdatePolicy 를 사용함으로 
		// 해당 policy로 설정된 userService를 별도의 target으로 지정한다.
		// 그래서 직접 proxyFactoryBean을 가져오는 것이다. 
		ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService",
				ProxyFactoryBean.class);
		txProxyFactoryBean.setTarget(testUserService);
		UserService txUserService = (UserService)txProxyFactoryBean.getObject();
		
		// 초기화 후 추가
		userDao.deleteAll();
		for (User user : users)
			userDao.add(user);

		try {
			txUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (RuntimeException e) {
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkLevelUpgraded(users.get(1), false);
	}
	

	private void checkLevelUpgraded(User user, boolean upgraded) {

		User userUpdate = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdate.getLevel(), Matchers.is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpdate.getLevel(), Matchers.is(user.getLevel()));
		}
	}
	


}

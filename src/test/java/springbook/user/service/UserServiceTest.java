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
import org.springframework.beans.factory.annotation.Autowired;
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
	UserDao userDao;

	@Autowired
	UserService testUserService;
	
	@Autowired 
	DataSource dataSource;
	
	@Autowired
	PlatformTransactionManager txManager;
	
	List<User> users;

	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("agregory", "그레고리포터", "password", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
				new User("bdesmond", "폴데스몬드", "password", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER, 10),
				new User("cjessy", "제시존스", "password", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD - 1),
				new User("dartpaper", "아트페퍼", "password", Level.SILVER, 100, UserService.MIN_RECOMMEND_FOR_GOLD),
				new User("echalie", "찰리파커", "password", Level.GOLD, 160, 129));
	}

	@Test
	public void bean() {
		assertThat(this.testUserService, Matchers.is(Matchers.notNullValue()));
	}

	@Test
	public void upgradeLevels() {
		userDao.deleteAll();
		for (User user : users) {
			userDao.add(user);
		}

		try {
			testUserService.upgradeLevels();
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

		testUserService.add(userWithLevel);
		testUserService.add(userWithoutLevel);

		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

		assertThat(userDao.get(userWithLevelRead.getId()).getLevel(), Matchers.is(userWithLevel.getLevel()));
		assertThat(userDao.get(userWithoutLevelRead.getId()).getLevel(), Matchers.is(Level.BASIC));

	}

	@Test
	public void upgradeAllOrNothing() {

		// Transaction Hander 생성
		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(testUserService);
		txHandler.setTransactionManager(txManager);
		txHandler.setPattern("upgradeLevels");
		
		//UserService 타입의 다이나믹 프록시 생성.. 
		UserService txUserService = (UserService)Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] {UserService.class}, 
				txHandler);

		userDao.deleteAll();
		for (User user : users)
			userDao.add(user);

		try {
			txUserService.upgradeLevels();
			fail("TestUserSErviceException expected");
		} catch (TestUserServiceException e) {
			
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

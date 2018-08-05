package springbook.user.service;

import org.junit.Before;
import org.junit.Test;

import org.hamcrest.Matchers;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.Level;
import springbook.user.dao.User;
import springbook.user.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/tobiAppContext.xml")

public class UserServiceTest {

	@Autowired
	UserDao userDao;

	@Autowired
	UserService userService;

	@Autowired
	UserLevelUpgradePolicy userLevelUpgradePolicy;

	List<User> users;

	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("agregory", "그레고리", "password", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
				new User("bdesmond", "데스몬드", "password", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER, 10),
				new User("cjessy", "제시", "password", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD - 1),
				new User("dartpaper", "아트페퍼", "password", Level.SILVER, 100, UserService.MIN_RECOMMEND_FOR_GOLD),
				new User("echalie", "찰리", "password", Level.GOLD, 160, 129));
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

		userService.upgradeLevels();

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

		assertThat(userDao.get(userWithLevel.getId()).getLevel(), Matchers.is(userWithLevel.getLevel()));
		assertThat(userDao.get(userWithoutLevel.getId()).getLevel(), Matchers.is(Level.BASIC));

	}

//	// 명시적으로 다음 level 을 입력하게 되어 있어, 테스트에 바람직하지 않다.
//	private void checkLevel(User user, Level expLevel) {
//		User userUpdate = userDao.get(user.getId());
//		assertThat(userUpdate.getLevel(), Matchers.is(expLevel));
//	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdate.getLevel(), Matchers.is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpdate.getLevel(), Matchers.is(user.getLevel()));
		}
	}

}

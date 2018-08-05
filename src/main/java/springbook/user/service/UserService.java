package springbook.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import springbook.user.dao.Level;
import springbook.user.dao.User;
import springbook.user.dao.UserDao;


public class UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	private UserDao userDao;
	private UserLevelUpgradePolicy userLevelUpgradePolicy; 
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public void setUserLevelUpgradePolicy (UserLevelUpgradePolicy ulup) {
		this.userLevelUpgradePolicy = ulup;
	}

	// re-factoried function
	public void upgradeLevels() {
		List<User> users = userDao.getAll();

		for (User user : users) {
			if (userLevelUpgradePolicy.canUpgradeLevel(user))
				userLevelUpgradePolicy.upgradeLevel(user);
		}
	}

	public void add(User user) {
		if (user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}
}

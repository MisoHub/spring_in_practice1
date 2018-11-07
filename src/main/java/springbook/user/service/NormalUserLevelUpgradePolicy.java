package springbook.user.service;

import springbook.user.model.Level;
import springbook.user.model.User;
import springbook.user.dao.UserDao;

public class NormalUserLevelUpgradePolicy implements UserLevelUpgradePolicy {

	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public boolean canUpgradeLevel(User user) {
		// TODO Auto-generated method stub
		Level currentLevel = user.getLevel();
		switch (currentLevel) {
		case BASIC:
			return (user.getLogin() >= 50);
		case SILVER:
			return (user.getRecommend() >= 30);
		case GOLD:
			return false;
		default:
			throw new IllegalArgumentException("Unknow Level: " + currentLevel);
		}
	}

	@Override
	public void upgradeLevel(User user) {
		// TODO Auto-generated method stub
		user.upgradeLevel();
		userDao.update(user);
	}
}

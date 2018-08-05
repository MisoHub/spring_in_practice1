package springbook.user.service;

import springbook.user.dao.User;

public interface UserLevelUpgradePolicy {
	boolean canUpgradeLevel(User user);
	void upgradeLevel(User user);

}

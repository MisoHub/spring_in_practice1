package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.model.User;

public class TestNormalUserLevelUpgradePolicy implements UserLevelUpgradePolicy {

	// decoration pattern 적용 
	private UserLevelUpgradePolicy superPolicy;
	private String id;
	

	public void setSuperPolicy(UserLevelUpgradePolicy superPolicy) {
		this.superPolicy = superPolicy;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public void upgradeLevel(User user) {
		// TODO Auto-generated method stub
		if(user.getId().equals(this.id))
			throw new TestUserServiceException();
			
		superPolicy.upgradeLevel(user);
	}

	@Override
	public boolean canUpgradeLevel(User user) {
		// TODO Auto-generated method stub
		return superPolicy.canUpgradeLevel(user);
	}

}

package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.model.User;

public class TestNormalUserLevelUpgradePolicy extends NormalUserLevelUpgradePolicy {

	private String id;
	
	public TestNormalUserLevelUpgradePolicy(String id) {
		this.id = id;
	}
	
	@Override
	public void upgradeLevel(User user) {
		// TODO Auto-generated method stub
		if(user.getId().equals(this.id))
			try {
				throw new TestUserServiceException();
			} catch (TestUserServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		super.upgradeLevel(user);
	}

}

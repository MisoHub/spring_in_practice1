package springbook.user.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import springbook.user.model.Level;
import springbook.user.model.User;
import springbook.user.dao.UserDao;

public class UserServiceImpl implements UserService{

	protected UserDao userDao;
	protected DataSource dataSource;
	protected UserLevelUpgradePolicy userLevelUpgradePolicy;
	protected PlatformTransactionManager txManager;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy ulup) {
		this.userLevelUpgradePolicy = ulup;
	}
	
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	// re-factoried function
	@Override
	public void upgradeLevels() throws SQLException {
				// DB 커넥션 생성 하고 트랜잭션 시작(auto commit끔)
		// DataSourceUtils 을 사용해서 connection을 가져오면 TransactionSynchronization 에 등록됨 
		
			List<User> users = userDao.getAll();
			for (User user : users) {
				if (userLevelUpgradePolicy.canUpgradeLevel(user))
					userLevelUpgradePolicy.upgradeLevel(user);
			}
	}
	
	@Override
	public void add(User user) {
		if (user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}
}

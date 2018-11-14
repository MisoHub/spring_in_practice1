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

public class UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

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
	public void upgradeLevels() throws SQLException {
		
		// 작업동기화 초기화 
		TransactionSynchronizationManager.initSynchronization();

		// DB 커넥션 생성 하고 트랜잭션 시작(auto commit끔)
		// DataSourceUtils 을 사용해서 connection을 가져오면 TransactionSynchronization 에 등록됨 
		Connection c = DataSourceUtils.getConnection(dataSource);
		c.setAutoCommit(false);
		
		try {
			
			List<User> users = userDao.getAll();
			for (User user : users) {
				if (userLevelUpgradePolicy.canUpgradeLevel(user))
					userLevelUpgradePolicy.upgradeLevel(user);
		}
			c.commit();
		}catch(Exception e) {
			// 예외 발생 시 트랜잭션 롤백 
			c.rollback();
			throw e;
		}finally {

			// 스프링 유틸리티 메소드를 이용해 DB커넥션을 안전하게 닫는다. 
			DataSourceUtils.releaseConnection(c, dataSource);

			// 동기화 작업 종료 및 정리 
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}
	}

	public void add(User user) {
		if (user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}
}

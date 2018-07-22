package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

public class UserDao {

	// private ConnectionMaker userDaoConnectionMaker = null;
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// //1.7.5 메소드를 이용한 의존관계주입
	// public void setUserDaoConnectionMaker(ConnectionMaker cm) {
	// this.userDaoConnectionMaker = cm;
	// }

	public void add(User user) throws SQLException, ClassNotFoundException {

		// Connection c = this.userDaoConnectionMaker.makeConnection();
		Connection c = this.dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());

		ps.executeUpdate();

		ps.close();
		c.close();

	}

	public User get(String id) throws ClassNotFoundException, SQLException {

		// Connection c = this.userDaoConnectionMaker.makeConnection();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;
		// try ~ catch 구문으로 예외처리를 한다.
		try {
			c = this.dataSource.getConnection();
			ps = c.prepareStatement("select * from users where id=?");
			ps.setString(1, id);

			rs = ps.executeQuery();
			user = null;

			if (rs.next()) {
				user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
			}

		} catch (SQLException e) {
			throw e;
		} finally {

			if (rs != null) {
				// close 구문에도 예외가 발생함으로 2중으로 try catch를 수행해야 한다.
				try {
					rs.close();
				} catch (SQLException e) {
					throw e;
				}
			}
		}
		if (c != null) {
			try {
				c.close();
			} catch (SQLException e) {
				throw e;
			}
		}

		// 예외테스트 적용 (TDD)
		if (user == null)
			throw new EmptyResultDataAccessException(1);

		return user;
	}

	public void deleteAll() throws SQLException {

		Connection c = null;
		PreparedStatement ps = null;

		try {
			c = this.dataSource.getConnection();
			ps = c.prepareStatement("delete from users");

			ps.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {

			if (ps != null) {
				try {
					// close 구문에도 예외가 발생함으로 2중으로 try catch를 수행해야 한다.
					ps.close();
				} catch (SQLException e) {
					throw e;
				} finally {
					ps.close();
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					throw e;
				} finally {
					c.close();
				}
			}

		}

	}

	public int getCount() throws ClassNotFoundException, SQLException {

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count;
		try {

			c = this.dataSource.getConnection();
			ps = c.prepareStatement("select count(*) from users");
			rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} catch (SQLException e) {
			throw e;
		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw e;
				}

			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw e;
				}

			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					throw e;
					// 예외처리 마지막... 반복적인 예외처리에 많은 effort가 든다... 
				}
			}
		}

		return count;
	}
}

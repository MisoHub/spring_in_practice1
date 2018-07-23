package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.statement.AddStatement;
import springbook.user.statement.DeleteAllStatement;
import springbook.user.statement.GetStatement;
import springbook.user.statement.StatementStrategy;

public class UserDao {

	// private ConnectionMaker userDaoConnectionMaker = null;
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// inner class 에서 변수를 사용할 수 있도록 final로 전달 
	public void add(final User user) throws SQLException, ClassNotFoundException {

		// 익명클래스로 선언하여 코드 효율화를 시킴
		StatementStrategy st = new StatementStrategy() {
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				// TODO Auto-generated method stub

				PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());

				return ps;
			}
		};
		jdbcContextWithStatementStrategy(st);

	}

	public User get(String id) throws ClassNotFoundException, SQLException {

		// Connection c = this.userDaoConnectionMaker.makeConnection();
		
		
		StatementStrategy st = new GetStatement(id);
		jdbcContextWithStatementStrategy(st);
		
		// 위 방법을 사용해서는 user 를 return 받기 어렵다. 
		return null;

	}

	public void deleteAll() throws SQLException {

		StatementStrategy st = new StatementStrategy() {
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				// TODO Auto-generated method stub
				PreparedStatement ps = c.prepareStatement("delete from users");
				return ps;
			}
		};
		jdbcContextWithStatementStrategy(st);
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

	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {

		Connection c = null;
		PreparedStatement ps = null;

		try {
			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			ps.executeQuery();

		} catch (SQLException e) {
			throw e;

		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
	}
}

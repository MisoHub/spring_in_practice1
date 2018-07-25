package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.connection.JdbcContext;
import springbook.user.statement.AddStatement;
import springbook.user.statement.DeleteAllStatement;
import springbook.user.statement.GetStatement;
import springbook.user.statement.StatementStrategy;

public class UserDao {

	// private ConnectionMaker userDaoConnectionMaker = null;

	private JdbcContext jdbcContext;
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}

	// inner class 에서 변수를 사용할 수 있도록 final로 전달
	public void add(final User user) throws SQLException, ClassNotFoundException {

		// 익명클래스로 선언하여 코드 효율화를 시킴
		this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				// TODO Auto-generated method stub

				PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?,?,?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());

				return ps;
			}
		});

	}

	public User get(String id) throws ClassNotFoundException, SQLException {

//20180722 원복함 
// 메소드 추출 하지 못함. return 값을 받아야 하는 경우.
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

		this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				// TODO Auto-generated method stub
				PreparedStatement ps = c.prepareStatement("delete from users");
				return ps;
			}
		});
	}

	public int getCount() throws ClassNotFoundException, SQLException {
// 메소드 추출 하지 못함. return 값을 받아야 하는 경우. 
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

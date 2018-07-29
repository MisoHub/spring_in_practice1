package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.connection.JdbcContext;
import springbook.user.statement.AddStatement;
import springbook.user.statement.DeleteAllStatement;
import springbook.user.statement.GetStatement;
import springbook.user.statement.StatementStrategy;

public class UserDao {

	// private JdbcContext jdbcContext;
	// private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	// public void setJdbcContext(JdbcContext jdbcContext) {
	// this.jdbcContext = jdbcContext;
	// }

	// inner class 에서 변수를 사용할 수 있도록 final로 전달
	public void add(final User user) throws SQLException, ClassNotFoundException {

		this.jdbcTemplate.update("insert into users(id, name,password) values(?,?,?)", user.getId(), user.getName(),
				user.getPassword());

	}

	public User get(String id) throws ClassNotFoundException, SQLException {

		// return this.jdbcTemplate.query(new PreparedStatementCreator() {
		//
		// @Override
		// public PreparedStatement createPreparedStatement(Connection arg0) throws
		// SQLException {
		// // TODO Auto-generated method stub
		// return arg0.prepareStatement("select * from users where id=\'"+id+"\'");
		// }
		//
		// }, new ResultSetExtractor<User>() {
		//
		// @Override
		// public User extractData(ResultSet arg0) throws SQLException,
		// DataAccessException {
		// // TODO Auto-generated method stub
		// arg0.next();
		// return new
		// User(arg0.getString("id"),arg0.getString("name"),arg0.getString("password"));
		// }
		// });
		//
		//
		// return this.jdbcTemplate.queryForObject("select * from users where id=?", new
		// Object[] { id },User.class);
		// 위와 같이 설정하면 오류남. 결과로 3개의 필드값(String)이 return 되는데, 각각이 User 인스턴스에 대응하여 리턴됨.

		return this.jdbcTemplate.queryForObject("select * from users where id=?", new Object[] { id },
				new RowMapper<User>() {

					@Override
					public User mapRow(ResultSet rs, int rowNum) throws SQLException {
						// TODO Auto-generated method stub
						return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
					}
				});

	}

	public void deleteAll() throws SQLException {

		// this.jdbcTemplate.update(new PreparedStatementCreator() {
		//
		// @Override
		// public PreparedStatement createPreparedStatement(Connection arg0) throws
		// SQLException {
		// // TODO Auto-generated method stub
		// return arg0.prepareStatement("delete from users");
		// }
		// });

		// 내부적인 callback 을 사용한다.
		this.jdbcTemplate.update("delete from users");

	}

	public int getCount() throws ClassNotFoundException, SQLException {

		// return this.jdbcTemplate.query(new PreparedStatementCreator() {
		//
		// @Override
		// public PreparedStatement createPreparedStatement(Connection arg0) throws
		// SQLException {
		// // TODO Auto-generated method stub
		// return arg0.prepareStatement("select count(*) from users");
		// }
		//
		// }, new ResultSetExtractor<Integer>() {
		//
		// @Override
		// public Integer extractData(ResultSet arg0) throws SQLException,
		// DataAccessException {
		// // TODO Auto-generated method stub
		// arg0.next();
		// return arg0.getInt(1);
		//
		// }
		// });
		// queryForObject 는 dedicated 됨.
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}

	// 그냥 jdbcTemplate.qeury 를 수행한다. (queryForObject 가 아님) 
	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id", new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowN) throws SQLException {
				// TODO Auto-generated method stub
				return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
			}
		});
		// mapRow 에서 return 한 User는 List 형태로 jdbcTemplate에 의해서 return된다.
	}
}

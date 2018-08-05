package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoJdbc implements UserDao {

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	// inner class 에서 변수를 사용할 수 있도록 final로 전달
	public void add(final User user) {

		this.jdbcTemplate.update("insert into users(id, name,password) values(?,?,?)", user.getId(), user.getName(),
				user.getPassword());

	}

	public User get(String id) {

		return this.jdbcTemplate.queryForObject("select * from users where id=?", new Object[] { id }, this.userMapper);

	}

	public void deleteAll() {
		// 내부적인 callback 을 사용한다.
		this.jdbcTemplate.update("delete from users");

	}

	public int getCount() {
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}

	// 그냥 jdbcTemplate.qeury 를 수행한다. (queryForObject 가 아님)
	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
		// mapRow 에서 return 한 User는 List 형태로 jdbcTemplate에 의해서 return된다.
	}

	private RowMapper<User> userMapper = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet rs, int arg1) throws SQLException {
			// TODO Auto-generated method stub
			return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
		}
	};
}

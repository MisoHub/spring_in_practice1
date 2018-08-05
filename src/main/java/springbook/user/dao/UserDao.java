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

public interface UserDao {

	public void add(final User user);
	public User get(String id);
	public void deleteAll();
	public int getCount();
	public List<User> getAll();
	public void update(User user1);
	
	
}

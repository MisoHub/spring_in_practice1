package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

public class UserDao {

//	private ConnectionMaker userDaoConnectionMaker = null;
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	

//	//1.7.5 메소드를 이용한 의존관계주입 
//	public void setUserDaoConnectionMaker(ConnectionMaker cm) {
//		this.userDaoConnectionMaker = cm;
//	}
	
	public void add(User user) throws SQLException, ClassNotFoundException {

//		Connection c = this.userDaoConnectionMaker.makeConnection();
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

//		Connection c = this.userDaoConnectionMaker.makeConnection();
		Connection c = this.dataSource.getConnection();
		PreparedStatement ps = c.prepareStatement("select * from users where id=?");
		ps.setString(1, id);

		ResultSet rs = ps.executeQuery();
		rs.next();

		User user = new User();

		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));

		rs.close();
		ps.close();
		c.close();

		return user;
	}


}

package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import springbook.user.connection.ConnectionMaker;

public class AdminDao {

	private ConnectionMaker connectionMaker = null;

	public AdminDao(ConnectionMaker c) {
		this.connectionMaker = c;

	}

	public User get(String id) throws ClassNotFoundException, SQLException {

		Connection c = this.connectionMaker.makeConnection();
		PreparedStatement ps = c.prepareStatement("select * from users where id=?");
		ps.setString(1, id);

		ResultSet rs = ps.executeQuery();
		rs.next();

		User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));

		rs.close();
		ps.close();
		c.close();

		return user;
	}

}

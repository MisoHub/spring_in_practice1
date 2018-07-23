package springbook.user.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author 78582
 * 
 * Template pattern으로 각 회사/환경마다 별도의 connection을 생성하여 배포할 수 있음 
 *
 */

public class NConnectionMaker implements ConnectionMaker {
	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub

		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost/spring", "root", "!root123$");
		return c;
	}

}

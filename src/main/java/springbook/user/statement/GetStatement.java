package springbook.user.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetStatement implements StatementStrategy {

	private String id;
	public GetStatement(String id) {
		this.id = id;
	}
	
	@Override
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = c.prepareStatement("select * from users where id=?");
		ps.setString(1, id);

		return ps;
	}
	

}

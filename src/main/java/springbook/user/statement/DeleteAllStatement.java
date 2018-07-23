package springbook.user.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy {

	@Override
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ps = c.prepareStatement("delete from users");
		return ps;
	}

}

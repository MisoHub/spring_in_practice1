package springbook.user.connection;

import java.sql.Connection;
import java.sql.SQLException;

// 1.7.4 DI응용 : 부가기능 추가 - 생성된 dao의 count를 기록하고 return함.  
public class CountingConnectionMaker implements ConnectionMaker {

	private int counter = 0;
	
	private ConnectionMaker realConnectionMaker;
	
	public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
		this.realConnectionMaker = realConnectionMaker;
	}

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		this.counter++;
		return realConnectionMaker.makeConnection();
	}
	
	public int getCounter() {
		return this.counter;
	}
}

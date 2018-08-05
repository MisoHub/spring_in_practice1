package springbook.user.dao;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/tobiAppContext.xml")
public class UserDaoTest {

	// 테스트 오브젝트가 만들어지고 나면 스프링 테스트 컨텍스트에 의해 자동으로 값이 주입된다.
	@Autowired
	private ApplicationContext context;

	@Autowired
	private UserDao dao;
	
	@Autowired
	private DataSource dataSource;

	private User[] users = null;

	@Before
	public void setup() {
		this.users = new User[] { new User("agregory", "그레고리", "password", Level.BASIC, 1, 0), new User("bdesmond", "데스몬드", "password",Level.SILVER, 55, 10),
				new User("cjessy", "제시", "password",Level.BASIC, 5, 1), new User("dartpaper", "아트페퍼", "password",Level.GOLD, 100, 40) };
		
	}

	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {

		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		User user = new User("whiteship", "가나다", "pw1234",Level.BASIC, 1, 0);

		dao.add(user);
		assertThat(dao.getCount(), is(1));

		User user2 = dao.get(user.getId());

		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
	}

	// 포괄테스트
	@Test
	public void count() throws ClassNotFoundException, SQLException {

		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		for (int i = 0; i < this.users.length; i++) {
			dao.add(this.users[i]);
			assertThat(dao.getCount(), is(i + 1));
		}

	}

	// 예외 발생 테스트, expected 추가 시 보통 테스트와 반대로 정상적으로 테스트 메소드를 마치면 실패, expected 예외가 던져지면
	// 테스트 성공.
	@Test(expected = org.springframework.dao.DataAccessException.class)
	public void getUserFailure() throws ClassNotFoundException, SQLException {

		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		dao.get("unknown_id");
	}

	@Test
	public void getAll() throws SQLException, ClassNotFoundException {
		dao.deleteAll();

		List<User> uList = dao.getAll();
		assertThat(uList.size(), is(0));

		dao.add(users[0]);
		uList = dao.getAll();
		assertThat(uList.size(), is(1));
		checkSameUser(users[0], uList.get(0));

		dao.add(users[1]);
		uList = dao.getAll();
		assertThat(uList.size(), is(2));
		checkSameUser(users[0], uList.get(0));
		checkSameUser(users[1], uList.get(1));

		dao.add(users[2]);
		uList = dao.getAll();
		assertThat(uList.size(), is(3));
		checkSameUser(users[0], uList.get(0));
		checkSameUser(users[1], uList.get(1));
		checkSameUser(users[2], uList.get(2));

	}

	@Test(expected = DataAccessException.class)
	public void duplicateKey() {
		dao.deleteAll();
		dao.add(users[0]);
		dao.add(users[0]);
	}
	
	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		try {
			dao.add(users[0]);
			dao.add(users[0]);
		}catch(DuplicateKeyException ex) {
			SQLException sqlEx = (SQLException)ex.getRootCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
			assertThat((DuplicateKeyException)set.translate(null,null,sqlEx),is(DuplicateKeyException.class));
		}
	}
	
	
	@Test
	public void update() {
		dao.deleteAll();
		
		User user = new User(users[0]);

		dao.add(user);
		dao.add(users[2]);
		
		user.setName(users[1].getName());
		user.setPassword(users[1].getPassword());
		user.setLevel(users[1].getLevel());
		user.setLogin(users[1].getLogin());
		user.setRecommend(users[1].getRecommend());
		
		dao.update(user);
		
		checkSameUser(user,dao.get(user.getId()));
		checkSameUser(users[2],dao.get(users[2].getId()));
	}

	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
		
	}

}

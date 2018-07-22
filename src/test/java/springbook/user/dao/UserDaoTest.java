package springbook.user.dao;

import java.sql.SQLException;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.springframework.dao.EmptyResultDataAccessException;

import org.junit.Before;
import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDaoTest {

	private UserDao dao = null;
	private User[] users = null;

	@Before
	public void setup() {
		ApplicationContext context = new ClassPathXmlApplicationContext("tobiAppContext.xml");
		this.dao = context.getBean("userDao", UserDao.class);
		
		users = new User[] { new User("gregory", "그레고리", "password"), new User("desmond", "데스몬드", "password"),
				new User("jessy", "제시", "password"), new User("artpaper", "아트페퍼", "password") };

	}

	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {

		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		User user = new User("whiteship", "가나다", "pw1234");

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

	// 예외 발생 테스트, expected 추가 시 보통 테스트와 반대로 정상적으로 테스트 메소드를 마치면 실패, expected 예외가 던져지면 테스트 성공. 
	@Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
	public void getUserFailure() throws ClassNotFoundException, SQLException {

		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.get("unknown_id");
	}
	
	
}

package springbook.user.dao;

import java.sql.SQLException;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/tobiAppContext.xml")
public class UserDaoTest {

	// 테스트 오브젝트가 만들어지고 나면 스프링 테스트 컨텍스트에 의해 자동으로 값이 주입된다. 
	@Autowired
	private ApplicationContext context=null;
	
	@Autowired
	private UserDao dao = null;
	private User[] users = null;

	@Before
	public void setup() {
		// setup에서 객체를 생성하면 매 테스트 마다 실행되어 여러개의 context가 생성된다. 
//		ApplicationContext context = new ClassPathXmlApplicationContext("tobiAppContext.xml");
//		this.dao = this.context.getBean("userDao", UserDao.class);
		
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

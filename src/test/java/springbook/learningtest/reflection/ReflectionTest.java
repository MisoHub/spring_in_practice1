package springbook.learningtest.reflection;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

public class ReflectionTest {
	
	@Test
	public void invokeMethod() throws Exception {
		
		String name = "Spring";
		assertThat(name.length(), is(6));
		
		// length()
		Method lengthMethod = String.class.getMethod("length");
		assertThat((Integer)lengthMethod.invoke(name), is(6));

		
		// charAt()
		assertThat(name.charAt(0), is('S'));
		
		Method charAtMethod = String.class.getMethod("charAt", int.class);
		assertThat((Character)charAtMethod.invoke(name,0), is('S'));
		
		
		
	}

}

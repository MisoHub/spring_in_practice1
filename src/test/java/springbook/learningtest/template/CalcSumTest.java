package springbook.learningtest.template;

import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {

	Calculator calculator = null;
	String filePath = null;

	@Before
	public void setup() throws IOException {
		calculator = new Calculator();
		filePath = getClass().getResource("/numbers.txt").getPath();
	}

	@Test
	public void sumOfNumbers() throws IOException {

		assertThat(calculator.calcSum(filePath), is(10));
	}

	@Test
	public void mulOfNumbers() throws IOException {

		assertThat(calculator.calcMul(filePath), is(24));
	}

}

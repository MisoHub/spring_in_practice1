package springbook.learningtest.dynamicproxy;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Proxy;

import org.junit.Test;

import springbook.ch6.dynamicproxy.hello.Hello;
import springbook.ch6.dynamicproxy.hello.HelloTarget;
import springbook.ch6.dynamicproxy.hello.HelloUppercase;
import springbook.ch6.dynamicproxy.hello.UppercaseHandler;

import static org.hamcrest.CoreMatchers.is;

public class DynamicProxyTest {

	@Test 
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Toby"), is("Hello Toby"));
		assertThat(hello.sayHi("Toby"), is("Hi Toby"));
		assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
	}
	
	@Test
	public void badUppercaseProxy() {
		Hello proxiedHello = new HelloUppercase(new HelloTarget());
		
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
		
	}
	
	@Test
	public void goodUppercaseProxy() {
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] {Hello.class},
				new UppercaseHandler(new HelloTarget()
						));
	}
	
}

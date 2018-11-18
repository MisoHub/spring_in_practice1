package springbook.learningtest.dynamicproxy;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

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
		assertThat(hello.sayThankYou("Toby"), is("Thank you Toby"));
	}

	@Test
	public void badUppercaseProxy() {
		Hello proxiedHello = new HelloUppercase(new HelloTarget());

		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}

	@Test
	public void goodUppercaseProxy() {
		Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Hello.class },
				new UppercaseHandler(new HelloTarget()));
	}

	@Test
	public void proxyFactoryBean() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		pfBean.addAdvice(new UppercaseAdvice());

		Hello proxiedHello = (Hello) pfBean.getObject();

		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));

	}

	@Test
	public void pointcutAdvisor() {
		// 프록시 팩토리 빈 생성 및 타겟 지정
		// 
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		// 포인트컷을 이용하여 메소드 필터링 수행 
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedName("sayH*");

		// 포인트컷 어드바이서를 팩토리 빈에 설정
		// Advisor = Pointcut + Advice 로 구성되어있다. 
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

		// 오브젝트 가져와서 테스트 수행 
		Hello proxiedHello = (Hello) pfBean.getObject();
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("Thank you Toby"));

	}

	static class UppercaseAdvice implements MethodInterceptor {

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			// 타겟지정 없이 콜백방식으로 메소드 수행 
			String ret = (String)invocation.proceed();
			return ret.toUpperCase();
		}

	}

}

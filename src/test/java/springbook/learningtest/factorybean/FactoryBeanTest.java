package springbook.learningtest.factorybean;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.ch6.dynamicproxy.hello.HelloTarget;
import springbook.user.model.Message;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="file:**/FactoryBeanTest-context.xml")
// default 경로 : ../springbook/learningtest/factorybean/FactoryBeanTest-context.xml

public class FactoryBeanTest {

	@Autowired
	ApplicationContext context;
	
	@Test
	public void getMessgaeFromFactoryBean() {
		Object message = context.getBean("message");
		
//		에러. The method assertThat(T, Matcher<? super T>) in the type Assert is not applicable for the arguments
//		assertThat(message.getClass(), is(Message.class));
		
		assertThat(message.getClass().getTypeName(), is(Message.class.getTypeName()));
		assertThat(((Message)message).getText(), is("Factory Bean"));
	}
	
}

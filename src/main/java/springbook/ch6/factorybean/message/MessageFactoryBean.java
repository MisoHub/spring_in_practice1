package springbook.ch6.factorybean.message;

import org.springframework.beans.factory.FactoryBean;

import springbook.user.model.Message;

public class MessageFactoryBean implements FactoryBean<Message> {

	String text;
	
	// 빈프로퍼티로 대신 DI 받는다. 
	public void setText(String text) {
		this.text = text;
	}
	
	/*
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 * 빈을 이용하여 실제 사용될 오브젝트 생성
	 */
	@Override
	public Message getObject() throws Exception {
		// TODO Auto-generated method stub
		return Message.newMessage(this.text);
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return Message.class;
	}
	
	/*
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 * 팩토리 빈은 매번 요청할 때마다 새로운 오브젝트를 만들기 때문에 false로 설정 
	 * 만들어진 빈 오브젝트는 싱글톤으로 스프링이 관리해줄 수 있다. 
	 */
	public boolean isSingleton() {
		return false;
	}
}

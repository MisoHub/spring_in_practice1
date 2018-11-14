package springbook.ch6.dynamicproxy.hello;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler{

	Object target;
	
	public UppercaseHandler(Object target) {
		this.target = target; 
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		Object ret = method.invoke(target,  args); // 타겟으로 위임. 인터페이스 메소드 호출에 모두 적용됨.
									
		if(ret instanceof String && method.getName().startsWith("say")) {
			// 부가기능 제공 - 타입에 따라 연산하여 리턴. 
			return ((String)ret).toUpperCase();
		}else
			return ret;
	}

}

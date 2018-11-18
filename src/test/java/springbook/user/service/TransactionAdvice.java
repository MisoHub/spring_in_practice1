package springbook.user.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

public class TransactionAdvice implements MethodInterceptor {

	// 부가기능(트랜잭션 관리)을 위한 변수 
	PlatformTransactionManager transactionManager;
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// TODO Auto-generated method stub

		// 트랜잭션 매니저로부터 트랜잭션 상태 값을 받음
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			// 메소드 수행 후 커밋 
			Object ret = invocation.proceed();
			this.transactionManager.commit(status);
			
		} catch (RuntimeException e) {
			// 스프링의 MethodInvocation의 타겟호출은 예외가 포장되지 않고 그대로 전달됨. 
			this.transactionManager.rollback(status);
			throw e;
		}

		return null;
	}

}

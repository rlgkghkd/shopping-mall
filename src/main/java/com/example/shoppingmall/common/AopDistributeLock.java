package com.example.shoppingmall.common;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AopDistributeLock {
	private final RedissonClient redissonClient;
	private final PlatformTransactionManager transactionManager;

	@Around("@annotation(distributedLock)")
	public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
		String key = distributedLock.key();
		long waitingTime = distributedLock.waitingTime();
		long rentingTime = distributedLock.rentingTime();

		RLock lock = redissonClient.getLock(key);

		boolean isLocked = false;
		try {
			isLocked = lock.tryLock(waitingTime, rentingTime, TimeUnit.SECONDS);
			if (!isLocked) {
				throw new IllegalStateException(key + " 잠겨있음");
			}

			log.info("락 획득: {}", key);
			TransactionTemplate template = new TransactionTemplate(transactionManager);
			template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

			return template.execute(status -> {
				try {
					return joinPoint.proceed();
				} catch (Throwable throwable) {
					status.setRollbackOnly();
					throw new RuntimeException(throwable);
				}
			});
		} finally {
			if (isLocked && lock.isHeldByCurrentThread()) {
				lock.unlock();
				log.info("{} 반환", key);
			}
		}
	}
}

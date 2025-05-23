package com.example.shoppingmall.common;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
	/*
	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
	}

	 */

	// ...
	// 세 케이스에서 모두 다른 형태의 bean 을 사용하여 redis 를 만들 수 있기 때문에 그냥 원시적인 형태로 때려박음
	// 다만 실제 프로젝트에서 redis client 가 여럿 필요한 경우
	// 하나의 bean 에서 여러 client 를 만들어 list 형태로 보관한 뒤
	// 해당 bean 가져와서 bean 의  list 에서 원하는 redis client 를 꺼내 사용하는 방식으로 쓸 것을 추천함
	// 만약 지금처럼 다른 형태의 bean 이 아니라 같은 bean 여러개 만들면 종속성 문제 발생함.
	// 지금은 그냥 귀찮아서 이렇게 함.
	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	private static final String REDISSON_PREFIX = "redis://";

	// 블랙리스트용 0번 redis
	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(host);
		config.setPort(port);
		config.setDatabase(0);

		LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
		factory.afterPropertiesSet();

		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		template.afterPropertiesSet();
		return template;
	}

	// 캐시용 2번 redis
	@Bean
	public RedisConnectionFactory cacheRedisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(host);
		config.setPort(port);
		config.setDatabase(2); // 캐시용 Redis DB 번호 지정

		return new LettuceConnectionFactory(config);
	}

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory cacheRedisConnectionFactory) {
		return RedisCacheManager.builder(cacheRedisConnectionFactory).build();
	}

	// 분산락용 1번 redis
	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer()
			.setAddress(REDISSON_PREFIX + host + ":" + port)
			.setDatabase(1);

		return Redisson.create(config);
	}
}

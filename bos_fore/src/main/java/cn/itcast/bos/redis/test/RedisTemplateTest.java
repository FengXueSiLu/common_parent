package cn.itcast.bos.redis.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class RedisTemplateTest {
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	@Test
	public void testRedis(){
		// 保存key value
		// 设置20秒失效
		redisTemplate.opsForValue().set("city", "西安", 20, TimeUnit.SECONDS);
		System.out.println(redisTemplate.opsForValue().get("city"));
	}
	
}

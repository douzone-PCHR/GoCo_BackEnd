//package com.pchr.service.impl;
//import java.time.Duration;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Service;
//import com.pchr.service.RedisService;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class RedisServiceImpl implements RedisService {
//	
//	private final Duration TTL = Duration.ofSeconds(5 * 60); //  TTL은 5분
//	private final StringRedisTemplate stringReidsTemplate;
//	
//	//i
//	public void setRedisValue(String key, String value) { 
//		ValueOperations<String,String> stringValueOperations = stringReidsTemplate.opsForValue();
//		stringValueOperations.set(key,value);
//	}
//	//r
//	public String getRedisValue(String key) {
//		ValueOperations<String,String> stringValueOperations = stringReidsTemplate.opsForValue();
//		String value =stringValueOperations.get(key);
//		if(value==null) {
//			return null;
//		}
//		return value;
//	}
//	//u
//	public void updateRedisValue(String key, String value) {
//		ValueOperations<String,String> stringValueOperations = stringReidsTemplate.opsForValue();
//		stringValueOperations.getAndSet(key, value);
//	}
//	//d
//	public void deleteRedisValue(String key) {
//		stringReidsTemplate.delete(key);
//	}
//	//expire
//	public String setExprieTime(String key) {
//		return stringReidsTemplate.opsForValue().getAndExpire(key, TTL);
//	}
//	//increase
//	public void increment(String key) {
//		setExprieTime(key);
//		stringReidsTemplate.opsForValue().increment(key);
//	}
//
//}

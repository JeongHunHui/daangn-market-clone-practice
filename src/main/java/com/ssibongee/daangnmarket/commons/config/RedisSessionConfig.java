package com.ssibongee.daangnmarket.commons.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

// @Configuration이 붙은 클래스를 스프링에 설정 클래스로 등록(Bean으로 등록)
@Configuration
// @EnableRedisHttpSession: 세션 저장소로 Redis를 사용
// 알아봐야 할 것: main에 붙히는 것과 설정파일에 붙히는 것의 차이점
@EnableRedisHttpSession
public class RedisSessionConfig {

    // @Value("${}"): application.properties 에 정의된 값을 주입
    @Value("${spring.redis.session.host}")
    private String host;

    @Value("${spring.redis.session.port}")
    private int port;

    // 의존관계 주입 시 타입이 같은 Bean이 여러개면 @Primary가 붙은 Bean을 우선적으로 주입
    @Primary
    // 개발자가 작성한 메소드를 통해 반환되는 객체를 Bean으로 만드는 어노테이션
    // @Configuration이 붙은 설정 클래스안에서 사용
    // name 속성으로 Bean의 id를 정할 수 있다.
    @Bean(name = "redisSessionConnectionFactory")
    // Redis와 Connection을 생성해주는 객체
    public RedisConnectionFactory redisConnectionFactory() {
        // 자바의 redis client 라이브러리는 jedis, lettuce가 있는데 lettuce가 더 좋다(이유는 따로 정리)
        // 그래서 lettuce로 redisConnectionFactory를 통해 redis와 연결한다.
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    // RedisTemplate: redis 서버와 통신을 처리하고, redis 모듈을
    // 사용하기 쉽고 다양한 기능을 제공하기 위해 스프링에서 높은 수준의 추상화를 통해서 다양한 작업을 제공
    public RedisTemplate<String, Object> redisTemplate(
            // 같은 타입의 객체 여러개를 Bean으로 등록하면 의존성을 주입받을 때 어떤 객체를 주입해야 할지 알 수 없음
            // -> @Qualifier로 주입받을 객체를 정할 수 있음
            @Qualifier("redisSessionConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // Serializer: Java내부에서 사용하는 객체나 데이터를 외부에서 사용할 수 있도록 Byte 형태의 데이터로 변환하는 기술
        // -> Java의 객체를 Redis에 저장하기 위해서는 직렬화가 필요
        // StringRedisSerializer는 Java의 String을 직렬화한다.
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // GenericJackson2JsonRedisSerializer를 사용하면
        // 별도로 클래스 타입을 지정해줄 필요없이 자동으로 객체를 JSON으로 직렬화 해주는 역할을 수행
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }


}

package com.com.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // 빈 등록
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
public class SecurityConfig {

    /**
     * Spring Security 5.7.x 부터 WebSecurityConfigurerAdapter 는 Deprecated.
     * -> SecurityFilterChain, WebSecurityCustomizer 를 상황에 따라 빈으로 등록해 사용한다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        return http.authorizeRequests()
                .antMatchers("/user/**").authenticated()  // 이런 주소로 들어오면 인증을 하겠다는 뜻
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")  // 이런 주소로 오면 인증 + ADMIN or MANAGER 권한인지 검사
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")  //이런 주소로 오면 인증 + ADMIN 권한인지 검사
                .anyRequest().permitAll()  // 나머지 주소들은 권한을 모두 풀어준다
                .and().formLogin().loginPage("/login")  // 모든 페이지를 설정한 로그인 페이지를 거쳐서 가도록 설정
                .and().build();
    }

// 과거 WebSecurityConfigurerAdapter 인터페이스를 상속해서 사용하던 시절 메서드 재정의해서 사용하던 방법
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.authorizeRequests()
//                .antMatchers("/user/**").authenticated()  // 이런 주소로 들어오면 인증을 하겠다는 뜻
//                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")  // 이런 주소로 오면 인증 + ADMIN or MANAGER 권한인지 검사
//                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")  //이런 주소로 오면 인증 + ADMIN 권한인지 검사
//                .anyRequest().permitAll();  // 나머지 주소들은 권한을 모두 풀어준다
//    }
}

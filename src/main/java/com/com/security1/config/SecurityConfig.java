package com.com.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // 빈 등록
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  // Secured 어노테이션 활성화, preAuthorize/postAuthorize 어노테이션 활성화
public class SecurityConfig {

    @Bean  // 해당 메서드의 리턴되는 오브젝트를 IoC(제어의 역전)로 등록해준다.
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security 5.7.x 부터 WebSecurityConfigurerAdapter 는 Deprecated.
     * -> SecurityFilterChain, WebSecurityCustomizer 를 상황에 따라 빈으로 등록해 사용한다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        return http.authorizeRequests()
                .antMatchers("/user/**").authenticated()  // 이런 주소로 들어오면 인증을 하겠다는 뜻. 인증만 되면 접속 가능
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")  // 이런 주소로 오면 인증 + ADMIN or MANAGER 권한인지 인가 검사
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")  //이런 주소로 오면 인증 + ADMIN 권한인지 검사
                .anyRequest().permitAll()  // 나머지 주소들은 권한을 모두 풀어준다
                .and().formLogin().loginPage("/loginForm")  // 모든 페이지를 설정한 로그인 페이지를 거쳐서 가도록 설정
                .loginProcessingUrl("/login")  // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다. 그래서 컨트롤러에 /login을 만들지 않아도 된다
                .defaultSuccessUrl("/")  // 기본 로그인 주소에서 로그인이 성공하면 메인 페이지로 이동시킴(/user 같은 페이지로 접근해서 로그인 했을 때에는 로그인 후 /user 페이지로 이동시킴)
                .and().oauth2Login().loginPage("/loginForm")  // oauth 로그인을 원래 로그인 주소인 /loginForm으로 맵핑. 구글 로그인이 완료 뒤의 후처리가 필요함
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

package com.com.security1.controller;

import com.com.security1.config.auth.PrincipalDetails;
import com.com.security1.model.User;
import com.com.security1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller // View를 리턴하겠다!!
public class IndexController {

    private final UserService userService;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {  // DI(의존성 주입)
        System.out.println("/test/login ==============");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());

        System.out.println("userDetails : " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth) {  // DI(의존성 주입)
        System.out.println("/test/oauth/login ==============");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("OAuth authentication : " + oAuth2User.getAttributes());
        System.out.println("OAuth2User : " + oAuth.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }

    // localhost:8080/
    // localhost:8080
    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본 폴더 src/main/resources/
        // 뷰리졸버 설정: templates (prefix), .mustache (suffix) 생략가능!!
        return "index";  // src/main/resources/templates/index.mustache
    }

    /**
     * OAuth 로그인을 해도 PrincipalDetails
     * 일반 로그인을 해도 PrincipalDetails
     *
     * 두가지 경우로 분기를 할 필요도 없고, Authentication에 접근할 때 다운 캐스팅을 할 필요도 없다.
     */
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails : " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // 스프링 시큐리티가 해당주를 낚아채버림 -> SecurityConfig 파일 생성 후 작동안함
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        return userService.join(user);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}

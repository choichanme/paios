package com.paios.Config;

import com.paios.Service.MemberService;
import com.paios.Service.impl.MemberServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberServiceImpl memberService;

    @Autowired
    DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }


    @Override
    public void configure(WebSecurity web) throws Exception
    {
        // static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
//        web.ignoring().antMatchers("/static/**" ,"/css/**", "js/**", "img/**", "lib/**", "/user/signup", "/templates/**");
        web.ignoring().antMatchers("/wijmo/**","/css/**", "/js/**", "/img/**","/user/register","/user/findInquiry","/user/dupCheckId","/user/findpw","/user/certnum","/user/password/{user}/{key}","/user/chpassword","/supplier/jusoPopup");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.rememberMe()
                .key("test")
                .userDetailsService(memberService)
                .tokenRepository(tokenRepository())
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(86400 * 14);
        http.csrf()
                .ignoringAntMatchers("/user/findInquiry");
        http.authorizeRequests()
                // 페이지 권한 설정
//                    .antMatchers("/", "/user/**", "/css/**").permitAll()
//                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**").hasAnyRole("BUYER,FORWARDING,SUPPLIER")
                    .antMatchers("/buyer/**").hasRole("BUYER")
                    .antMatchers("/supplier/**").permitAll()
                    .antMatchers("/forward/**").permitAll()
                    .antMatchers("/include/**").permitAll()
//                    .anyRequest().permitAll()
                .and() // 로그인 설정
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/user/login")
//                    .defaultSuccessUrl("/user/login/result")
                    .successHandler(new MyLoginSuccessHandler())
                    .permitAll()
                .and() // 로그아웃 설정
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("remember-me")
                .and()
                // 403 예외처리 핸들링
                    .exceptionHandling()
                        .accessDeniedPage("/user/denied");
//                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }

    public class MyLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            HttpSession session = request.getSession();
            if (session != null) {
                session.setAttribute("greeting", authentication.getName() + "님 반갑습니다.");
                String redirectUrl = (String) session.getAttribute("prevPage");
                System.out.println(redirectUrl);
                if (redirectUrl != null & !redirectUrl.equals("http://localhost:8080/") & !redirectUrl.equals("http://localhost:8080/login") & !redirectUrl.equals("http://localhost:8080/user/register")) {
                    session.removeAttribute("prevPage");
                    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                } else if(redirectUrl != null){
                    if(authentication.getAuthorities().toString().equals("[ROLE_BUYER]")){
                        response.sendRedirect("/");
                    }else if(authentication.getAuthorities().toString().equals("[ROLE_SUPPLIER]")){
                        response.sendRedirect("/supplier/main");
                    }else if(authentication.getAuthorities().toString().equals("[ROLE_FORWARDING]")){
                        response.sendRedirect("/forward/main");
                    }
                } else {
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            } else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
            System.out.println(authentication.getAuthorities().toString());
        }
    }

}

//package com.example.ghtkprofilelink.config;
//
//import com.example.ghtkprofilelink.model.entity.CustomOAuth2User;
//import com.example.ghtkprofilelink.service.CustomOAuth2UserService;
//import com.example.ghtkprofilelink.service.UserDetailsServiceImpl;
//import com.example.ghtkprofilelink.service.UserService;
//import com.example.ghtkprofilelink.service.UserServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private CustomOAuth2UserService oAuth2UserService;
//    @Autowired
//    private UserServiceImpl userService;
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsServiceImpl();
//    }
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//        http.authorizeRequests()
//                .antMatchers().permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll().loginPage("/login")
//                .and()
//                .oauth2Login()
//                    .loginPage("/login")
//                    .userInfoEndpoint()
//                    .userService(oAuth2UserService)
//                .and()
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request,
//                                                        HttpServletResponse response,
//                                                        Authentication authentication) throws IOException, ServletException {
//                        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
//                        userService.processOAuthPostLogin(oAuth2User.getName());
//                    }
//                });
//    }
//}

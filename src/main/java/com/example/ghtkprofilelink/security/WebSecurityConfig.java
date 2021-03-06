package com.example.ghtkprofilelink.security;

import com.example.ghtkprofilelink.error.MyAuthenticationEntryPoint;
import com.example.ghtkprofilelink.model.entity.CustomOAuth2User;
import com.example.ghtkprofilelink.security.jwt.JwtAuthenticationFilter;
import com.example.ghtkprofilelink.service.CustomOAuth2UserService;
import com.example.ghtkprofilelink.service.UserDetailsServiceImpl;
import com.example.ghtkprofilelink.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userService) // Cung c??p userservice cho spring security
                .passwordEncoder(passwordEncoder); // cung c???p password encoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                )
                .and();

        // Enable CORS and disable CSRF
        http.cors().and().csrf().disable()
                .authorizeRequests().antMatchers("/test/**", "/swagger-ui.html#", "/loginFb","/api/v1.0/design/get/**","/api/v1.0/link/list/**","/api/v1.0/profile/getbyshortbio","/api/v1.0/social/get/**").permitAll() // Cho ph??p t???t c??? m???i ng?????i truy c???p v??o login
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll().loginPage("/loginFb")
                .and()
                .oauth2Login()
                .loginPage("/loginFb")
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {
                        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
                        String username = oAuth2User.getName();
                        String email = oAuth2User.getEmail();
                        userService.processOAuthPostLogin(username, email);
                    }
                });
    // T???t c??? c??c request kh??c ?????u c???n ph???i x??c th???c m???i ???????c truy c???p
//                .and().formLogin() // Cho ph??p ng?????i d??ng x??c th???c b???ng form login
//                .defaultSuccessUrl("/swagger-ui.html#/").permitAll() // T???t c??? ?????u ???????c truy c???p v??o ?????a ch??? n??y
//                .and().logout().permitAll(); // Cho ph??p logout



        // Th??m m???t l???p Filter ki???m tra jwt
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        //Exception handling configuration
        http.exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint());

//        // FB
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
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}

package com.example.ghtkprofilelink.security;

import com.example.ghtkprofilelink.constants.RoleEnum;
import com.example.ghtkprofilelink.error.MyAuthenticationEntryPoint;
import com.example.ghtkprofilelink.model.entity.CustomOAuth2User;
import com.example.ghtkprofilelink.security.jwt.JwtAuthenticationFilter;
import com.example.ghtkprofilelink.service.CustomOAuth2UserService;
import com.example.ghtkprofilelink.service.UserDetailsServiceImpl;
import com.example.ghtkprofilelink.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
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
        auth.userDetailsService(userService) // Cung cáp userservice cho spring security
                .passwordEncoder(passwordEncoder); // cung cấp password encoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        * Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

//        * Set unauthorized requests exception handler
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

//        * Enable CORS and disable CSRF and config oauth2
        http.cors().and().csrf().disable();
//                .formLogin().permitAll().loginPage("/loginFb")
//                .and()
//                .oauth2Login()
//                .loginPage("/loginFb")
//                .userInfoEndpoint()
//                .userService(oAuth2UserService)
//                .and()
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request,
//                                                        HttpServletResponse response,
//                                                        Authentication authentication) throws IOException, ServletException {
//                        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
//                        String username = oAuth2User.getName();
//                        String email = oAuth2User.getEmail();
//                        userService.processOAuthPostLogin(username, email);
//                    }
//                });
        // Tất cả các request khác đều cần phải xác thực mới được truy cập
//                .and().formLogin() // Cho phép người dùng xác thực bằng form login
//                .defaultSuccessUrl("/swagger-ui.html#/").permitAll() // Tất cả đều được truy cập vào địa chỉ này
//                .and().logout().permitAll(); // Cho phép logout


//        * Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

//        * Exception handling configuration
        http.exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint());

//        * Phan quyen
        http.authorizeRequests()
                // * Cho phép tất cả mọi người truy cập kể cả chưa đăng nhập
                .antMatchers("/test/**", "/swagger-ui.html#", "/login/oauth/**", "/api/v1.0/design/get/**",
                        "/api/v1.0/link/list/**", "/api/v1.0/profile/shortbio", "/api/v1.0/social/get/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1.0/profile").permitAll()
                
                // * Cho phép tất cả mọi người đăng nhập rồi truy cập
                .antMatchers("/update_password_token", "/api/v1.0/user/username", "/api/v1.0/user/add", "/api/v1.0/charts", "/api/v1.0/link", "/api/v1.0/profile",
                        "/api/v1.0/social").hasAnyAuthority(RoleEnum.USER.name(), RoleEnum.USER_VIP.name(), RoleEnum.ADMIN.name())

                // * Cho phép USER_VIP và ADMIN truy cập
                .antMatchers(HttpMethod.POST, "/api/v1.0/design").hasAnyAuthority(RoleEnum.USER_VIP.name(), RoleEnum.ADMIN.name())
                .antMatchers("/api/v1.0/design/{id}").hasAnyAuthority(RoleEnum.USER_VIP.name(), RoleEnum.ADMIN.name())

                // * Cho phép USER_VIP truy cập
                .antMatchers().hasRole(RoleEnum.USER_VIP.name())

                // * Cho phép ADMIN truy cập
                .antMatchers().hasRole(RoleEnum.ADMIN.name())
                .anyRequest().authenticated();

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

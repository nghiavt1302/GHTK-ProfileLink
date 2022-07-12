package com.example.ghtkprofilelink.security;

import com.example.ghtkprofilelink.security.jwt.JwtAuthenticationFilter;
import com.example.ghtkprofilelink.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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
                .authorizeRequests().antMatchers("/test/**", "/swagger-ui.html#").permitAll() // Cho phép tất cả mọi người truy cập vào login
                .anyRequest().authenticated(); // Tất cả các request khác đều cần phải xác thực mới được truy cập
//                .and().formLogin() // Cho phép người dùng xác thực bằng form login
//                .defaultSuccessUrl("/swagger-ui.html#/").permitAll() // Tất cả đều được truy cập vào địa chỉ này
//                .and().logout().permitAll(); // Cho phép logout


        // Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
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

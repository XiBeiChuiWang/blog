//package com.star.config;
//
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity
//public class MySecurity extends WebSecurityConfigurerAdapter {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/admin","/admin/login").permitAll()
//                .antMatchers("/admin/**").hasRole("v1");
//        http.formLogin();
//        http.formLogin().loginPage("/admin");
//    }
//}

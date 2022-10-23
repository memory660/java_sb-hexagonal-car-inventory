package com.alvesjv.projecthexagonalfull.config;

import com.alvesjv.projecthexagonalfull.app.domain.entities.User;
import com.alvesjv.projecthexagonalfull.app.domain.enums.Role;
import com.alvesjv.projecthexagonalfull.app.ports.out.DataBaseIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableAuthorizationServer
@EnableResourceServer
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("user")
    private DataBaseIntegration dataBase;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        List<User> users = dataBase.findAll();

        users.stream().forEach(user -> {
            String role = user.getRole() == Role.ADMIN ? "ADMIN" : "USER";

            try {
                auth.inMemoryAuthentication()
                        .withUser(user.getUsername())
                        .password(user.getPassword())
                        .roles(role);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring()
                .antMatchers("/h2/**")
                .antMatchers("/actuator/**")
                .antMatchers("/instances/**")
                .antMatchers("/applications/**")
                .antMatchers("/wallboard/**")
                .antMatchers("/assets/**","/sba-settings.js");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}

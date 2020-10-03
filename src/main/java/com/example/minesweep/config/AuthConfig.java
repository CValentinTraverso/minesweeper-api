package com.example.minesweep.config;

import com.example.minesweep.rest.AuthenticationFilter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthConfig extends WebSecurityConfigurerAdapter {

    @Value("${software.security.jwt.secret-key}")
    private String secretKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        http.csrf().disable()
                .addFilterBefore(new AuthenticationFilter(secretKey), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/*/account/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/*/public/**").permitAll()
                .antMatchers("/**").authenticated();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(Lists.newArrayList(HttpMethod.DELETE.name(), HttpMethod.GET.name(),
                HttpMethod.OPTIONS.name(), HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.PATCH.name()));
        http.cors().configurationSource(request -> corsConfiguration);

    }
}
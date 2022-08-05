package com.plusitsolution.timesheet.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.plusitsolution.common.vaultfiltermvc.filter.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().antMatcher("/secure/**")
                // Disable CRSF
                .csrf().disable().cors().and()
                // STATELESS Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // Api Key Filter
                .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).authorizeRequests()
//                REGISTRATOR
                .antMatchers(HttpMethod.GET, "/secure/admin/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/secure/admin/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/secure/admin/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/secure/admin/**").hasAnyRole("ADMIN")
//                
                .antMatchers(HttpMethod.GET, "/secure/employee/**").hasAnyRole("ADMIN", "EMPLOYEE")
                .antMatchers(HttpMethod.POST, "/secure/employee/**").hasAnyRole("ADMIN", "EMPLOYEE")
                .antMatchers(HttpMethod.PUT, "/secure/employee/**").hasAnyRole("ADMIN", "EMPLOYEE")
                .antMatchers(HttpMethod.DELETE, "/secure/employee/**").hasAnyRole("ADMIN", "EMPLOYEE")

                .anyRequest().authenticated();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> origins = new ArrayList<>();
        origins.add("http://localhost:54321");
        origins.add("http://localhost:1234");
        origins.add("http://localhost:8081");
        
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
//        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "apikey",
                "token", "jwttoken", "role", "user", "org", "*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}


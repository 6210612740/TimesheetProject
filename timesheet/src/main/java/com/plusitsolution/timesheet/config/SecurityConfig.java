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
                .antMatchers(HttpMethod.GET, "/secure/regis/**").hasRole("REGISTRATOR")
                .antMatchers(HttpMethod.POST, "/secure/regis/**").hasRole("REGISTRATOR")
                .antMatchers(HttpMethod.PUT, "/secure/regis/**").hasRole("REGISTRATOR")
                .antMatchers(HttpMethod.DELETE, "/secure/regis/**").hasRole("REGISTRATOR")
//                FINANCEOFFICER
                .antMatchers(HttpMethod.GET, "/secure/fin/**").hasRole("FINANCEOFFICER")
                .antMatchers(HttpMethod.POST, "/secure/fin/**").hasRole("FINANCEOFFICER")
                .antMatchers(HttpMethod.PUT, "/secure/fin/**").hasRole("FINANCEOFFICER")
                .antMatchers(HttpMethod.DELETE, "/secure/fin/**").hasRole("FINANCEOFFICER")
//                DEBTCOLLECTOR
                .antMatchers(HttpMethod.GET, "/secure/debt/**").hasRole("DEBTCOLLECTOR")
                .antMatchers(HttpMethod.POST, "/secure/debt/**").hasRole("DEBTCOLLECTOR")
                .antMatchers(HttpMethod.PUT, "/secure/debt/**").hasRole("DEBTCOLLECTOR")
                .antMatchers(HttpMethod.DELETE, "/secure/debt/**").hasRole("DEBTCOLLECTOR")
                .anyRequest().authenticated();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> origins = new ArrayList<>();
        origins.add("http://localhost:54321");
        origins.add("https://hec.beta.thaivivat.co.th");
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


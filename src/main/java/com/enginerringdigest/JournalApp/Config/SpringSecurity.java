package com.enginerringdigest.JournalApp.Config;

import com.enginerringdigest.JournalApp.Service.CustomUserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Configuration
public class SpringSecurity {

    private final CustomUserDetailsServiceImp userDetailsService;

    public SpringSecurity(CustomUserDetailsServiceImp userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public AuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder   ) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider) throws Exception {


//        http
//                .csrf(csrf -> csrf.disable())
//
//                .authorizeHttpRequests(auth -> auth
//
//                        // Signup is public
//                        .requestMatchers(
//                                HttpMethod.POST,
//                                "/public/create-user"
//                        ).permitAll()
//
//                        // Update requires authentication
//                        .requestMatchers(
//                                HttpMethod.PUT,
//                                "/public"
//                        ).authenticated()
//
//                        // Everything else requires authentication
//                        .anyRequest().authenticated()
//                )
//
//                .authenticationProvider(authenticationProvider)
//
//                .httpBasic(Customizer.withDefaults())
//
//                .formLogin(form -> form.permitAll());
//
//        return http.build();



        return http.authorizeHttpRequests(request -> request
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/journal/**", "/user/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();

    }
}
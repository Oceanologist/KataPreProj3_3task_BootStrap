package ru.kata.spring.boot_security.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Autowired
    SecurityConfig( UserDetailsService userDetailsService){
        this.userDetailsService=userDetailsService;
    }


    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/hello_page/**").permitAll()
                        .requestMatchers("/user_page/**").hasAnyRole("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers("/admin_page/**").hasRole("ROLE_ADMIN")
                        .anyRequest().authenticated())

                .formLogin(form -> form // 3. Настройка формы входа
                        .defaultSuccessUrl("/dashboard")
                        .failureUrl("hello_page")
                        .permitAll()
                )
                .logout(logout -> logout // 4. Настройка выхода
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/hello_page")
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(ex -> ex // 5. Обработка ошибок
                        .accessDeniedPage("/access-denied")
                );
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Используем BCrypt
    }
}
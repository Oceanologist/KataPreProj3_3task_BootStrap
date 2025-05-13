package ru.kata.spring.boot_security.demo.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/hello_page/**").permitAll()
                        .requestMatchers("/user_page/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin_page/**").hasRole("ADMIN")
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

    /* access-denied - ошибка прав доступа юзер зашел к админу
    /dashboard здесь нужно расписать куда перенаправлять к юзеру или к админу

     */


//    private final UserDetailsService userDetailsService;
//
//    @Autowired
//    public WebSecurityConfig(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService)  // Указываем свой UserDetailsService
//                .passwordEncoder(passwordEncoder());    // Добавляем шифрование паролей
//    }


    // аутентификация inMemory
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password(passwordEncoder())
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Используем BCrypt
    }
}
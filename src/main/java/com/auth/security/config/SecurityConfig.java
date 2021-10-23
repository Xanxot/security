package com.auth.security.config;

import com.auth.security.model.Permission;
import com.auth.security.model.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) /** для включения альтернативного способа
// проверки прав пользователя через @PreAuthorize **/
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * Конфигурация доступа к http методам в зависимости от роли пользователя или прав пользователя
         * **/

        //реализация прав доступа с помощью Permission
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll() //доступ к главной странице имеют все
                .antMatchers(HttpMethod.GET, "/api/**").hasAuthority(Permission.USER_READ.getPermission())
                .antMatchers(HttpMethod.POST, "/api/**").hasAuthority(Permission.USER_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority(Permission.USER_WRITE.getPermission())
                .antMatchers(HttpMethod.PATCH, "/api/**").hasAuthority(Permission.USER_WRITE.getPermission()) //.hasAnyRole(Roles.ADMIN.name())
                .anyRequest().authenticated().and()//все запросы доступны только после аутентификации
                .formLogin()
                .loginPage("/auth/login").permitAll()
                .defaultSuccessUrl("/auth/success")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("auth/logout", "POST"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESESSIONID")
                .logoutSuccessUrl("/auth/login");

    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("admin"))
                        //.roles(Roles.ADMIN.name())
                        .authorities(Roles.ADMIN.grantedAuthorities())
                        .build(),
                User.builder()
                        .username("user")
                        .password(passwordEncoder().encode("user"))
                        //.roles(Roles.USER.name())
                        .authorities(Roles.USER.grantedAuthorities())
                        .build()
        );
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}

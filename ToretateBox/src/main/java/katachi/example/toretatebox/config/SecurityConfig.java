package katachi.example.toretatebox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // CSRF 無効化（自作ログインのため）
            .csrf(csrf -> csrf.disable())

            // 認可設定
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers(
            			    "/", "/top",
            			    "/products", "/products/**",
            			    "/css/**", "/js/**", "/images/**",
            			    "/search/**",
            			    "/cart", "/cart/**",
            			    "/login",
            			    "/signup"        // ← ★これを追加
            			).permitAll()
                .anyRequest().authenticated()
            )

            // ★ Spring Security のログイン機能を使わない
            .formLogin(form -> form.disable())

            // ログアウトだけ使う
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/top")
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

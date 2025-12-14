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

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth

            // ▼ ゲストがアクセスできる場所（すべて許可）
            .requestMatchers(
                    "/", "/top",
                    "/products", "/products/**",   // ← 商品一覧・詳細を完全許可
                    "/css/**", "/js/**", "/images/**",
                    "/search/**",
                    "/cart", "/cart/**"
            ).permitAll()

            // ▼ ログインページ許可
            .requestMatchers("/login").permitAll()

            // ▼ その他はログイン必須
            .anyRequest().authenticated()
        );

        // ▼ ログイン設定
        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/top", true)
                .permitAll()
        );

        // ▼ ログアウト設定
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/top")
                .permitAll()
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


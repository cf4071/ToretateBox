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
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // ▼ ゲストでもアクセス可能なページ
                .requestMatchers(
                    "/", "/top", "/products", "/products/**", // ← 追加
                    "/css/**", "/search/**", "/images/**"
                ).permitAll()

                // ▼ ログインページは誰でもアクセスOK
                .requestMatchers("/login").permitAll()

                // ▼ それ以外はログイン必須
                .anyRequest().authenticated()
            )

            // ▼ 自作ログインページの指定
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")      // ← form の name と一致
                .passwordParameter("password")   // ← form の name と一致
                .defaultSuccessUrl("/top", true) // ← ログイン後 /top へ
                .permitAll()
            )

            .logout(logout -> logout
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

package katachi.example.toretatebox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * セキュリティ設定本体
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider authenticationProvider
    ) throws Exception {

        http
            // CSRF は今回は無効
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
                    "/signup"
                ).permitAll()
                .anyRequest().authenticated()
            )

            // ★ ログイン設定（自作 login.html）
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/top", true)
                .failureUrl("/login?error")
                .permitAll()
            )

            // ログアウト設定
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/top")
                .permitAll()
            )

            // ★ DB認証を使う
            .authenticationProvider(authenticationProvider);

        return http.build();
    }

    /**
     * BCrypt パスワードエンコーダ
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * DB（UserDetailsService）を使った認証プロバイダ
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}

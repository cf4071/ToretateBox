package katachi.example.toretatebox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * 認可設定（URLへのアクセス制御）
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // CSRFは今回のログイン方式では一旦無効（必要な時にオンに変更）
            .csrf(csrf -> csrf.disable())

            // ページごとのアクセス権限
            .authorizeHttpRequests(auth -> auth
                // ログイン関連とCSSなどを許可
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/top").permitAll()
                // ↑★ここに /top を追加しました！★

                // その他はすべてログイン済みを要求
                .anyRequest().authenticated()
            )

            // ログインページの設定（Spring Security を使わず自前でログイン）
            .formLogin(form -> form
                .loginPage("/login")     // ログイン画面URL
                .permitAll()
            )

            // ログアウト設定
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login") // ログアウト後はログイン画面へ
                .permitAll()
            );

        return http.build();
    }

    /**
     * パスワードのハッシュ化に必須
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

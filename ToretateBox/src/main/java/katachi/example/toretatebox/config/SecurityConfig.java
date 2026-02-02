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

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider authenticationProvider
    ) throws Exception {

        http
            // ✅ CSRF は今回は無効（テンプレ側の _csrf は th:if 付きならOK）
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // ✅ 静的リソース（画像アップロード含む）は先に許可
                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/uploads/**"   // ★追加：アップロード画像を誰でも見れるようにする
                ).permitAll()

                // ✅ 公開ページ（ログイン不要）
                .requestMatchers(
                    "/", "/top",
                    "/products", "/products/**",
                    "/search/**",
                    "/cart", "/cart/**",
                    "/login",
                    "/signup",
                    "/guest", "/guest/**",
                    "/order/**"
                ).permitAll()

                // ✅ 管理画面は管理者だけ（uploadsは上でpermitAll済みなので影響なし）
                .requestMatchers("/admin/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")

                // ✅ ログイン成功後：管理者なら管理画面、それ以外は /top
                .successHandler((request, response, authentication) -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                    if (isAdmin) {
                        response.sendRedirect("/admin/products");
                    } else {
                        response.sendRedirect("/top");
                    }
                })

                .failureUrl("/login?error")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/top")
                .permitAll()
            )

            .authenticationProvider(authenticationProvider);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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

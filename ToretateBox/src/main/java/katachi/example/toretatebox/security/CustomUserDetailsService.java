package katachi.example.toretatebox.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // email でユーザー検索
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません");
        }

        // Spring Security 用 UserDetails に変換
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),   // ★ BCrypt ハッシュ
                Collections.emptyList()
        );
    }
}

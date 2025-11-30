package katachi.example.toretatebox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 新規ユーザー登録
     * パスワードをハッシュ化して保存する
     */
    public User registerUser(User user) {

        // メールアドレス重複チェック
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスは既に使用されています。");
        }

        // パスワードをハッシュ化
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // 保存
        return userRepository.save(user);
    }

    /**
     * ログイン用：メールアドレスからユーザーを取得する
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * パスワードの一致判定
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

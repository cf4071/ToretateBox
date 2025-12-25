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
     * ・メール重複チェック
     * ・パスワードを BCrypt でハッシュ化
     * ・初期フラグを設定して保存
     */
    public User register(User user) {

        // ▼ メールアドレス重複チェック
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                "このメールアドレスは既に使用されています。"
            );
        }

        // ▼ パスワードを BCrypt でハッシュ化
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // ▼ 初期値設定
        user.setAdmin(false);     // 管理者ではない
        user.setDeleted(false);   // 有効ユーザー

        // ▼ 保存
        return userRepository.save(user);
    }

    /**
     * ログイン用
     * メールアドレスからユーザーを取得
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * パスワード一致チェック
     * raw（入力）と encoded（DB）を比較
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * メールアドレスの存在チェック（登録画面用）
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

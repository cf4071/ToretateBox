package katachi.example.toretatebox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.AddressRepository;
import katachi.example.toretatebox.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 新規ユーザー登録（ユーザーのみ）
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ▼ 初期値設定
        user.setAdmin(false);     // 管理者ではない
        user.setDeleted(false);   // 有効ユーザー

        // ▼ 保存
        return userRepository.save(user);
    }

    /**
     * 新規ユーザー登録（ユーザー + 住所）
     * ・users / addresses を同時に保存
     * ・途中で失敗したらロールバック
     */
    @Transactional
    public void registerWithAddress(User user, Address address) {

        // ▼ メール重複チェック
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                "このメールアドレスは既に使用されています。"
            );
        }

        // ▼ パスワードを BCrypt でハッシュ化
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ▼ 初期値設定
        user.setAdmin(false);
        user.setDeleted(false);

        // ▼ ユーザー保存
        User savedUser = userRepository.save(user);

        // ▼ 住所に user_id を設定
        address.setUserId(savedUser.getId());

        // ▼ 住所保存
        addressRepository.save(address);
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

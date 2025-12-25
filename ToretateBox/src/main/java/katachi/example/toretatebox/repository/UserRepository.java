package katachi.example.toretatebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * メールアドレスからユーザーを取得
     * ・ログイン処理で使用
     */
    User findByEmail(String email);

    /**
     * メールアドレスの存在チェック
     * ・ユーザー登録時の重複チェック用
     */
    boolean existsByEmail(String email);
}

package katachi.example.toretatebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import katachi.example.toretatebox.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * メールアドレスからユーザーを検索
     * → ログイン時によく使う
     */
    User findByEmail(String email);

    /**
     * メールアドレスが存在するか確認
     * → 新規登録時の重複チェックに使用
     */
    boolean existsByEmail(String email);
}

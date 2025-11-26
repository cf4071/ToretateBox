package katachi.example.toretatebox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.form.LoginForm;
import katachi.example.toretatebox.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * ログイン画面表示
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login/login";
    }

    /**
     * ログイン処理
     */
    @PostMapping("/login")
    public String login(
            @Valid LoginForm form,
            BindingResult bindingResult,
            Model model) {

        // 入力チェック
        if (bindingResult.hasErrors()) {
            return "login/login";
        }

        // メールからユーザー取得
        User user = userService.findByEmail(form.getEmail());

        // ユーザーが存在しない or パスワード不一致
        if (user == null || !userService.checkPassword(form.getPassword(), user.getPassword())) {
            model.addAttribute("errorMessage", "メールアドレスまたはパスワードが違います。");
            return "login/login";
        }

        // 削除済みユーザーかチェック（必要なら）
        if (user.is_deleted()) {
            model.addAttribute("errorMessage", "このアカウントは無効です。");
            return "login/login";
        }

        // ログイン成功 → ホームへ
        return "redirect:/home";
    }
}

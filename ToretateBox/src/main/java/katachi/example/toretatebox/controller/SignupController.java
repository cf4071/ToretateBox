package katachi.example.toretatebox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.form.SignupForm;
import katachi.example.toretatebox.service.UserService;

@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    /**
     * ユーザー登録画面表示
     */
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "user/signup";
    }

    /**
     * ユーザー登録処理
     */
    @PostMapping("/signup")
    public String signup(
            @Valid SignupForm form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "signup/signup";
        }

        // メール重複チェック
        if (userService.existsByEmail(form.getEmail())) {
            model.addAttribute("errorMessage", "このメールアドレスは既に登録されています。");
            return "signup/signup";
        }

        // User エンティティに詰め替え
        User user = new User();
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setPassword(form.getPassword()); // ← 平文でOK（Serviceでハッシュ化）

        userService.register(user);

        // 登録後はログイン画面へ
        return "redirect:/login";
    }
}

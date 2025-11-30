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
        return "login/login";   // ★ これが正しい！
    }

    @PostMapping("/login")
    public String login(
            @Valid LoginForm form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "login/login";   // ★ これに戻す！
        }

        User user = userService.findByEmail(form.getEmail());

        if (user == null || !userService.checkPassword(form.getPassword(), user.getPassword())) {
            model.addAttribute("errorMessage", "メールアドレスまたはパスワードが違います。");
            return "login/login";   // ★
        }

        if (user.is_deleted()) {
            model.addAttribute("errorMessage", "このアカウントは無効です。");
            return "login/login";   // ★
        }

        return "redirect:/home";
    }
}

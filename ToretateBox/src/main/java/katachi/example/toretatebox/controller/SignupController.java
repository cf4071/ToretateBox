package katachi.example.toretatebox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import katachi.example.toretatebox.domain.model.Address;
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
     * users + addresses を同時に保存
     */
    @PostMapping("/signup")
    public String signup(
            @Valid SignupForm form,
            BindingResult bindingResult,
            Model model) {

        /* ===== 入力チェック（アノテーション） ===== */
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }

        /* ===== パスワード一致チェック ===== */
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            bindingResult.rejectValue(
                "passwordConfirm",
                "password.mismatch",
                "パスワードが一致しません。"
            );
            return "user/signup";
        }

        try {
            /* ===== User 作成 ===== */
            User user = new User();
            user.setName(form.getName());
            user.setNameKana(form.getNameKana());
            user.setPhoneNumber(form.getPhoneNumber());
            user.setEmail(form.getEmail());
            user.setPassword(form.getPassword());

            /* ===== Address 作成 ===== */
            Address address = new Address();
            address.setRecipient(form.getName());
            address.setPhoneNumber(form.getPhoneNumber());
            address.setPostalCode(form.getPostalCode());
            address.setPrefecture(form.getPrefecture());
            address.setCity(form.getCity());
            address.setAddressLine1(form.getAddress());
            address.setAddressLine2(form.getBuilding());

            /* ===== 同時登録 ===== */
            userService.registerWithAddress(user, address);

        } catch (IllegalArgumentException e) {
            // メール重複など（全体エラー）
            model.addAttribute("errorMessage", e.getMessage());
            return "user/signup";
        }

        /* ===== 登録成功 ===== */
        return "redirect:/top";
    }
}

package katachi.example.toretatebox.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.AddressRepository;
import katachi.example.toretatebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @GetMapping("/user/mypage")
    public String mypage(Authentication auth, Model model) {

        // ログインユーザー(emailで取得)
        String email = auth.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "redirect:/login";
        }

        // 住所情報を取得（addressesテーブル）
        Address address = addressRepository.findByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("address", address);

        // パスワードはマスク表示
        model.addAttribute("maskedPassword", "●●●●●●●●");

        return "user/mypage";
    }
}

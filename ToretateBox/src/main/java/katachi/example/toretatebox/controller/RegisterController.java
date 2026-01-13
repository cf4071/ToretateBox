package katachi.example.toretatebox.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.UserRepository;
import katachi.example.toretatebox.service.AddressService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final AddressService addressService;
    private final UserRepository userRepository;

    @GetMapping("/register")
    public String showRegister(Authentication auth, HttpSession session, Model model) {

        // =========================
        // ゲスト（未ログイン）
        // =========================
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {

            Integer addressId = (Integer) session.getAttribute("guestAddressId");
            if (addressId == null) {
                return "redirect:/guest";
            }

            Address address = addressService.findById(addressId);
            model.addAttribute("address", address);

            return "user/register";
        }

        // =========================
        // ログイン済みユーザー
        // =========================
        String email = auth.getName(); // ←ログインID（email）
        User user = userRepository.findByEmail(email);

        if (user != null) {
            Address address = addressService.findLatestByUserId(user.getId());
            model.addAttribute("address", address);
        }

        return "user/register";
    }
}

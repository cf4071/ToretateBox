package katachi.example.toretatebox.controller.user;

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

    @GetMapping("/mypage")
    public String mypage(Authentication auth, Model model) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "redirect:/login";
        }

        Address address = addressRepository.findTopByUserIdOrderByIdDesc(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("address", address);

        model.addAttribute("maskedPassword", "●●●●●●●●");

        return "user/mypage";
    }
}

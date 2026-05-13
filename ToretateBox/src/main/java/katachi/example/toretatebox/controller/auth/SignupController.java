package katachi.example.toretatebox.controller.auth;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.form.SignupForm;
import katachi.example.toretatebox.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(
            @Valid @ModelAttribute("signupForm") SignupForm form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            bindingResult.rejectValue(
                "passwordConfirm",
                "password.mismatch",
                "パスワードが一致しません。"
            );
            return "auth/signup";
        }

        try {
            User user = modelMapper.map(form, User.class);

            Address address = modelMapper.map(form, Address.class);
            address.setRecipient(form.getName());

            userService.registerWithAddress(user, address);

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/signup";
        }

        return "redirect:/login";
    }
}
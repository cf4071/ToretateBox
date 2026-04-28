package katachi.example.toretatebox.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class SignupController {

    @Autowired
    private UserService userService;

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
            User user = new User();
            user.setName(form.getName());
            user.setNameKana(form.getNameKana());
            user.setPhoneNumber(form.getPhoneNumber());
            user.setEmail(form.getEmail());
            user.setPassword(form.getPassword());

            Address address = new Address();
            address.setRecipient(form.getName());
            address.setPhoneNumber(form.getPhoneNumber());
            address.setPostalCode(form.getPostalCode());
            address.setPrefecture(form.getPrefecture());
            address.setCity(form.getCity());
            address.setAddressLine1(form.getAddressLine1());
            address.setAddressLine2(form.getAddressLine2());

            userService.registerWithAddress(user, address);

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/signup";
        }

        return "redirect:/login";
    }
}
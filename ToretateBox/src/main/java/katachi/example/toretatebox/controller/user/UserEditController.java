package katachi.example.toretatebox.controller.user;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.form.UserEditForm;
import katachi.example.toretatebox.repository.AddressRepository;
import katachi.example.toretatebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserEditController {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @GetMapping("/edit")
    public String showEdit(Authentication auth, Model model) {

        if (auth == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(auth.getName());
        if (user == null) {
            return "redirect:/login";
        }

        Address address = addressRepository.findTopByUserIdOrderByIdDesc(user.getId());

        UserEditForm form = modelMapper.map(user, UserEditForm.class);

        form.setPassword("");
        form.setPasswordConfirm("");

        if (address != null) {
            modelMapper.map(address, form);
        }

        model.addAttribute("userEditForm", form);

        return "user/user_edit";
    }

    @PostMapping("/edit")
    public String update(
            Authentication auth,
            @Valid @ModelAttribute("userEditForm") UserEditForm form,
            BindingResult result,
            Model model) {

        if (auth == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(auth.getName());

        if (user == null) {
            return "redirect:/login";
        }

        if (form.getPassword() != null && !form.getPassword().isBlank()) {

            if (form.getPassword().length() < 8) {
                result.rejectValue(
                        "password",
                        "password.size",
                        "パスワードは8文字以上で入力してください"
                );
            }

            if (!form.getPassword().equals(form.getPasswordConfirm())) {
                result.rejectValue(
                        "passwordConfirm",
                        "password.mismatch",
                        "パスワードが一致しません"
                );
            }
        }

        User existingUser = userRepository.findByEmail(form.getEmail());

        if (existingUser != null && existingUser.getId() != user.getId()) {
            result.rejectValue(
                    "email",
                    "email.duplicate",
                    "そのメールアドレスはすでに使用されています"
            );
        }

        if (result.hasErrors()) {
            return "user/user_edit";
        }

        Address address = addressRepository.findTopByUserIdOrderByIdDesc(user.getId());

        if (address == null) {
            address = new Address();
        }

        String currentPassword = user.getPassword();

        modelMapper.map(form, user);

        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(form.getPassword()));
        } else {
            user.setPassword(currentPassword);
        }

        modelMapper.map(form, address);

        address.setUserId(user.getId());
        address.setRecipient(form.getName());

        userRepository.save(user);
        addressRepository.save(address);

        return "redirect:/mypage";
    }
}
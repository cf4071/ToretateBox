package katachi.example.toretatebox.controller.user;

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

    @GetMapping("/edit")
    public String showEdit(Authentication auth, Model model) {

        User user = userRepository.findByEmail(auth.getName());
        if (user == null) return "redirect:/login";

        Address address = addressRepository.findTopByUserIdOrderByIdDesc(user.getId());

        UserEditForm form = new UserEditForm();
        form.setName(user.getName());
        form.setNameKana(user.getNameKana());
        form.setPhoneNumber(user.getPhoneNumber());
        form.setEmail(user.getEmail());
        form.setPassword("");
        form.setPasswordConfirm("");

        if (address != null) {
            form.setPostalCode(address.getPostalCode());
            form.setPrefecture(address.getPrefecture());
            form.setCity(address.getCity());
            form.setAddressLine1(address.getAddressLine1());
            form.setAddressLine2(address.getAddressLine2());
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

        if (!result.hasErrors()) {
            if (form.getPassword() != null && !form.getPassword().isBlank()) {
                if (!form.getPassword().equals(form.getPasswordConfirm())) {
                    result.rejectValue("passwordConfirm", "password.mismatch", "パスワードが一致しません");
                }
            }
        }

        if (result.hasErrors()) {
            return "user/user_edit";
        }

        User user = userRepository.findByEmail(auth.getName());
        if (user == null) return "redirect:/login";

        Address address = addressRepository.findTopByUserIdOrderByIdDesc(user.getId());
        if (address == null) {
            address = new Address();
            address.setUserId(user.getId());
        }

        user.setName(form.getName());
        user.setNameKana(form.getNameKana());
        user.setPhoneNumber(form.getPhoneNumber());

        // メール変更をいったん止めるならコメントアウト
        user.setEmail(form.getEmail());

        // パスワードは入力されたときだけ更新
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        address.setRecipient(form.getName());
        address.setPhoneNumber(form.getPhoneNumber());
        address.setPostalCode(form.getPostalCode());
        address.setPrefecture(form.getPrefecture());
        address.setCity(form.getCity());
        address.setAddressLine1(form.getAddressLine1());
        address.setAddressLine2(form.getAddressLine2());

        userRepository.save(user);
        addressRepository.save(address);

        return "redirect:/mypage";
    }
}

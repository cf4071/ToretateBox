package katachi.example.toretatebox.controller.cart;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.form.GuestForm;
import katachi.example.toretatebox.repository.UserRepository;
import katachi.example.toretatebox.service.AddressService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GuestController {

    // ゲスト購入用の固定ユーザーID
    private static final int GUEST_USER_ID = -1;

    private final AddressService addressService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/guest")
    public String showGuest(Model model) {

        if (!model.containsAttribute("guestForm")) {
            model.addAttribute("guestForm", new GuestForm());
        }

        return "cart/guest";
    }

    @PostMapping("/guest")
    public String submitGuest(
            @Valid @ModelAttribute("guestForm") GuestForm form,
            BindingResult bindingResult,
            HttpSession session,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.guestForm",
                    bindingResult);

            redirectAttributes.addFlashAttribute("guestForm", form);

            return "redirect:/guest?error";
        }

        session.setAttribute("guestForm", form);

        Address address = modelMapper.map(form, Address.class);

        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName());

            if (user != null) {
                address.setUserId(user.getId());
            }

        } else {
            // ゲスト購入時は固定ユーザーを使用する
            address.setUserId(GUEST_USER_ID);
        }

        address.setRecipient(form.getName());

        Address saved = addressService.save(address);

        session.setAttribute("guestAddressId", saved.getId());

        return "redirect:/checkout";
    }
}
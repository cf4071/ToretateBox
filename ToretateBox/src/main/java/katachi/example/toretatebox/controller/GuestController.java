package katachi.example.toretatebox.controller;

import java.security.Principal;

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

    private final AddressService addressService;
    private final UserRepository userRepository;

    @GetMapping("/guest")
    public String showGuest(Model model, HttpSession session) {

        GuestForm form = (GuestForm) session.getAttribute("guestForm");
        if (form == null) form = new GuestForm();

        model.addAttribute("guestForm", form);
        return "user/guest";
    }

    @PostMapping("/guest")
    public String submitGuest(
            @Valid @ModelAttribute("guestForm") GuestForm form,
            BindingResult bindingResult,
            HttpSession session,
            Principal principal,
            RedirectAttributes ra) {

        if (bindingResult.hasErrors()) {
            return "user/guest";
        }

        session.setAttribute("guestForm", form);

        Address address = new Address();

        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName());
            if (user != null) {
                address.setUserId(user.getId());
            }
        }

        address.setRecipient(form.getName());
        address.setPhoneNumber(form.getPhoneNumber());
        address.setPostalCode(form.getPostalCode());
        address.setPrefecture(form.getPrefecture());
        address.setCity(form.getCity());
        address.setAddressLine1(form.getAddressLine1());
        address.setAddressLine2(form.getAddressLine2());

        Address saved = addressService.save(address);

        session.setAttribute("guestAddressId", saved.getId());

        return "redirect:/order/register";
    }
}

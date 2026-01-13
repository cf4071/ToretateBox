package katachi.example.toretatebox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.form.GuestForm;
import katachi.example.toretatebox.service.AddressService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GuestController {

    private final AddressService addressService;

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
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "user/guest";
        }

        // 入力保持（戻ってきた時のため）
        session.setAttribute("guestForm", form);

        // ▼ GuestForm → Address(Entity)
        Address address = new Address();
        address.setRecipient(form.getName());
        address.setPhoneNumber(form.getPhoneNumber());
        address.setPostalCode(form.getPostalCode());
        address.setPrefecture(form.getPrefecture());
        address.setCity(form.getCity());
        address.setAddressLine1(form.getAddress());
        address.setAddressLine2(form.getBuilding());

        // ▼ DB保存
        Address saved = addressService.save(address);

        // ▼ 次のレジ画面で使う（どの住所か）
        session.setAttribute("guestAddressId", saved.getId());

        return "redirect:/register";
    }
}

package katachi.example.toretatebox.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import katachi.example.toretatebox.domain.dto.AdminUserRow;
import katachi.example.toretatebox.domain.model.Address;
import katachi.example.toretatebox.domain.model.User;
import katachi.example.toretatebox.repository.AddressRepository;
import katachi.example.toretatebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;


    @GetMapping("/users")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        PageRequest pageable = PageRequest.of(page, 10, Sort.by("id").ascending());
        Page<User> userPage = userRepository.findAll(pageable);

        List<AdminUserRow> rows = userPage.getContent().stream().map(u -> {

            Address addr = addressRepository.findTopByUserIdOrderByIdDesc(u.getId());

            String postalCode = (addr != null) ? addr.getPostalCode() : "";
            String addressText = (addr != null) ? buildAddress(addr) : "";

            return new AdminUserRow(
                    u.getId(),
                    u.getEmail(),
                    u.getName(),
                    u.getPhoneNumber(),
                    postalCode,
                    addressText,
                    u.getCreatedAt()
            );
        }).collect(Collectors.toList());

        model.addAttribute("rows", rows);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());

        return "admin/users";
    }


    @GetMapping("/users/{id}")
    public String detail(
            @PathVariable Integer id,
            Model model) {

        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            return "redirect:/admin/users";
        }

        User user = opt.get();

        Address addr = addressRepository.findTopByUserIdOrderByIdDesc(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("addr", addr);
        model.addAttribute("postalCode", addr != null ? addr.getPostalCode() : "");
        model.addAttribute("addressText", addr != null ? buildAddress(addr) : "");

        return "admin/user_detail";
    }

    private String buildAddress(Address a) {
        String line2 = (a.getAddressLine2() == null || a.getAddressLine2().isBlank())
                ? ""
                : " " + a.getAddressLine2();

        return a.getPrefecture() + a.getCity() + " " + a.getAddressLine1() + line2;
    }
}

package katachi.example.toretatebox.controller.admin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.domain.model.ProductForm;
import katachi.example.toretatebox.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductController {

    private final ProductsRepository productsRepository;

    @GetMapping("/products")
    public String list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        int size = 10;
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> productPage = productsRepository.findAll(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "admin/product_list";
    }

    @ModelAttribute("productForm")
    public ProductForm setUpProductForm() {
        return new ProductForm();
    }

    @GetMapping("/products_new")
    public String showNew() {
        return "admin/product_new";
    }

    @GetMapping("/product_new")
    public String showNewAlias() {
        return "admin/product_new";
    }

    @PostMapping("/products/new")
    public String create(
            @Validated @ModelAttribute("productForm") ProductForm form,
            BindingResult result) {

        if (result.hasErrors()) {
            return "admin/product_new";
        }

        String imageUrl = null;
        MultipartFile imageFile = form.getImageFile();

        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = saveImageAndGetUrl(imageFile);
        }

        Product product = new Product();
        product.setName(form.getName());
        product.setCategoryId(form.getCategoryId());
        product.setSeason(form.getSeason());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setImageUrl(imageUrl);

        productsRepository.save(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/product_edit/{id}")
    public String showEdit(@PathVariable Integer id, Model model) {
        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + id));

        ProductForm form = new ProductForm();
        form.setId(product.getId());
        form.setName(product.getName());
        form.setCategoryId(product.getCategoryId());
        form.setSeason(product.getSeason());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());
        form.setImageUrl(product.getImageUrl());

        model.addAttribute("productForm", form);

        return "admin/product_edit";
    }

    @PostMapping("/product_update")
    public String update(
            @Validated @ModelAttribute("productForm") ProductForm form,
            BindingResult result) {

        if (result.hasErrors()) {
            return "admin/product_edit";
        }

        Product product = productsRepository.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + form.getId()));

        String imageUrl = product.getImageUrl();
        MultipartFile imageFile = form.getImageFile();

        if (imageFile != null && !imageFile.isEmpty()) {
            String newUrl = saveImageAndGetUrl(imageFile);
            if (newUrl != null && !newUrl.isBlank()) {
                imageUrl = newUrl;
            }
        }

        product.setName(form.getName());
        product.setCategoryId(form.getCategoryId());
        product.setSeason(form.getSeason());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setImageUrl(imageUrl);

        productsRepository.save(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/product_delete/{id}")
    public String delete(@PathVariable Integer id) {
        productsRepository.deleteById(id);
        return "redirect:/admin/products";
    }

    private String saveImageAndGetUrl(MultipartFile imageFile) {
        try {
            String contentType = imageFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return null;
            }

            String ext = guessExtension(contentType);
            if (ext.isBlank()) {
                return null;
            }

            String saveName = UUID.randomUUID().toString() + ext;

            Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
            Files.createDirectories(uploadDir);

            Path savePath = uploadDir.resolve(saveName).normalize();

            imageFile.transferTo(savePath.toFile());

            return "/uploads/" + saveName;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String guessExtension(String contentType) {
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> "";
        };
    }
}
package katachi.example.toretatebox.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.service.ProductsService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping("/top")
    public String showTopPage(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        String season = getCurrentSeason();
        Pageable pageable = PageRequest.of(page, 8);

        Page<Product> productPage = productsService.searchBySeason(season, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("season", season);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "top/top";
    }

    @GetMapping("/products")
    public String showProductsList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String season,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 8);
        Page<Product> productPage;

        if (isNotBlank(keyword)) {
            productPage = productsService.searchProducts(keyword, pageable);

            model.addAttribute("products", productPage.getContent());
            model.addAttribute("keyword", keyword);
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("season", season);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());

            return "products/list";
        }

        if (categoryId != null && isNotBlank(season)) {
            productPage = productsService.searchByCategoryAndSeason(categoryId, season, pageable);

        } else if (categoryId != null) {
            productPage = productsService.searchByCategory(categoryId, pageable);

        } else if (isNotBlank(season)) {
            productPage = productsService.searchBySeason(season, pageable);

        } else {
            productPage = productsService.findAllPage(pageable);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("season", season);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "products/list";
    }

    @GetMapping("/products/{id}")
    public String showProductDetail(
            @PathVariable Integer id,
            Model model) {

        Product product = productsService.findById(id);

        if (product == null) {
            return "error/404";
        }

        model.addAttribute("product", product);

        return "products/detail";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product) {
        productsService.save(product);
        return "redirect:/products";
    }

    private String getCurrentSeason() {
        int month = LocalDate.now().getMonthValue();

        if (month >= 3 && month <= 5) return "春";
        if (month >= 6 && month <= 8) return "夏";
        if (month >= 9 && month <= 11) return "秋";
        return "冬";
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}

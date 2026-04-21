package katachi.example.toretatebox.controller.product;

import java.time.LocalDate;
import java.util.Set;

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

    private static final Set<String> VALID_SEASONS =
            Set.of("春", "夏", "秋", "冬", "通年");

    @GetMapping("/top")
    public String showTopPage(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        if (page < 0) {
            page = 0;
        }

        String season = getCurrentSeason();
        Page<Product> productPage = getTopPageResult(page, season);

        if (page >= productPage.getTotalPages() && productPage.getTotalPages() > 0) {
            page = productPage.getTotalPages() - 1;
            productPage = getTopPageResult(page, season);
        }

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

        if (page < 0) {
            page = 0;
        }

        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        if (season != null && season.isBlank()) {
            season = null;
        }

        if (season != null && !VALID_SEASONS.contains(season)) {
            season = null;
        }

        if (categoryId != null && categoryId != 1 && categoryId != 2) {
            categoryId = null;
        }

        Page<Product> productPage = getProductsPageResult(keyword, categoryId, season, page);

        if (page >= productPage.getTotalPages() && productPage.getTotalPages() > 0) {
            page = productPage.getTotalPages() - 1;
            productPage = getProductsPageResult(keyword, categoryId, season, page);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("season", season);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "product/list";
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

        return "product/detail";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product) {
        productsService.save(product);
        return "redirect:/products";
    }

    private Page<Product> getTopPageResult(int page, String season) {
        Pageable pageable = PageRequest.of(page, 8);
        return productsService.searchBySeason(season, pageable);
    }

    private Page<Product> getProductsPageResult(String keyword, Integer categoryId, String season, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        if (isNotBlank(keyword)) {
            return productsService.searchProducts(keyword, pageable);

        } else if (categoryId != null && isNotBlank(season)) {
            return productsService.searchByCategoryAndSeason(categoryId, season, pageable);

        } else if (categoryId != null) {
            return productsService.searchByCategory(categoryId, pageable);

        } else if (isNotBlank(season)) {
            return productsService.searchBySeason(season, pageable);

        } else {
            return productsService.findAllPage(pageable);
        }
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
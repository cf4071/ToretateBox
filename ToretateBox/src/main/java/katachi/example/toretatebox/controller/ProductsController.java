package katachi.example.toretatebox.controller;

import java.time.LocalDate;
import java.util.List;

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

    // ============================
    // ✅ TOPページ（今が旬）
    // ============================
    @GetMapping("/top")
    public String showTopPage(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        String season = getCurrentSeason();

        Pageable pageable = PageRequest.of(page, 6);

        Page<Product> productPage =
                productsService.searchBySeason(season, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("season", season);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "top/top";
    }

    // ==================================
    // ✅ 食材一覧（カテゴリID＋旬＋検索）
    // ==================================
    @GetMapping("/products")
    public String showProductsList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String season,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 8);
        Page<Product> productPage;

        // キーワード検索
        if (keyword != null && !keyword.isBlank()) {

            List<Product> products =
                    productsService.searchProducts(keyword);

            model.addAttribute("products", products);
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 1);

            return "products/list";
        }

        // カテゴリ＋旬
        if (categoryId != null && isNotBlank(season)) {
            productPage = productsService
                    .searchByCategoryAndSeason(categoryId, season, pageable);

        // カテゴリのみ
        } else if (categoryId != null) {
            productPage = productsService
                    .searchByCategory(categoryId, pageable);

        // 旬のみ
        } else if (isNotBlank(season)) {
            productPage = productsService
                    .searchBySeason(season, pageable);

        // 全て
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

    // ============================
    // ✅ 商品詳細ページ
    // ============================
    @GetMapping("/products/{id}")
    public String showProductDetail(
            @PathVariable Integer id,
            Model model) {

        Product product = productsService.findById(id);

        if (product == null) {
            return "error/404";  // 任意：商品が存在しないとき
        }

        model.addAttribute("product", product);

        return "products/detail"; // detail.html
    }

    // ============================
    // ✅ 商品保存
    // ============================
    @PostMapping("/products/save")
    public String saveProduct(Product product) {
        productsService.save(product);
        return "redirect:/products";
    }

    // ============================
    // ✅ 商品削除
    // ============================
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productsService.deleteById(id);
        return "redirect:/products";
    }

    // ============================
    // ✅ 今の季節を自動判定
    // ============================
    private String getCurrentSeason() {

        int month = LocalDate.now().getMonthValue();

        if (month >= 3 && month <= 5) return "春";
        if (month >= 6 && month <= 8) return "夏";
        if (month >= 9 && month <= 11) return "秋";
        return "冬";
    }

    // null or 空文字チェック
    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}

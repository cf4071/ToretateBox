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

    // ✅ A-1：TOPページ（今が旬）
    @GetMapping("/top")
    public String showTopPage(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // ▼ 今の季節を自動判定
        String season = getCurrentSeason();

        // ▼ ページング（1ページ6件）
        Pageable pageable = PageRequest.of(page, 6);

        // ▼ 季節で商品取得（ページ対応）
        Page<Product> productPage =
                productsService.searchBySeason(season, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("season", season);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "top/top"; // templates/top.html
    }

    // ✅ B-1：食材一覧ページ（検索あり）
    @GetMapping("/products")
    public String showProductsList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // ▼ キーワード検索（ページングなし）
        if (keyword != null && !keyword.isBlank()) {
            List<Product> products =
                    productsService.searchProducts(keyword);

            model.addAttribute("products", products);
            model.addAttribute("keyword", keyword);

            return "products/list";
        }

        // ▼ 通常一覧（ページングあり）
        Pageable pageable = PageRequest.of(page, 9);
        List<Product> products = productsService.getAllProducts();

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", 1); // 全件表示なので1固定

        return "products/list"; // templates/products/list.html
    }

    // ✅ C-2：食材詳細ページ
    @GetMapping("/products/{id}")
    public String showProductDetail(
            @PathVariable Integer id,
            Model model) {

        Product product = productsService.getProductById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("商品が見つかりません"));

        model.addAttribute("product", product);

        return "products/detail"; // templates/products/detail.html
    }

    // ✅ 商品登録画面（管理用）
    @GetMapping("/products/new")
    public String showCreateForm(Model model) {

        model.addAttribute("product", new Product());

        return "products/form";
    }

    // ✅ 商品保存（新規・更新）
    @PostMapping("/products/save")
    public String saveProduct(Product product) {

        productsService.save(product);

        return "redirect:/products";
    }

    // ✅ 商品削除（管理用）
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {

        productsService.deleteById(id);

        return "redirect:/products";
    }

    // ✅ 今の季節を自動判定するメソッド
    private String getCurrentSeason() {

        int month = LocalDate.now().getMonthValue();

        if (month >= 3 && month <= 5) {
            return "春";
        } else if (month >= 6 && month <= 8) {
            return "夏";
        } else if (month >= 9 && month <= 11) {
            return "秋";
        } else {
            return "冬";
        }
    }
}

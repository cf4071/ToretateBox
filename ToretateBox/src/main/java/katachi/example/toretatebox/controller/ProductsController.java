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
    // ✅ A-1：TOPページ（今が旬）
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
 // ✅ B-1：食材一覧（カテゴリID＋旬＋検索）【修正版】
 // ==================================
 @GetMapping("/products")
 public String showProductsList(
         @RequestParam(required = false) String keyword,
         @RequestParam(required = false) Integer categoryId, // ✅ String → Integer に変更
         @RequestParam(required = false) String season,
         @RequestParam(defaultValue = "0") int page,
         Model model) {

     Pageable pageable = PageRequest.of(page, 10);
     Page<Product> productPage;

     // ✅ キーワード検索（最優先）
     if (keyword != null && !keyword.isBlank()) {

         List<Product> products =
                 productsService.searchProducts(keyword);

         model.addAttribute("products", products);
         model.addAttribute("keyword", keyword);
         model.addAttribute("currentPage", 0);
         model.addAttribute("totalPages", 1);

         return "products/list";
     }

     // ✅ カテゴリID＋旬 両方あり
     if (categoryId != null && isNotBlank(season)) {
         productPage = productsService
                 .searchByCategoryAndSeason(categoryId, season, pageable);

     // ✅ カテゴリIDのみ
     } else if (categoryId != null) {
         productPage = productsService
                 .searchByCategory(categoryId, pageable);

     // ✅ 旬のみ
     } else if (isNotBlank(season)) {
         productPage = productsService
                 .searchBySeason(season, pageable);

     // ✅ すべて
     } else {
         productPage = productsService.findAllPage(pageable);
     }

     model.addAttribute("products", productPage.getContent());
     model.addAttribute("categoryId", categoryId); // ✅ category → categoryId
     model.addAttribute("season", season);
     model.addAttribute("currentPage", page);
     model.addAttribute("totalPages", productPage.getTotalPages());

     return "products/list";
 }


    // ============================
    // ✅ 商品保存（新規・更新）
    // ============================
    @PostMapping("/products/save")
    public String saveProduct(Product product) {

        productsService.save(product);

        return "redirect:/products";
    }

    // ============================
    // ✅ 商品削除（管理用）
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

    // ✅ nullや空文字チェック用
    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}

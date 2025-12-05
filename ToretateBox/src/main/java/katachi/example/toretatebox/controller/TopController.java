package katachi.example.toretatebox.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.repository.ProductRepository;

@Controller
public class TopController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/top")
    public String showTop(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        int pageSize = 8; // 1ページに表示する商品数

        // ★ 今の季節（漢字）を取得
        String season = getCurrentSeasonKanji();

        Pageable pageable = PageRequest.of(page, pageSize);

        // ★ 今の季節の商品だけ取得
        Page<Product> productPage = productRepository.findBySeason(season, pageable);
        // ※ is_active を使いたい場合は findBySeasonAndIsActive を使用

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("season", season); // 画面に「春の旬！」など表示可能

        return "top/top"; // HTML のパス
    }

    /**
     * 現在の月から季節（春・夏・秋・冬）を判定して返す
     */
    private String getCurrentSeasonKanji() {
        int month = LocalDate.now().getMonthValue();

        if (3 <= month && month <= 5)  return "春";   // 3〜5月
        if (6 <= month && month <= 8)  return "夏";   // 6〜8月
        if (9 <= month && month <= 11) return "秋";   // 9〜11月
        return "冬";                                 // 12〜2月
    }
}

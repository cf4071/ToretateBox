package katachi.example.toretatebox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductsController {

    @GetMapping("/products")
    public String showProducts(Model model) {

        // TODO: ここに後で DB から食材一覧を取得する処理を追加
        // とりあえずダミー表示用
        model.addAttribute("dummy", "ok");

        return "products/products";  // templates/products/products.html
    }
}

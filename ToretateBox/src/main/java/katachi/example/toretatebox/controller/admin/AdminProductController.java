package katachi.example.toretatebox.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductController {

    private final ProductsRepository productsRepository;

    /**
     * 管理：食材一覧
     * GET /admin/products
     */
    @GetMapping("/products")
    public String list(Model model) {
        List<Product> products = productsRepository.findAll();
        model.addAttribute("products", products);
        return "admin/product_list";
    }

    /**
     * 管理：編集画面表示
     * GET /admin/product_edit/{id}
     */
    @GetMapping("/product_edit/{id}")
    public String showEdit(@PathVariable Integer id, Model model) {
        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + id));
        model.addAttribute("product", product);
        return "admin/product_edit";
    }

    /**
     * 管理：更新処理
     * POST /admin/product_update
     */
    @PostMapping("/product_update")
    public String update(@ModelAttribute Product product) {
        // id が入っていれば更新、入ってなければ新規扱いになります（JPAの動き）
        productsRepository.save(product);
        return "redirect:/admin/products";
    }

    /**
     * 管理：削除（※本当はPOST推奨。今はリンクから動かすためGETにしています）
     * GET /admin/product_delete/{id}
     */
    @GetMapping("/product_delete/{id}")
    public String delete(@PathVariable Integer id) {
        productsRepository.deleteById(id);
        return "redirect:/admin/products";
    }
}

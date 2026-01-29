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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import katachi.example.toretatebox.domain.model.Product;
import katachi.example.toretatebox.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductController {

    private final ProductsRepository productsRepository;

    /**
     * 管理：食材一覧（ページング）
     * GET /admin/products?page=0
     */
    @GetMapping("/products")
    public String list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ) {
        int size = 15; // 15件ずつ表示

        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> productPage = productsRepository.findAll(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());

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
     * 管理：更新処理（画像アップロード対応）
     * POST /admin/product_update
     *
     * HTMLのformに enctype="multipart/form-data" が必要
     * <input type="file" name="imageFile"> で受け取る
     */
    @PostMapping("/product_update")
    public String update(
            @ModelAttribute Product product,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile
    ) {
        // 既存データ取得（画像など「フォームに無い項目」を守るため）
        Product current = productsRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + product.getId()));

        // ===============================
        // 画像処理
        // ===============================
        if (imageFile == null || imageFile.isEmpty()) {
            // 画像が選ばれていない → 既存の画像URLを維持
            product.setImageUrl(current.getImageUrl());
        } else {
            try {
                String contentType = imageFile.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    product.setImageUrl(current.getImageUrl());
                } else {
                    String ext = guessExtension(contentType);
                    if (ext.isBlank()) {
                        product.setImageUrl(current.getImageUrl());
                    } else {
                        String saveName = UUID.randomUUID().toString() + ext;

                        // ✅ 404対策：保存先を絶対パスに固定（実行時カレントのズレを防ぐ）
                        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
                        Files.createDirectories(uploadDir);

                        Path savePath = uploadDir.resolve(saveName).normalize();

                        // ★ どこに保存したか確認できるログ（困った時用）
                        System.out.println("[UPLOAD] savePath = " + savePath);

                        imageFile.transferTo(savePath.toFile());

                        // DBに保存するURL（ブラウザから参照するパス）
                        product.setImageUrl("/uploads/" + saveName);
                    }
                }
            } catch (Exception e) {
                // 保存に失敗したら既存維持
                product.setImageUrl(current.getImageUrl());

                // ★ エラー原因をログで見たい場合（任意）
                e.printStackTrace();
            }
        }

        productsRepository.save(product);
        return "redirect:/admin/products";
    }

    /**
     * 管理：削除
     * GET /admin/product_delete/{id}
     * ※本当はPOST推奨。今はリンクで動かすためGETにしています。
     */
    @GetMapping("/product_delete/{id}")
    public String delete(@PathVariable Integer id) {
        productsRepository.deleteById(id);
        return "redirect:/admin/products";
    }

    // Content-Typeから拡張子をざっくり推測（最低限）
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

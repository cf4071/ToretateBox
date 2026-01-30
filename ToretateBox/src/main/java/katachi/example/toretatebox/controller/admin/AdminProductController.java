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

    /**
     * 管理：食材一覧（ページング）
     * GET /admin/products?page=0
     */
    @GetMapping("/products")
    public String list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ) {
        int size = 15;

        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> productPage = productsRepository.findAll(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "admin/product_list";
    }

    /**
     * 管理：新規登録画面（本命URL）
     * GET /admin/products_new
     */
    @GetMapping("/products_new")
    public String showNew(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "admin/product_new";
    }

    /**
     * ★追加：新規登録画面（別名URL）
     * GET /admin/product_new
     *
     * 既存リンクが /admin/product_new を指していても404にならないようにする
     */
    @GetMapping("/product_new")
    public String showNewAlias(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "admin/product_new";
    }

    /**
     * 管理：新規登録処理（画像アップロード対応）
     * POST /admin/products/new
     */
    @PostMapping("/products/new")
    public String create(
            @Validated @ModelAttribute("productForm") ProductForm form,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "admin/product_new";
        }

        // 画像が選ばれていれば保存してURLを作る
        String imageUrl = null;
        MultipartFile imageFile = form.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = saveImageAndGetUrl(imageFile);
        }

        // form -> entity
        Product product = new Product();
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setCategoryId(form.getCategoryId());
        product.setSeason(form.getSeason());
        product.setImageUrl(imageUrl);

        productsRepository.save(product);

        // ★修正：一覧のURLに戻す（テンプレ名ではない）
        return "redirect:/admin/products";
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

        // 任意：将来的にフォーム運用にするなら使える（今はHTMLがproductでもOK）
        ProductForm form = new ProductForm();
        form.setId(product.getId());
        form.setName(product.getName());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());
        form.setCategoryId(product.getCategoryId());
        form.setSeason(product.getSeason());
        form.setImageUrl(product.getImageUrl());
        model.addAttribute("productForm", form);

        return "admin/product_edit";
    }

    /**
     * 管理：更新処理（画像アップロード対応）
     * POST /admin/product_update
     */
    @PostMapping("/product_update")
    public String update(
            @ModelAttribute Product product,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile
    ) {
        // 既存データ取得（画像など「フォームに無い項目」を守るため）
        Product current = productsRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + product.getId()));

        // 画像URLは基本「既存維持」
        String imageUrl = current.getImageUrl();

        // 画像が選ばれていれば差し替え
        if (imageFile != null && !imageFile.isEmpty()) {
            String newUrl = saveImageAndGetUrl(imageFile);
            if (newUrl != null && !newUrl.isBlank()) {
                imageUrl = newUrl;
            }
        }

        product.setImageUrl(imageUrl);

        productsRepository.save(product);

        // ★修正：一覧のURLに戻す
        return "redirect:/admin/products";
    }

    /**
     * 管理：削除
     * GET /admin/product_delete/{id}
     */
    @GetMapping("/product_delete/{id}")
    public String delete(@PathVariable Integer id) {
        productsRepository.deleteById(id);

        // ★修正：一覧のURLに戻す
        return "redirect:/admin/products";
    }

    // ==========================================================
    // 画像保存：uploadsフォルダへ保存し、/uploads/xxx を返す
    // ==========================================================
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

            // 保存先（絶対パス）
            Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
            Files.createDirectories(uploadDir);

            Path savePath = uploadDir.resolve(saveName).normalize();

            System.out.println("[UPLOAD] savePath = " + savePath);

            imageFile.transferTo(savePath.toFile());

            // DBに保存するURL（ブラウザから参照するパス）
            return "/uploads/" + saveName;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Content-Typeから拡張子を推測
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

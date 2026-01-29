package katachi.example.toretatebox.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 実行時のカレントに依存するので、絶対パスにしてからURI化（Windowsでも安全）
        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        String location = uploadDir.toUri().toString();   // 例: file:/C:/.../uploads/

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }
}

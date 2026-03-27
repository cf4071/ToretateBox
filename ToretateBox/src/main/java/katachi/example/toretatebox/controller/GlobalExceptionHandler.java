package katachi.example.toretatebox.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {

        // ログ確認用（開発中）
        e.printStackTrace();

        // エラーメッセージ
        model.addAttribute("errorMessage",
                "申し訳ございません。エラーが発生しました。時間をおいて再度お試しください。");

        return "error/system_error";
    }
}
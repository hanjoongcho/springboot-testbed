package io.github.hanjoongcho.file;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileUploadController {
    
    @GetMapping("/upload-progrss")
    public String uploadWithProgress(Model model) {
        return "uploadWithProgress";
    }
}

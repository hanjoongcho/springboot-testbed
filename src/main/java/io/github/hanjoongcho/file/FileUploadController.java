package io.github.hanjoongcho.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import io.github.hanjoongcho.ckeditor.CKEditorController;

@Controller
public class FileUploadController {
    private Logger logger = LoggerFactory.getLogger(CKEditorController.class);
    
    @Value("${ckeditor.storage.image-path}")
    private String ckeditorStorageImagePath;
    
    @Value("${ckeditor.storage.service-url:}")
    private String ckeditorStorageServiceUrl;
    
    @GetMapping("/upload/form")
    public String uploadWithProgress(Model model) {
        return "uploadWithProgress";
    }
    
    @CrossOrigin
    @RequestMapping(value = "/upload/files", method = RequestMethod.POST)
    @ResponseBody
    public HashMap<String, Object> uploadImage(@RequestParam("upload") List<MultipartFile> files, HttpServletRequest request, HttpServletResponse response) {
        String name = "";
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String rootFilePath = ckeditorStorageImagePath;
        File rootFile = new File(rootFilePath);
        if (!rootFile.exists()){
            rootFile.mkdir();
        }
        
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    logger.info(String.format("Original file name is %s", file.getOriginalFilename()));
                    fileNames.add(file.getOriginalFilename());
                    file.transferTo(new File(rootFile, file.getOriginalFilename()));
                } catch (Exception e) {
                    logger.info("You failed to upload " + name + " => " + e.getMessage());
                }
            } else {
                logger.info("You failed to upload " + name + " because the file was empty.");
            }
        }
        resultMap.put("fileCount", fileNames.size());
        resultMap.put("fileNames", fileNames);
        return resultMap;
    }
}

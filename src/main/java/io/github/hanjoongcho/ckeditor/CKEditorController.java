package io.github.hanjoongcho.ckeditor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CKEditorController {

    private String ckeditorStorageImagePath = "C:/temp";
    private Logger logger = LoggerFactory.getLogger(CKEditorController.class);
    
    @CrossOrigin
    @RequestMapping(value = "/upload/image", method = RequestMethod.POST)
    @ResponseBody
    public HashMap<String, Object> uploadImage(@RequestParam("upload") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        String name = "";
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        if (!file.isEmpty()) {
            try {
                String fileClientName = file.getOriginalFilename();
                String rootFilePath=ckeditorStorageImagePath+"\\images\\";

                File rootFile=new File(rootFilePath);
                if (!rootFile.exists()){
                    rootFile.mkdir();
                }

                String pathName = rootFile +File.separator+ fileClientName;
                File newfile = new File(pathName);
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(newfile));
                stream.write(bytes);
                stream.close();

                // FIXME hard code result map
                String fileUrl = "https://user-images.githubusercontent.com/156149/47722540-ed367c00-dc52-11e8-97d4-b7eafbb6e495.png";
                resultMap.put("fileName", "sample01.PNG");
                resultMap.put("uploaded", 1);
                resultMap.put("url", fileUrl);
            } catch (Exception e) {
                logger.info("You failed to upload " + name + " => " + e.getMessage());
            }
        } else {
            logger.info("You failed to upload " + name + " because the file was empty.");
        }
        return resultMap;
    }
}

package io.github.hanjoongcho.ckeditor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CKEditorController {
    private Logger logger = LoggerFactory.getLogger(CKEditorController.class);
    
    @Value("${ckeditor.storage.image-path}")
    private String ckeditorStorageImagePath;
    
    @Value("${ckeditor.storage.service-url:}")
    private String ckeditorStorageServiceUrl;
    
    @CrossOrigin
    @RequestMapping(value = "/upload/image", method = RequestMethod.POST)
    @ResponseBody
    public HashMap<String, Object> uploadImage(@RequestParam("upload") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        String name = "";
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        if (!file.isEmpty()) {
            try {
                String fileClientName = file.getOriginalFilename();
                logger.info(String.format("Original file name is %s", fileClientName));
                String rootFilePath = ckeditorStorageImagePath;
                File rootFile = new File(rootFilePath);
                if (!rootFile.exists()){
                    rootFile.mkdir();
                }

                int fileSequence = rootFile.listFiles().length + 1;
                String pathName = rootFile + File.separator+ fileSequence;
                file.transferTo(new File(pathName));
                
                // FIXME hard code result map
                String fileUrl = ckeditorStorageServiceUrl + "/" + fileSequence;
                resultMap.put("fileName", fileClientName);
                resultMap.put("uploaded", 1); // success: 1, fail: 0
                resultMap.put("url", fileUrl);
            } catch (Exception e) {
                logger.info("You failed to upload " + name + " => " + e.getMessage());
            }
        } else {
            logger.info("You failed to upload " + name + " because the file was empty.");
        }
        return resultMap;
    }
    
    /**
     * FIXME: Return application mime type dynamically.
     * 
     * @param imageId
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("imageId") String imageId) throws IOException {
        InputStream is = FileUtils.openInputStream(new File(ckeditorStorageImagePath, imageId));
        return IOUtils.toByteArray(is);
    }
}

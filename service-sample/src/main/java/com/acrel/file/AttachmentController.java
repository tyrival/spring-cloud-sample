package com.acrel.file;

import com.acrel.api.ControllerName;
import com.acrel.entity.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(ControllerName.SAMPLE_ATTACHMENT)
public class AttachmentController {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result upload(@RequestPart(value = "file") MultipartFile file) {
        log.info("原始文件名：", file.getName());
        return new Result();
    }

}

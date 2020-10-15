package com.acrel.entity.file;

import com.acrel.entity.base.Base;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 附件类
 * @Author: ZhouChenyu
 */
@Data
public class Attachment extends Base {

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件完整名称，包含后缀
     */
    private String fullName;

    /**
     * 后缀名
     */
    private String extensionName;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 储存路径
     */
    private String absolutePath;

    /**
     * 相对路径
     */
    private String relativePath;

    public Attachment() {}

    public Attachment(MultipartFile file) {
        this.fullName = file.getOriginalFilename();
        int dotIndex = this.fullName.lastIndexOf(".");
        this.name = this.fullName.substring(0, dotIndex);
        this.extensionName = this.fullName.substring(dotIndex + 1);
        this.size = file.getSize();
    }
}

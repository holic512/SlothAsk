/**
 * File Name: ServiceImageFeign.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-06
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.config.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "service-image")
public interface ServiceImageFeign {

    @GetMapping("/images/preview/{fileName}")
    String getPreviewUrl(@PathVariable String fileName);

    /**
     * 上传图片接口
     *
     * @param file 要上传的图片文件
     * @return 返回包含文件名和预览URL的Map
     */
    @PostMapping(value = "/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, String> uploadImage(@RequestPart("file") MultipartFile file);

    /**
     * 删除指定图片
     *
     * @param fileName 要删除的图片文件名
     * @return 返回空响应
     */
    @DeleteMapping("/images/{fileName}")
    ResponseEntity<Void> deleteImage(@PathVariable String fileName);

}

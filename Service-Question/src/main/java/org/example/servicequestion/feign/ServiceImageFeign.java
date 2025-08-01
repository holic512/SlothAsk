/**
 * File Name: ServiceImageFeign.java
 * Description: 图片服务Feign客户端接口，用于与图片微服务进行远程调用
 * Author: holic512
 * Created Date: 2025-02-06
 * Version: 1.0
 * Usage:
 * 提供图片上传、预览、删除和批量获取预览URL等功能，通过OpenFeign实现微服务间的通信
 */
package org.example.servicequestion.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
     * response.put("fileName", fileName);
     * response.put("previewUrl", minioService.getPreviewUrl(fileName));
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

    /**
     * 批量获取图片预览 URL
     *
     * @param fileNames 图片文件名列表
     * @return 文件名 -> 预览URL 的映射
     */
    @PostMapping("/images/preview/batch")
    Map<String, String> getPreviewUrls(@RequestBody List<String> fileNames);

}

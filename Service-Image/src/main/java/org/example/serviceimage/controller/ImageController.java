package org.example.serviceimage.controller;

import lombok.RequiredArgsConstructor;
import org.example.serviceimage.service.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final MinioService minioService;

    /**
     * 上传图片接口
     *
     * @param file 要上传的图片文件
     * @return 返回包含文件名和预览URL的Map
     */
    @PostMapping("/upload")
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) {
        String fileName = minioService.uploadImage(file);
        Map<String, String> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("previewUrl", minioService.getPreviewUrl(fileName));
        return response;
    }

    /**
     * 下载指定图片
     *
     * @param fileName 图片文件名
     * @return 返回图片文件流
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadImage(@PathVariable String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(minioService.downloadImage(fileName)));
    }

    /**
     * 删除指定图片
     *
     * @param fileName 要删除的图片文件名
     * @return 返回空响应
     */
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteImage(@PathVariable String fileName) {
        minioService.deleteImage(fileName);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取图片预览URL（简化版，无需参数验证）
     * 直接通过文件名获取预览链接
     */
    @GetMapping("/preview/{fileName}")
    public String getPreviewUrl(@PathVariable String fileName) {
        return minioService.getPreviewUrl(fileName);
    }

    /**
     * 获取所有图片列表
     *
     * @return 返回所有图片文件名列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<String>> listImages() {
        return ResponseEntity.ok(minioService.listImages());
    }

    /**
     * 批量获取图片预览 URL
     *
     * @param fileNames 图片文件名列表
     * @return 文件名 -> 预览URL 的映射
     */
    @PostMapping("/preview/batch")
    public Map<String, String> getPreviewUrls(@RequestBody List<String> fileNames) {
        return minioService.getPreviewUrls(fileNames);
    }

} 
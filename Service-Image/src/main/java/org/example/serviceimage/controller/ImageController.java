package org.example.serviceimage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.serviceimage.service.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final MinioService minioService;

    /**
     * 上传图片
     * @param file 图片文件
     * @return 包含文件名和预览URL的响应
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String fileName = minioService.uploadImage(file);
        Map<String, String> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("previewUrl", minioService.getPreviewUrl(fileName));
        return ResponseEntity.ok(response);
    }

    /**
     * 下载图片
     * @param fileName 文件名
     * @return 图片文件流
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadImage(@PathVariable String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(minioService.downloadImage(fileName)));
    }

    /**
     * 删除图片
     * @param fileName 文件名
     * @return 空响应
     */
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteImage(@PathVariable String fileName) {
        minioService.deleteImage(fileName);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取图片预览URL
     * @param fileName 文件名
     * @return 预览URL
     */
    @GetMapping("/preview/{fileName}")
    public ResponseEntity<Map<String, String>> getPreviewUrl(@PathVariable String fileName) {
        Map<String, String> response = new HashMap<>();
        response.put("previewUrl", minioService.getPreviewUrl(fileName));
        return ResponseEntity.ok(response);
    }

    /**
     * 获取图片列表
     * @return 图片名称列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<String>> listImages() {
        return ResponseEntity.ok(minioService.listImages());
    }
} 
package org.example.serviceimage.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.example.serviceimage.config.MinioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.SneakyThrows;

@Service
public class MinioService {
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    // 缓存预览URL的过期时间（毫秒）
    private static final long URL_EXPIRY = TimeUnit.HOURS.toMillis(1);
    
    @Autowired
    public MinioService(MinioClient minioClient, MinioConfig minioConfig) {
        this.minioClient = minioClient;
        this.minioConfig = minioConfig;
        createBucketIfNotExists();
    }

    /**
     * 检查并创建存储桶
     */
    @SneakyThrows
    private void createBucketIfNotExists() {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioConfig.getBucketName())
                .build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .build());
        }
    }

    /**
     * 上传图片
     * @param file 图片文件
     * @return 文件名
     */
    @SneakyThrows
    public String uploadImage(MultipartFile file) {
        // 生成唯一文件名
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        
        // 上传文件到Minio
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
        
        return fileName;
    }

    /**
     * 生成唯一文件名
     */
    private String generateUniqueFileName(String originalFilename) {
        return System.currentTimeMillis() + "_" + originalFilename;
    }

    /**
     * 下载图片
     * @param fileName 文件名
     * @return 图片输入流
     */
    @SneakyThrows
    public InputStream downloadImage(String fileName) {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .build());
    }

    /**
     * 删除图片
     * @param fileName 文件名
     */
    @SneakyThrows
    public void deleteImage(String fileName) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .build());
    }

    /**
     * 获取图片预览URL
     * @param fileName 文件名
     * @return 预览URL
     */
    @SneakyThrows
    @Cacheable(value = "previewUrls", key = "#fileName", unless = "#result == null")
    public String getPreviewUrl(String fileName) {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .method(Method.GET)
                .expiry(1, TimeUnit.HOURS)
                .build());
    }

    /**
     * 获取图片列表
     * @return 图片名称列表
     */
    @SneakyThrows
    public List<String> listImages() {
        List<String> imageList = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(minioConfig.getBucketName())
                .build());
        
        for (Result<Item> result : results) {
            imageList.add(result.get().objectName());
        }
        return imageList;
    }
} 
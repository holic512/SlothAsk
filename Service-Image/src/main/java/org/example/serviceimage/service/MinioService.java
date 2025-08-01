package org.example.serviceimage.service;

import io.minio.*;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.example.serviceimage.config.MinioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        setBucketPolicy();
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
     *
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
     *
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
     *
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
     * 获取图片预览URL（永久公共链接）
     *
     * @param fileName 文件名
     * @return 预览URL
     */
    @Cacheable(value = "previewUrls", key = "#fileName", unless = "#result == null")
    public String getPreviewUrl(String fileName) {
        // 优先使用代理地址，如果没有配置则使用默认endpoint
        String baseUrl = (minioConfig.getProxyUrl() != null && !minioConfig.getProxyUrl().isEmpty()) 
                ? minioConfig.getProxyUrl() 
                : minioConfig.getEndpoint();
        
        // 构建永久公共访问URL
        return String.format("%s/%s/%s",
                baseUrl.replaceAll("/$", ""),  // 移除末尾的斜杠
                minioConfig.getBucketName(),
                fileName
        );
    }

    /**
     * 初始化时设置存储桶的访问策略为公开读
     */
    @SneakyThrows
    private void setBucketPolicy() {
        String bucketName = minioConfig.getBucketName();
        String policy = """
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Effect": "Allow",
                            "Principal": "*",
                            "Action": ["s3:GetObject"],
                            "Resource": ["arn:aws:s3:::%s/*"]
                        }
                    ]
                }
                """.formatted(bucketName);

        minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(policy)
                        .build()
        );
    }

    /**
     * 获取图片列表
     *
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

    /**
     * 批量获取图片预览URL（永久公共链接）
     *
     * @param fileNames 文件名列表
     * @return 文件名 -> 预览URL 的映射
     */
    @Cacheable(value = "previewUrlsBatch", key = "#fileNames.hashCode()", unless = "#result == null || #result.isEmpty()")
    public Map<String, String> getPreviewUrls(List<String> fileNames) {
        if (fileNames == null || fileNames.isEmpty()) {
            return Collections.emptyMap();
        }

        // 优先使用代理地址，如果没有配置则使用默认endpoint
        String baseUrl = (minioConfig.getProxyUrl() != null && !minioConfig.getProxyUrl().isEmpty()) 
                ? minioConfig.getProxyUrl() 
                : minioConfig.getEndpoint();
        baseUrl = baseUrl.replaceAll("/$", "");
        String bucket = minioConfig.getBucketName();

        String finalBaseUrl = baseUrl;
        return fileNames.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(
                        fileName -> fileName,
                        fileName -> String.format("%s/%s/%s", finalBaseUrl, bucket, fileName)
                ));
    }


}
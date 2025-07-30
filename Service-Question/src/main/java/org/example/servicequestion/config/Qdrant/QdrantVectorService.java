/**
 * File Name: QdrantVectorService.java
 * Description: Qdrant向量数据库服务工具类，提供向量插入、批量插入和相似度查询功能
 * Author: holic512
 * Created Date: 2025-01-27
 * Version: 1.0
 * Usage:
 * 提供向量数据的CRUD操作，支持单个和批量插入，以及基于相似度的查询功能
 */
package org.example.servicequestion.config.Qdrant;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections.Distance;
import io.qdrant.client.grpc.Collections.VectorParams;
import io.qdrant.client.grpc.Points.PointStruct;
import io.qdrant.client.grpc.Points.ScoredPoint;
import io.qdrant.client.grpc.Points.SearchPoints;
import io.qdrant.client.grpc.Points.WithPayloadSelector;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;

@Slf4j
@Service
public class QdrantVectorService {

    @Autowired
    private QdrantClient qdrantClient;

    /**
     * 固定的集合名称
     */
    @Value("${qdrant.collection.name:question_vectors}")
    private String collectionName;

    /**
     * 向量维度
     */
    @Value("${qdrant.vector.dimension:1024}")
    private int vectorDimension;

    // Getter和Setter方法
    /**
     * 相似度查询阈值（可配置）
     */
    @Setter
    @Getter
    private double similarityThreshold = 0.3;

    /**
     * 查询结果上限（可配置）
     */
    @Setter
    @Getter
    private int queryLimit = 50;

    /**
     * 初始化集合
     */
    @PostConstruct
    public void initializeCollection() {
        try {
            // 检查集合是否存在
            Boolean existsResponse = qdrantClient.collectionExistsAsync(collectionName).get();

            if (!existsResponse) {
                // 使用简化的API创建集合
                qdrantClient.createCollectionAsync(collectionName,
                                VectorParams.newBuilder()
                                        .setDistance(Distance.Cosine)
                                        .setSize(vectorDimension)
                                        .build())
                        .get();
                log.info("成功创建Qdrant集合: {}", collectionName);
            } else {
                log.info("Qdrant集合已存在: {}", collectionName);
            }
        } catch (Exception e) {
            log.error("初始化Qdrant集合失败: {}", e.getMessage(), e);
            throw new RuntimeException("初始化Qdrant集合失败", e);
        }
    }

    /**
     * 插入单个向量
     *
     * @param id     文档ID
     * @param title  文档标题
     * @param vector 向量数据
     * @return 是否插入成功
     */
    public boolean insertVector(String id, String title, List<Float> vector) {
        try {
            // 使用静态工厂方法简化代码
            PointStruct point = PointStruct.newBuilder()
                    .setId(id(Long.parseLong(id)))
                    .setVectors(vectors(vector))
                    .putAllPayload(Map.of(
                            "title", value(title),
                            "id", value(id)
                    ))
                    .build();

            // 使用简化的upsert方法
            qdrantClient.upsertAsync(collectionName, List.of(point)).get();
            log.debug("成功插入向量，ID: {}, 标题: {}", id, title);
            return true;
        } catch (Exception e) {
            log.error("插入向量失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 批量插入向量
     *
     * @param vectorData 向量数据列表
     * @return 成功插入的数量
     */
    public int batchInsertVectors(List<VectorData> vectorData) {
        try {
            // 使用静态工厂方法简化代码
            List<PointStruct> points = vectorData.stream()
                    .map(data -> PointStruct.newBuilder()
                            .setId(id(Long.parseLong(data.getId())))
                            .setVectors(vectors(data.getVector()))
                            .putAllPayload(Map.of(
                                    "title", value(data.getTitle()),
                                    "id", value(data.getId())
                            ))
                            .build())
                    .collect(Collectors.toList());

            // 使用简化的upsert方法
            qdrantClient.upsertAsync(collectionName, points).get();
            log.info("成功批量插入 {} 个向量", vectorData.size());
            return vectorData.size();
        } catch (Exception e) {
            log.error("批量插入向量失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 相似度查询
     *
     * @param queryVector 查询向量
     * @return 相似度查询结果列表
     */
    public List<SimilarityResult> searchSimilarVectors(List<Float> queryVector) {
        return searchSimilarVectors(queryVector, similarityThreshold, queryLimit);
    }

    /**
     * 相似度查询（可自定义阈值和限制）
     *
     * @param queryVector 查询向量
     * @param threshold   相似度阈值
     * @param limit       结果数量限制
     * @return 相似度查询结果列表
     */
    public List<SimilarityResult> searchSimilarVectors(List<Float> queryVector, double threshold, int limit) {
        try {
            // 使用简化的搜索API
            SearchPoints searchPoints = SearchPoints.newBuilder()
                    .setCollectionName(collectionName)
                    .addAllVector(queryVector)
                    .setLimit(limit)
                    .setScoreThreshold((float) threshold)
                    .setWithPayload(WithPayloadSelector.newBuilder().setEnable(true).build())
                    .build();

            List<ScoredPoint> points = qdrantClient.searchAsync(searchPoints).get();

            return points.stream()
                    .map(scoredPoint -> {
                        String id = scoredPoint.getPayloadMap().get("id").getStringValue();
                        String title = scoredPoint.getPayloadMap().get("title").getStringValue();
                        double score = scoredPoint.getScore();
                        return new SimilarityResult(id, title, score);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("相似度查询失败: {}", e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * 删除向量
     *
     * @param id 文档ID
     * @return 是否删除成功
     */
    public boolean deleteVector(String id) {
        try {
            // 使用简化的删除API
            qdrantClient.deleteAsync(collectionName, List.of(id(Long.parseLong(id)))).get();
            log.debug("成功删除向量，ID: {}", id);
            return true;
        } catch (Exception e) {
            log.error("删除向量失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return false;
        }
    }


    /**
     * 向量数据实体类
     */
    @Data
    public static class VectorData {
        private String id;
        private String title;
        private List<Float> vector;

        public VectorData() {
        }

        public VectorData(String id, String title, List<Float> vector) {
            this.id = id;
            this.title = title;
            this.vector = vector;
        }
    }

    /**
     * 相似度查询结果实体类
     */
    @Data
    public static class SimilarityResult {
        private String id;
        private String title;
        private double score;

        public SimilarityResult() {
        }

        public SimilarityResult(String id, String title, double score) {
            this.id = id;
            this.title = title;
            this.score = score;
        }
    }


}
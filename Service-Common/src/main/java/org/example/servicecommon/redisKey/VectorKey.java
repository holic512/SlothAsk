/**
 * File Name: VectorKey.java
 * Description: 向量化相关的Redis Key常量
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * 存储向量化处理相关的Redis Key常量
 */
package org.example.servicecommon.redisKey;

public class VectorKey {
    
    /**
     * 向量化处理锁的Key
     */
    public static final String VECTORIZATION_LOCK = CommonKey.KEY + "Vector:Lock:Batch";
    
    /**
     * 最后更新时间的Key
     */
    public static final String LAST_UPDATE_TIME = CommonKey.KEY + "Vector:LastUpdateTime";
    
    /**
     * 向量化进度的Key
     */
    public static final String VECTORIZATION_PROGRESS = CommonKey.KEY + "Vector:Progress";
}
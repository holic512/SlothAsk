/**
 * File Name: VipType.java
 * Description: VIP类型枚举
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 */
package org.example.servicevip.enums;

/**
 * VIP类型枚举
 */
public enum VipType {
    
    /**
     * 月付VIP
     */
    MONTHLY(1, "月付VIP"),
    
    /**
     * 永久VIP
     */
    PERMANENT(2, "永久VIP");
    
    private final int code;
    private final String description;
    
    VipType(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 根据代码获取VIP类型
     * @param code 类型代码
     * @return VIP类型枚举，如果找不到则返回null
     */
    public static VipType getByCode(int code) {
        for (VipType vipType : VipType.values()) {
            if (vipType.getCode() == code) {
                return vipType;
            }
        }
        return null;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
/**
 * File Name: FavQuestionListDTO.java
 * Description: 收藏题目列表数据传输对象
 * Author: holic512
 * Created Date: 2025-04-29
 * Version: 1.0
 */
package org.example.servicequestion.user.favQuestion.dto;

import java.util.List;

/**
 * 收藏题目列表DTO
 */
public class FavQuestionListDTO {

    /**
     * 收藏题目列表
     */
    private List<FavQuestionDTO> list;
    
    /**
     * 总记录数
     */
    private int totalCount;
    
    /**
     * 总页数
     */
    private int totalPages;
    
    /**
     * 当前页码
     */
    private int currentPage;

    public FavQuestionListDTO() {
    }

    public FavQuestionListDTO(List<FavQuestionDTO> list, int totalCount, int totalPages, int currentPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    public List<FavQuestionDTO> getList() {
        return list;
    }

    public void setList(List<FavQuestionDTO> list) {
        this.list = list;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
} 
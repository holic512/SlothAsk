/**
 * File Name: HistoryQuestionDTO.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-04-16
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.user.history.dto;

import lombok.Data;

import java.util.List;

@Data
public class HistoryQuestionDTO {
    List<UserHistoryDTO> userHistoryList;
    List<TagSimpleDTO> tagSimpleList;

    public HistoryQuestionDTO(List<UserHistoryDTO> historyList, List<TagSimpleDTO> tags) {
        this.userHistoryList = historyList;
        this.tagSimpleList = tags;
    }
}

package org.example.serviceuser.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "service-question")
public interface QuestionFeign {
    
    /**
     * 获取问题信息
     * @param questionId 问题ID
     * @return 包含问题虚拟ID和标题的Map
     */
    @GetMapping("/question/profile/questionUidAndTitle/{questionId}")
    Map<String, String> getQuestionUidAndTitle(@PathVariable("questionId") Long questionId);

    /**
     * 获取问题信息
     * @param questionId 问题ID
     * @return 包含问题虚拟ID,标题,标签,访问量的Map
     */
    @GetMapping("/question/profile/questionUidAndTitleAndTags/{questionId}")
    Map<String, Object> getQuestionUidAndTitleAndTags(@PathVariable("questionId") Long questionId);
}
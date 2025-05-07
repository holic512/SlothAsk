package org.example.servicequestion.user.questionBank.controller;


import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.questionBank.service.GetQuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 题库获取控制器
 * 负责处理与题库相关的所有查询请求
 */
@RestController
@RequestMapping("/user/questionBank")
public class GetQuestionBankController {

    private final GetQuestionBankService getQuestionBankService;

    /**
     * 构造函数注入服务依赖
     * @param getQuestionBankService 题库获取服务
     */
    @Autowired
    public GetQuestionBankController(GetQuestionBankService getQuestionBankService) {
        this.getQuestionBankService = getQuestionBankService;
    }


    /**
     * 根据项目ID获取分类列表
     * @param projectId 项目ID
     * @return 包含分类列表的API响应
     */
    @GetMapping("/categoriesByProjectId/{projectId}")
    public ApiResponse GetCategoriesByProjectId(@PathVariable Long projectId){
        return new ApiResponse(200, "获取题库信息成功", getQuestionBankService.getCategoriesByProjectId(projectId));
    }
    
    /**
     * 根据项目ID分页获取分类列表
     * @param projectId 项目ID
     * @param page 页码，默认为1
     * @param pageSize 每页大小，默认为10
     * @return 包含分页分类列表的API响应
     */
    @GetMapping("/categoriesByProjectIdPaged/{projectId}")
    public ApiResponse GetCategoriesByProjectIdPaged(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return new ApiResponse(200, "获取题库信息成功", 
                getQuestionBankService.getCategoriesByProjectIdPaged(projectId, page, pageSize));
    }

    /**
     * 获取所有项目列表
     * @return 包含项目列表的API响应
     */
    @GetMapping("/project")
    public ApiResponse GetProjects(){
        return new ApiResponse(200, "获取项目信息成功", getQuestionBankService.getProjects());
    }

    /**
     * 根据分类ID获取题目数量
     * @param categoryId 分类ID
     * @return 包含题目数量的API响应
     */
    @GetMapping("/count/{categoryId}")
    public ApiResponse GetCountByCategoryId(@PathVariable Long categoryId){
        return new ApiResponse(200, "获取题库数量成功", getQuestionBankService.getCountByCategoryId(categoryId));
    }

    /**
     * 根据分类ID获取分类详情
     * @param categoryId 分类ID
     * @return 包含分类详情的API响应
     */
    @GetMapping("/categoryById/{categoryId}")
    public ApiResponse GetCategoryById(@PathVariable Long categoryId){
        return new ApiResponse(200, "获取题库信息成功", getQuestionBankService.getCategoryById(categoryId));
    }

    /**
     * 根据分类ID获取问题列表
     * @param categoryId 分类ID
     * @return 包含问题列表的API响应
     */
    @GetMapping("/questions/{categoryId}")
    public ApiResponse GetQuestionsByCategoryId(@PathVariable Long categoryId){
        return new ApiResponse(200, "获取题库信息成功", getQuestionBankService.getQuestionsByCategoryId(categoryId));
    }
}

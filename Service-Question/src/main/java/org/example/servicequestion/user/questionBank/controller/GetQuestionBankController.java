package org.example.servicequestion.user.questionBank.controller;



import jakarta.annotation.Resource;
import org.example.servicequestion.config.ApiResponse.ApiResponse;
import org.example.servicequestion.user.questionBank.service.GetQuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/questionBank")
public class GetQuestionBankController {

    private final GetQuestionBankService getQuestionBankService;

    @Autowired
    public GetQuestionBankController(GetQuestionBankService getQuestionBankService) {
        this.getQuestionBankService = getQuestionBankService;
    }


    @GetMapping("/categoriesByProjectId/{projectId}")
    public ApiResponse GetCategoriesByProjectId(@PathVariable Long projectId){
        return new ApiResponse(200, "获取题库信息成功", getQuestionBankService.getCategoriesByProjectId(projectId));
    }

    @GetMapping("/project")
    public ApiResponse GetProjects(){
        return new ApiResponse(200, "获取项目信息成功", getQuestionBankService.getProjects());
    }

    @GetMapping("/count/{categoryId}")
    public ApiResponse GetCountByCategoryId(@PathVariable Long categoryId){
        return new ApiResponse(200, "获取题库数量成功", getQuestionBankService.getCountByCategoryId(categoryId));
    }

    @GetMapping("/categoryById/{categoryId}")
    public ApiResponse GetCategoryById(@PathVariable Long categoryId){
        return new ApiResponse(200, "获取题库信息成功", getQuestionBankService.getCategoryById(categoryId));
    }

    @GetMapping("/questions/{categoryId}")
    public ApiResponse GetQuestionsByCategoryId(@PathVariable Long categoryId){
        return new ApiResponse(200, "获取题库信息成功", getQuestionBankService.getQuestionsByCategoryId(categoryId));
    }
}

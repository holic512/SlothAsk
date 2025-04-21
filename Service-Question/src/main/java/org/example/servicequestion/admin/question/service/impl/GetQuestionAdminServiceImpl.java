/**
 * File Name: GetQuestionAdminServicce.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-02-21
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicequestion.admin.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.servicequestion.admin.question.dto.GetCateAndTagDto;
import org.example.servicequestion.admin.question.dto.GetCateDto;
import org.example.servicequestion.admin.question.dto.GetProjectDto;
import org.example.servicequestion.admin.question.dto.GetTagDto;
import org.example.servicequestion.admin.question.mapper.AdminQuestionMapper;
import org.example.servicequestion.admin.question.request.QuestionSearchRequest;
import org.example.servicequestion.admin.question.service.GetQuestionAdminService;
import org.example.servicequestion.entity.Question;
import org.example.servicequestion.util.HtmlImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetQuestionAdminServiceImpl implements GetQuestionAdminService {

    private final AdminQuestionMapper adminQuestionMapper;
    private final HtmlImageProcessor htmlImageProcessor;

    @Autowired
    public GetQuestionAdminServiceImpl(AdminQuestionMapper adminQuestionMapper, HtmlImageProcessor htmlImageProcessor) {
        this.adminQuestionMapper = adminQuestionMapper;
        this.htmlImageProcessor = htmlImageProcessor;
    }


    @Override
    public List<GetProjectDto> getProject() {
        return adminQuestionMapper.selectProjectList();
    }

    @Override
    public GetCateAndTagDto getCateAndTagByProjectId(Long projectId) {
        // 获取改项目id下的 分类列表
        List<GetCateDto> result1 = adminQuestionMapper.selectCateListByProjectId(projectId);

        // 获取改项目id下的 标签列表
        List<GetTagDto> result2 = adminQuestionMapper.selectTagListByProjectId(projectId);

        return new GetCateAndTagDto(result1, result2);
    }

    @Override
    public IPage<Question> searchQuestion(QuestionSearchRequest params) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();


        // 获取 Question 表的所有字段
        TableInfo tableInfo = TableInfoHelper.getTableInfo(Question.class);
        if (tableInfo != null) {
            List<String> allFields = tableInfo.getFieldList().stream()
                    .map(TableFieldInfo::getColumn)  // 获取数据库字段名
                    .collect(Collectors.toList());

            // 添加主键字段
            allFields.add(tableInfo.getKeyColumn());


            // 排除内容和答案 单独查询
            allFields.remove("content");
            allFields.remove("answer");

            // 设置查询字段
            queryWrapper.select(allFields.toArray(new String[0]));

        }


        // 如果关键字不为空，则在标题和内容中模糊搜索
        if (StringUtils.hasText(params.getKeyword())) {
            queryWrapper.and(wrapper ->
                    wrapper.like("title", params.getKeyword())
                            .or().like("content", params.getKeyword())
            );
        }

        if (params.getCategoryId() != null) {
            queryWrapper.eq("category_id", params.getCategoryId());
        }
        if (params.getDifficulty() != null) {
            queryWrapper.eq("difficulty", params.getDifficulty());
        }
        if (params.getType() != null) {
            queryWrapper.eq("type", params.getType());
        }
        if (params.getStatus() != null) {
            queryWrapper.eq("status", params.getStatus());
        }

        if (params.getProjectId() != null) {
            queryWrapper.eq("project_id", params.getProjectId());
        }

        Page<Question> page = new Page<>(params.getPageNum(), params.getPageSize());
        return adminQuestionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public String getContent(Long QuestionId) {

        // 设置查询条件
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("content").eq("id", QuestionId);

        // 执行查询操作 并获取内容
        Question question = adminQuestionMapper.selectOne(queryWrapper);
        String content = question != null ? question.getContent() : null;

        // 输出 正常显示的 Content
        return htmlImageProcessor.insertSrcForImages(content);
    }

    @Override
    public String getAnswer(Long QuestionId) {
        // 设置查询条件
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("answer").eq("id", QuestionId);

        // 执行查询操作 并获取内容
        Question question = adminQuestionMapper.selectOne(queryWrapper);
        String answer = question != null ? question.getAnswer() : null;

        // 输出 正常显示的 answer
        return htmlImageProcessor.insertSrcForImages(answer);
    }

    @Override
    public GetCateAndTagDto getAllCateAndTag() {
        // 获取改项目id下的 分类列表
        List<GetCateDto> result1 = adminQuestionMapper.selectAllCateList();

        // 获取改项目id下的 标签列表
        List<GetTagDto> result2 = adminQuestionMapper.selectAllTagList();

        return new GetCateAndTagDto(result1, result2);
    }

}

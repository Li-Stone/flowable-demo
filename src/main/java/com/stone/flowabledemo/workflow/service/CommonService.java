package com.stone.flowabledemo.workflow.service;

import com.stone.flowabledemo.workflow.dto.TaskDto;

import java.util.List;

/**
 * @author Administrator
 */
public interface CommonService {
    /**
     * 查询流程当前活跃任务
     *
     * @param processId
     * @param username
     * @return
     */
    List<TaskDto> activeTask(String processId, String username);

    /**
     * 关闭流程
     * @param processId
     */
    void close(String processId);
}

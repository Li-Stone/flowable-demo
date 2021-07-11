package com.stone.flowabledemo.workflow.service.impl;

import com.stone.flowabledemo.workflow.dto.TaskDto;
import com.stone.flowabledemo.workflow.service.CommonService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Resource
    private TaskService taskService;
    @Resource
    private RuntimeService runtimeService;

    @Override
    public List<TaskDto> activeTask(String processId, String username) {
        TaskQuery query = taskService.createTaskQuery().includeIdentityLinks();
        if (StringUtils.isNotBlank(processId)) {
            query.processInstanceId(processId);
        }
        if (StringUtils.isNotBlank(username)) {
            query.taskCandidateOrAssigned(username);
        }
        List<Task> taskList = query.active().list();
        List<TaskDto> list = new ArrayList<>();
        for (Task task : taskList) {
            TaskDto dto = new TaskDto();
            dto.setProcessId(task.getProcessInstanceId()).setTaskId(task.getId())
                    .setTaskName(task.getName()).setAssignee(task.getAssignee());
            list.add(dto);
        }
        return list;
    }

    @Override
    public void close(String processId) {
        runtimeService.deleteProcessInstance(processId, "强制关闭");
    }
}

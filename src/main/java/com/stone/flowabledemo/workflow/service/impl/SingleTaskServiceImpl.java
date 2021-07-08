package com.stone.flowabledemo.workflow.service.impl;

import com.stone.flowabledemo.workflow.service.SingleTaskService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
public class SingleTaskServiceImpl implements SingleTaskService {
    @Resource
    private ProcessEngine processEngine;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;

    @Override
    public Object startProcess(String user) {
        Map<String, Object> val = new HashMap<>();
        val.put("candidates", user);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("single_task", val);
        Map<String, Object> result = new HashMap<>();
        result.put("processId", instance.getProcessInstanceId());
        return result;
    }

    @Override
    public Object sendEval(String processId, String username) {
        Task task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();
        if (task != null) {
            Map<String,Object> val = new HashMap<>();
            val.put("per", username);
            taskService.complete(task.getId(), val);
        }
        return true;
    }

    @Override
    public Object doEval(String taskId, String result) {
        Map<String,Object> val = new HashMap<>();
        val.put("outcome", result);
        taskService.complete(taskId, val);
        return true;
    }
}

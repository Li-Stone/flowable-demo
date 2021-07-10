package com.stone.flowabledemo.workflow.service.impl;

import com.stone.flowabledemo.exception.CheckException;
import com.stone.flowabledemo.workflow.constant.WorkflowConstant;
import com.stone.flowabledemo.workflow.service.SingleTaskService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
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
        Map<String, Object> val = new HashMap<>(2);
        val.put("principal", user);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("single_instance", val);
        runtimeService.setProcessInstanceName(instance.getProcessInstanceId(), "single_instance");
        Map<String, Object> result = new HashMap<>(2);
        result.put("processId", instance.getProcessInstanceId());
        return result;
    }

    @Override
    public Object sendEval(String processId, String username) {
        Task task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();
        if (task == null) {
            throw new CheckException("流程没有活跃任务");
        }
        String taskName = task.getName();
        Map<String, Object> val = new HashMap<>(4);
        switch (taskName) {
            case WorkflowConstant.SINGLE_INSTANCE_NEW:
                val.put("per", username);
                taskService.complete(task.getId(), val);
                break;
            case WorkflowConstant.SINGLE_INSTANCE_EVAL_APPROVED:
            case WorkflowConstant.SINGLE_INSTANCE_EVAL_REJECTED:
                String sourceId = WorkflowConstant.SINGLE_INSTANCE_EVAL_APPROVED.equals(taskName) ?
                        WorkflowConstant.SINGLE_ID_APPROVED : WorkflowConstant.SINGLE_ID_REJECTED;
                runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId())
                        .moveActivityIdTo(sourceId, WorkflowConstant.SINGLE_ID_EVAL)
                        .changeState();
                task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();
                taskService.setAssignee(task.getId(), username);
                break;
            default:
                throw new CheckException("当前节点不能进行发评估操作");
        }
        return true;
    }

    @Override
    public Object transferEval(String taskId, String username) {
        taskService.setAssignee(taskId, username);
        return true;
    }

    @Override
    public Object doEval(String taskId, String result) {
        Map<String, Object> val = new HashMap<>(2);
        val.put("outcome", result);
        taskService.complete(taskId, val);
        return true;
    }
}

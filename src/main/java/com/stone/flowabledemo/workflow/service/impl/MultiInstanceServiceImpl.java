package com.stone.flowabledemo.workflow.service.impl;

import com.stone.flowabledemo.exception.CheckException;
import com.stone.flowabledemo.workflow.constant.WorkflowConstant;
import com.stone.flowabledemo.workflow.service.MultiInstanceService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Administrator
 */
@Service
public class MultiInstanceServiceImpl implements MultiInstanceService {

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;

    @Override
    public Map<String, Object> startProcess(String user) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> var = new HashMap<>();
        var.put(WorkflowConstant.VAR_KEY_PRINCIPAL, user);
        var.put(WorkflowConstant.VAR_KEY_EVAL_RESULT, new HashMap<>(8));
        var.put(WorkflowConstant.VAR_KEY_EVAL_REJECTED, 0);
        ProcessInstance process = runtimeService.startProcessInstanceByKey(WorkflowConstant.PROCESS_KEY_MULTI_INSTANCE, var);
        Task task = taskService.createTaskQuery().processInstanceId(process.getProcessInstanceId()).active().singleResult();
        result.put("processId", process.getProcessInstanceId());
        result.put("status", task.getName());
        return result;
    }

    @Override
    public boolean sendEval(String processId, Set<String> userList) {
        Task task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();
        Map<String, Object> var = new HashMap<>(2);
        switch (task.getName()) {
            case WorkflowConstant.STATUS_NEW:
                var.put("pers", userList);
                taskService.complete(task.getId(), var);
                break;
            case WorkflowConstant.STATUS_EVAL:
                break;
            case WorkflowConstant.STATUS_APPROVED:
                break;
            case WorkflowConstant.STATUS_REJECTED:
                break;
            default:
                throw new CheckException("当前节点不可发起评估");
        }
        return false;
    }

    @Override
    public boolean transferEval(String taskId, String newUser) {
        return false;
    }

    @Override
    public boolean deleteTask(String taskId) {
        return false;
    }

    @Override
    public boolean doEval(String taskId, String result) {
        Task task = taskService.createTaskQuery().taskId(taskId).active().includeProcessVariables().singleResult();
        if (task == null) {
            throw new CheckException("taskId为非活跃任务");
        }
        Map<String,Object> var = new HashMap<>(8);
        Map evalResult = (Map) task.getProcessVariables().get(WorkflowConstant.VAR_KEY_EVAL_RESULT);
        evalResult.put(task.getAssignee(), result);
        var.put(WorkflowConstant.VAR_KEY_EVAL_RESULT, evalResult);
        var.put(WorkflowConstant.VAR_KEY_EVAL_CURRENT_RESULT, result);
        taskService.complete(taskId, var);
        return true;
    }
}

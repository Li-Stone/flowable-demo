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
    public Map<String, Object> startProcess(String user) {
        Map<String, Object> val = new HashMap<>(2);
        val.put(WorkflowConstant.VAR_KEY_PRINCIPAL, user);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("single_instance", val);
        runtimeService.setProcessInstanceName(instance.getProcessInstanceId(), "single_instance");
        Map<String, Object> result = new HashMap<>(2);
        result.put("processId", instance.getProcessInstanceId());
        return result;
    }

    @Override
    public boolean sendEval(String processId, String username) {
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
    public boolean transferEval(String taskId, String username) {
        taskService.setAssignee(taskId, username);
        return true;
    }

    @Override
    public boolean deleteTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task==null) {
            throw new CheckException("任务不存在");
        }
        if (!task.getName().equals(WorkflowConstant.SINGLE_INSTANCE_EVAL)) {
            throw new CheckException("非评估中不可删除任务");
        }
        // 删除任务即是流程从当前节点回退到最近一次发评估操作前的节点
        String preNodeId = WorkflowConstant.SINGLE_ID_NEW;
        ProcessInstance process = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).includeProcessVariables().singleResult();
        Map<String, Object> vars = process.getProcessVariables();
        if (vars.containsKey(WorkflowConstant.VAR_KEY_OUTCOME)) {
            preNodeId = WorkflowConstant.EVAL_RESULT_APPROVED.equals(vars.get(WorkflowConstant.VAR_KEY_OUTCOME))?WorkflowConstant.SINGLE_ID_APPROVED: WorkflowConstant.SINGLE_ID_REJECTED;
        }
        runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId())
                .moveActivityIdTo(WorkflowConstant.SINGLE_ID_EVAL, preNodeId)
                .changeState();
        return true;
    }

    @Override
    public boolean doEval(String taskId, String result) {
        Map<String, Object> val = new HashMap<>(2);
        val.put(WorkflowConstant.VAR_KEY_OUTCOME, result);
        taskService.complete(taskId, val);
        return true;
    }

    @Override
    public boolean transferPrincipal(String processId, String principal) {
        // todo executionId 手动删除再添加 act_ru_identitylink 数据
        runtimeService.setVariable(processId, WorkflowConstant.VAR_KEY_PRINCIPAL, principal);
        return true;
    }
}

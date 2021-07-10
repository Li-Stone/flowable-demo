package com.stone.flowabledemo.workflow.service;

import java.util.Map;

/**
 * @author Administrator
 */
public interface SingleTaskService {
    /**
     * 开启一个新流程实例
     *
     * @param user 负责人
     * @return
     */
    Map<String, Object> startProcess(String user);

    /**
     * 发起评估
     *
     * @param processId
     * @param username
     * @return
     */
    boolean sendEval(String processId, String username);

    /**
     * 任务转派
     *
     * @param taskId
     * @param username
     * @return
     */
    boolean transferEval(String taskId, String username);

    /**
     * 删除评估任务
     *
     * @param taskId
     * @return
     */
    boolean deleteTask(String taskId);

    /**
     * 处理评估
     *
     * @param taskId
     * @param result
     * @return
     */
    boolean doEval(String taskId, String result);

    /**
     * 转派任务
     *
     * @param processId
     * @param principal
     * @return
     */
    boolean transferPrincipal(String processId, String principal);

}

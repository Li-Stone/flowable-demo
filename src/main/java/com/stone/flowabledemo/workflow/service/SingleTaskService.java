package com.stone.flowabledemo.workflow.service;

/**
 *
 * @author Administrator
 */
public interface SingleTaskService {
    /**
     * 开启一个新流程实例
     * @param user 负责人
     * @return
     */
    Object startProcess(String user);

    /**
     * 发起评估
     * @param processId
     * @param username
     * @return
     */
    Object sendEval(String processId, String username);

    /**
     * 处理评估
     *
     * @param taskId
     * @param result
     * @return
     */
    Object doEval(String taskId, String result);

}

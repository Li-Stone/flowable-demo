package com.stone.flowabledemo.workflow.service;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface MultiInstanceService {

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
     * @param userList
     * @return
     */
    boolean sendEval(String processId, List<String> userList);

    /**
     * 任务转派
     *
     * @param taskId
     * @param newUser
     * @return
     */
    boolean transferEval(String taskId, String newUser);

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

}

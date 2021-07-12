package com.stone.flowabledemo.workflow.service.impl;

import com.stone.flowabledemo.workflow.service.MultiInstanceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
@Service
public class MultiInstanceServiceImpl implements MultiInstanceService {
    @Override
    public Map<String, Object> startProcess(String user) {
        return null;
    }

    @Override
    public boolean sendEval(String processId, List<String> userList) {
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
        return false;
    }
}

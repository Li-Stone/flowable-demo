package com.stone.flowabledemo.workflow.controller;

import com.stone.flowabledemo.workflow.constant.ResponseData;
import com.stone.flowabledemo.workflow.service.SingleTaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("singleTask")
public class SingleTaskController {
    @Resource
    private SingleTaskService singleTaskService;

    @PostMapping("create")
    public ResponseData<Object> create(String username) {
        ResponseData<Object> response = new ResponseData<>();
        response.setData(singleTaskService.startProcess(username));
        return response;
    }

    @PutMapping("sendEval")
    public ResponseData<Object> sendEval(String processId, String username) {
        ResponseData<Object> response = new ResponseData<>();
        response.setData(singleTaskService.sendEval(processId, username));
        return response;
    }

    @PutMapping("doEval")
    public ResponseData<Object> doEval(String taskId, String result) {
        ResponseData<Object> response = new ResponseData<>();
        response.setData(singleTaskService.doEval(taskId, result));
        return response;
    }
}

package com.stone.flowabledemo.workflow.controller;

import com.stone.flowabledemo.constant.ResponseData;
import com.stone.flowabledemo.workflow.service.SingleTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Api(tags = "单实例任务")
@RestController
@RequestMapping("singleTask")
public class SingleTaskController {
    @Resource
    private SingleTaskService singleTaskService;

    @ApiOperation("创建一个新流程")
    @ApiImplicitParam(name="username", value="负责人", required = true)
    @PostMapping("create")
    public ResponseData<Object> create(String username) {
        ResponseData<Object> response = new ResponseData<>();
        response.setData(singleTaskService.startProcess(username));
        return response;
    }

    @ApiOperation("发起评估")
    @ApiImplicitParams({
            @ApiImplicitParam(name="processId", value="流程id", required = true),
            @ApiImplicitParam(name="username", value="评估人", required = true)
    })
    @PutMapping("sendEval")
    public ResponseData<Object> sendEval(String processId, String username) {
        ResponseData<Object> response = new ResponseData<>();
        response.setData(singleTaskService.sendEval(processId, username));
        return response;
    }

    @ApiOperation("评估转派")
    @ApiImplicitParams({
            @ApiImplicitParam(name="taskId", value="任务id", required = true),
            @ApiImplicitParam(name="username", value="转派人", required = true)
    })
    @PutMapping("transferEval")
    public ResponseData<Object> transferEval(String taskId, String username) {
        ResponseData<Object> response = new ResponseData<>();
        response.setData(singleTaskService.transferEval(taskId, username));
        return response;
    }

    @ApiOperation("处理评估")
    @ApiImplicitParams({
            @ApiImplicitParam(name="taskId", value="任务Id", required = true),
            @ApiImplicitParam(name="result", value="评估结果", required = true)
    })
    @PutMapping("doEval")
    public ResponseData<Object> doEval(String taskId, String result) {
        ResponseData<Object> response = new ResponseData<>();
        response.setData(singleTaskService.doEval(taskId, result));
        return response;
    }
}

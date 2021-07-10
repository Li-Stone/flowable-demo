package com.stone.flowabledemo.workflow.controller;

import com.stone.flowabledemo.constant.ResponseData;
import com.stone.flowabledemo.workflow.service.SingleTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

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
    public ResponseData<Map<String, Object>> create(String username) {
        ResponseData<Map<String, Object>> response = new ResponseData<>();
        response.setData(singleTaskService.startProcess(username));
        return response;
    }

    @ApiOperation("发起评估")
    @ApiImplicitParams({
            @ApiImplicitParam(name="processId", value="流程id", required = true),
            @ApiImplicitParam(name="username", value="评估人", required = true)
    })
    @PutMapping("sendEval")
    public ResponseData<Boolean> sendEval(String processId, String username) {
        ResponseData<Boolean> response = new ResponseData<>();
        response.setData(singleTaskService.sendEval(processId, username));
        return response;
    }

    @ApiOperation("评估转派")
    @ApiImplicitParams({
            @ApiImplicitParam(name="taskId", value="任务id", required = true),
            @ApiImplicitParam(name="username", value="转派人", required = true)
    })
    @PutMapping("transferEval")
    public ResponseData<Boolean> transferEval(String taskId, String username) {
        ResponseData<Boolean> response = new ResponseData<>();
        response.setData(singleTaskService.transferEval(taskId, username));
        return response;
    }

    @ApiOperation("处理评估")
    @ApiImplicitParams({
            @ApiImplicitParam(name="taskId", value="任务Id", required = true),
            @ApiImplicitParam(name="result", value="评估结果", required = true)
    })
    @PutMapping("doEval")
    public ResponseData<Boolean> doEval(String taskId, String result) {
        ResponseData<Boolean> response = new ResponseData<>();
        response.setData(singleTaskService.doEval(taskId, result));
        return response;
    }

    @ApiOperation("删除评估")
    @ApiImplicitParam(name="taskId", value="任务Id", required = true)
    @DeleteMapping("deleteTask/{taskId}")
    public ResponseData<Boolean> deleteTask(@PathVariable String taskId) {
        ResponseData<Boolean> response = new ResponseData<>();
        response.setData(singleTaskService.deleteTask(taskId));
        return response;
    }

    @ApiOperation("转派流程负责人")
    @ApiImplicitParams({
            @ApiImplicitParam(name="processId", value="流程id", required = true),
            @ApiImplicitParam(name="username", value="新负责人", required = true)
    })
    @PutMapping("transferPrincipal/{processId}/{username}")
    public ResponseData<Boolean> transferPrincipal(@PathVariable String processId, @PathVariable String username) {
        ResponseData<Boolean> response = new ResponseData<>();
        response.setData(singleTaskService.transferPrincipal(processId, username));
        return response;
    }
}

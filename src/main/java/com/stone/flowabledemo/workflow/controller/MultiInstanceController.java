package com.stone.flowabledemo.workflow.controller;

import com.stone.flowabledemo.constant.ResponseData;
import com.stone.flowabledemo.util.CheckUtil;
import com.stone.flowabledemo.workflow.service.MultiInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 简单多实例流程
 *
 * @author Administrator
 */
@Api(tags = "多实例任务")
@RestController
@RequestMapping("multiInstance")
public class MultiInstanceController {

    @Resource
    private MultiInstanceService multiInstanceService;


    @ApiOperation("创建一个新流程")
    @ApiImplicitParam(name="username", value="负责人", required = true)
    @PostMapping("create")
    public ResponseData<Map<String, Object>> create(String username) {
        CheckUtil.check(StringUtils.isNoneBlank(username), "负责人不能为空");
        ResponseData<Map<String, Object>> response = new ResponseData<>();
        response.setData(multiInstanceService.startProcess(username));
        return response;
    }

    @ApiOperation("发起评估")
    @ApiImplicitParams({
            @ApiImplicitParam(name="processId", value="流程id", required = true),
            @ApiImplicitParam(name="username", value="评估人", required = true)
    })
    @PutMapping("sendEval")
    public ResponseData<Boolean> sendEval(String processId, String... username) {
        ResponseData<Boolean> response = new ResponseData<>();
        response.setData(multiInstanceService.sendEval(processId, new HashSet<>(Arrays.stream(username).collect(Collectors.toList()))));
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
        response.setData(multiInstanceService.transferEval(taskId, username));
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
        response.setData(multiInstanceService.doEval(taskId, result));
        return response;
    }

    @ApiOperation("删除评估")
    @ApiImplicitParam(name="taskId", value="任务Id", required = true)
    @DeleteMapping("deleteTask/{taskId}")
    public ResponseData<Boolean> deleteTask(@PathVariable String taskId) {
        ResponseData<Boolean> response = new ResponseData<>();
        response.setData(multiInstanceService.deleteTask(taskId));
        return response;
    }
}

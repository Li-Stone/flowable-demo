package com.stone.flowabledemo.workflow.controller;

import com.stone.flowabledemo.constant.ResponseData;
import com.stone.flowabledemo.workflow.dto.TaskDto;
import com.stone.flowabledemo.workflow.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 公共接口
 *
 * @author Administrator
 */
@Api(tags = "工作流公用接口")
@RestController
@RequestMapping("common")
public class CommonController {
    @Resource
    private CommonService commonService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private ProcessEngine processEngine;

    @ApiOperation("手动部署流程")
    @GetMapping("deploy/{name}")
    public ResponseData<Boolean> deploy(@PathVariable String name) {
        ResponseData<Boolean> response = new ResponseData<>();
        try {
            String path = new ClassPathResource("processes/" + name + ".bpmn20.xml").getPath();
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                    .addClasspathResource(path);
            deploymentBuilder.deploy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation("查询流程的活跃任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程id"),
            @ApiImplicitParam(name = "username", value = "任务处理人")
    })
    @GetMapping("activeTask")
    public ResponseData<List<TaskDto>> activeTask(String processId, String username) {
        ResponseData<List<TaskDto>> response = new ResponseData<>();
        response.setData(commonService.activeTask(processId, username));
        return response;
    }

    @ApiOperation("强制关闭流程")
    @GetMapping("close/{processId}")
    public ResponseData<Boolean> close(@PathVariable String processId) {
        ResponseData<Boolean> response = new ResponseData<>();
        commonService.close(processId);
        return response;
    }

    @ApiOperation("查看流程图")
    @GetMapping("processDiagram/{processId}")
    public void generateDiagram(@PathVariable String processId, HttpServletResponse httpServletResponse) throws Exception {
        // todo 历史走过的节点标绿色
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        //流程走完的不显示图
        if (pi == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();

        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(InstanceId)
                .list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0, false);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = httpServletResponse.getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

    }

}

package com.stone.flowabledemo.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
@ApiModel("任务dto")
public class TaskDto {
    @ApiModelProperty("流程id")
    private String processId;
    @ApiModelProperty("任务id")
    private String taskId;
    @ApiModelProperty("任务节点名称")
    private String taskName;
    @ApiModelProperty("任务处理人")
    private String assignee;

}

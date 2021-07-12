package com.stone.flowabledemo.workflow.component;

import com.stone.flowabledemo.workflow.constant.WorkflowConstant;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Component("multiEval")
public class MultiEvalCompleteTask implements Serializable {

    private static final long serialVersionUID = -858029427755527737L;


    public boolean accessCondition(DelegateExecution execution) {
        int sum = (int) execution.getVariable("nrOfInstances");
        int activeNum = (int) execution.getVariable("nrOfActiveInstances");
        int complete = (int) execution.getVariable("nrOfCompletedInstances");
        String currentActivityId = execution.getCurrentActivityId();
        if (!currentActivityId.equals(WorkflowConstant.MULTI_ID_EVAL)) {
            throw new FlowableException(execution.getProcessInstanceId() + "任务定义id异常");
        }
        if (sum != complete) {
            return false;
        }
        if (execution.getVariable(WorkflowConstant.VAR_KEY_EVAL_RESULT) != null) {
            if ((int) execution.getVariable(WorkflowConstant.VAR_KEY_EVAL_REJECTED) > 0) {
                // 有拒绝
                execution.setVariable(WorkflowConstant.VAR_KEY_OUTCOME, WorkflowConstant.EVAL_RESULT_REJECTED);
                return true;
            }
        }
        execution.setVariable(WorkflowConstant.VAR_KEY_OUTCOME, WorkflowConstant.EVAL_RESULT_APPROVED);
        return true;
    }
}

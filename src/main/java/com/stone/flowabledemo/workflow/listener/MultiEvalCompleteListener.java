package com.stone.flowabledemo.workflow.listener;

import com.stone.flowabledemo.workflow.constant.WorkflowConstant;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component("MultiEvalCompleteListener")
public class MultiEvalCompleteListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String result = (String) delegateTask.getVariable(WorkflowConstant.VAR_KEY_EVAL_CURRENT_RESULT);
        int rejectedCount;
        String evalRejectedKey = WorkflowConstant.VAR_KEY_EVAL_REJECTED;
        if (WorkflowConstant.EVAL_RESULT_REJECTED.equals(result)) {
            if (delegateTask.getVariable(evalRejectedKey) != null) {
                rejectedCount = (int) delegateTask.getVariable(evalRejectedKey);
                delegateTask.setVariable(evalRejectedKey, ++rejectedCount);
            }
        }
    }
}

package com.stone.flowabledemo.workflow.listener;

import com.stone.flowabledemo.workflow.constant.WorkflowConstant;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * 执行监听器
 *
 * @author Administrator
 */
@Component("MultiEvalExecutionStartListener")
public class MultiEvalExecutionStartListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        // 初始化评估结果为不通过的数量为0
        delegateExecution.setVariable(WorkflowConstant.VAR_KEY_EVAL_REJECTED, 0);
    }
}

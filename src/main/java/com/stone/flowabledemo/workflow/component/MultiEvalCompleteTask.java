package com.stone.flowabledemo.workflow.component;

import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Component("multiEval")
public class MultiEvalCompleteTask implements Serializable {

    private static final long serialVersionUID = -858029427755527737L;


    public boolean accessCondition(DelegateExecution execution){
        int sum = (int) execution.getVariable("nrOfInstances");
        int activeNum = (int) execution.getVariable("nrOfActiveInstances");
        int complete = (int) execution.getVariable("nrOfCompletedInstances");
        return true;
    }
}

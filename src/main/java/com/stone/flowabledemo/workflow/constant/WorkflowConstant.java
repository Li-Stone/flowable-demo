package com.stone.flowabledemo.workflow.constant;


/**
 * 工作流常量
 *
 * @author Administrator
 */
public class WorkflowConstant {

    public static final String PROCESS_KEY_SINGLE_INSTANCE = "single_instance";
    public static final String PROCESS_KEY_MULTI_INSTANCE = "multi_instance";

    public static final String STATUS_NEW = "新建";
    public static final String STATUS_EVAL = "评估中";
    public static final String STATUS_APPROVED = "评估通过";
    public static final String STATUS_REJECTED = "评估不通过";

    public static final String EVAL_RESULT_APPROVED = "approved";
    public static final String EVAL_RESULT_REJECTED = "rejected";

    public static final String SINGLE_ID_NEW = "_new";
    public static final String SINGLE_ID_EVAL = "_eval";
    public static final String SINGLE_ID_APPROVED = "_approved";
    public static final String SINGLE_ID_REJECTED = "_rejected";

    public static final String MULTI_ID_NEW = "_new";
    public static final String MULTI_ID_EVAL = "_eval";
    public static final String MULTI_ID_APPROVED = "_approved";
    public static final String MULTI_ID_REJECTED = "_rejected";

    public static final String VAR_KEY_PRINCIPAL = "principal";
    public static final String VAR_KEY_OUTCOME = "outcome";

    public static final String VAR_KEY_EVAL_REJECTED = "rejected";
    public static final String VAR_KEY_EVAL_CURRENT_RESULT = "currentResult";
    public static final String VAR_KEY_EVAL_RESULT = "evalResult";
}

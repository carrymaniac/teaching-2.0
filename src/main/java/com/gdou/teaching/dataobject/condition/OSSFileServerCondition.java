package com.gdou.teaching.dataobject.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.dataobject.condition
 * @ClassName: OSSFileServerCondition
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/5/19 9:08 下午
 * @Version:
 */
public class OSSFileServerCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String type = conditionContext.getEnvironment().getProperty("fileServer.type");
        return "oss".equalsIgnoreCase(type);
    }
}

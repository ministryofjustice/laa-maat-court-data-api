package gov.uk.courtdata.aspect;

import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.spring.aop.AbstractXRayInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Slf4j
@Component
@ConditionalOnProperty(prefix = "xray", value = "enabled", havingValue = "true")
public class XRayInspector extends AbstractXRayInterceptor {
    @Override
    protected Map<String, Map<String, Object>> generateMetadata(ProceedingJoinPoint proceedingJoinPoint, Subsegment subsegment) {
        Map<String, Map<String, Object>> metadata = super.generateMetadata(proceedingJoinPoint, subsegment);

        Map<String, Object> argumentsInfo = new HashMap<>();

        Object[] methodArgs = proceedingJoinPoint.getArgs();
        if (methodArgs != null) {
            Arrays.stream(methodArgs)
                    .filter(Objects::nonNull)
                    .forEach(arg -> argumentsInfo.put(arg.getClass().getSimpleName(), arg));
            metadata.put("Arguments", argumentsInfo);
        }
        metadata.get("ClassInfo").put("Method", proceedingJoinPoint.getSignature().getName());
        return metadata;
    }

    @Override
    @Pointcut("" +
            "@within(com.amazonaws.xray.spring.aop.XRayEnabled) && " +
            "(bean(*Controller) || bean(*Listener) || bean(*Service) || bean(*Validator) || bean(*Impl))"
    )
    public void xrayEnabledClasses() {
    }
}

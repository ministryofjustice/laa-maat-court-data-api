package gov.uk.courtdata.aspect;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import com.amazonaws.xray.spring.aop.AbstractXRayInterceptor;
import gov.uk.courtdata.util.SignatureParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.application.name}")
    private String AWS_XRAY_SEGMENT_NAME;

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
        return metadata;
    }

    @Override
    @Pointcut(
            "@within(com.amazonaws.xray.spring.aop.XRayEnabled) && " +
                    "(bean(*Controller) || bean(*Service) || bean(*Validator) || bean(*Impl) || bean(*Processor))"
    )
    public void xrayEnabledClasses() {
    }

    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) && (bean(*Listener))")
    public void xrayEnabledListeners() {
    }

    @Around("xrayEnabledListeners()")
    public Object traceAroundListeners(ProceedingJoinPoint pjp) throws Throwable {
        try {
            AWSXRay.beginSegment(AWS_XRAY_SEGMENT_NAME);
            AWSXRay.beginSubsegment(SignatureParser.getFormattedMethodName(pjp.getSignature()));
            return pjp.proceed(pjp.getArgs());
        } catch (Exception e) {
            AWSXRay.getCurrentSegmentOptional().ifPresent(x -> x.addException(e));
            throw e;
        } finally {
            AWSXRay.endSubsegment();
            AWSXRay.endSegment();
        }
    }
}

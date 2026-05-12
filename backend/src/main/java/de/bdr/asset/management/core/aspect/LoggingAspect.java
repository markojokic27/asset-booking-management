package de.bdr.asset.management.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Matches all methods in any class annotated with @RestController
    // TODO: security concerns, maybe shouldnt log auth controllers
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllers() {}

    // Targets any class annotated with @Service
    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void serviceLayer() {}

    @Around("restControllers()")
    public Object logControllerAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long start = System.currentTimeMillis();

        try {
            extractContextToMDC(joinPoint);
            log.info("REST_CALL_START: {}", methodName);

            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - start;
            log.info("REST_CALL_SUCCESS: {} ({}ms)", methodName, duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("REST_CALL_ERROR: {} -> {} ({}ms)", methodName, e.getMessage(), duration);
            throw e;
        } finally {
            MDC.clear(); // Essential to prevent context leaking between threads
        }
    }

    @Around("serviceLayer()")
    public Object logServiceAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        // Use toShortString() to get "UserService.getUserById" instead of just the method name
        String methodName = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();

        try {
            MDC.put("layer", "SERVICE");
            log.info("SERVICE_START: {}", methodName);

            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - start;
            log.info("SERVICE_SUCCESS: {} ({}ms)", methodName, duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("SERVICE_ERROR: {} -> {} ({}ms)", methodName, e.getMessage(), duration);
            throw e;
        } finally {
            // Remove ONLY the layer key so we don't break the Controller's MDC
            MDC.remove("layer");
        }
    }

    private void extractContextToMDC(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            if (arg instanceof Pageable p) {
                MDC.put("page", String.valueOf(p.getPageNumber()));
                MDC.put("size", String.valueOf(p.getPageSize()));
            } else if ((arg instanceof Long || arg instanceof String || arg instanceof Integer)
                // Captures path variables/params using their actual names (e.g., "id")
                && parameterNames != null && parameterNames.length > i) {
                    MDC.put(parameterNames[i], String.valueOf(arg));
            }
        }
    }
}

package com.example.HealthCareManagement.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class UserActivityAspect {

    // 1. Log before any method in AuthService starts
    @Before("execution(* com.example.HealthCareManagement.service.AuthService.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("AOP AUDIT: Starting method [" + methodName + "] with arguments: " + Arrays.toString(args));
    }

    // 2. Log specifically after a successful password change or profile update
    @AfterReturning("execution(* com.example.HealthCareManagement.service.AuthService.update*(..)) || " +
            "execution(* com.example.HealthCareManagement.service.AuthService.changePassword(..))")
    public void logSensitiveUpdates(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("AOP SECURITY ALERT: User data successfully modified via [" + methodName + "].");
    }

    // 3. Log specifically when a new user is registered
    @AfterReturning(pointcut = "execution(* com.example.HealthCareManagement.service.AuthService.register(..))", returning = "result")
    public void logNewRegistration(Object result) {
        System.out.println("AOP AUDIT: A new user has been successfully registered in the system.");
    }
}
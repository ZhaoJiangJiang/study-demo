package com.study.demo.commonswitch.aspect;

import com.study.demo.commonswitch.annotation.ServiceSwitch;
import com.study.demo.commonswitch.constant.ServiceSwitchConstant;
import com.study.demo.commonswitch.exception.GeneralException;
import com.study.demo.commonswitch.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class ServiceSwitchAspect {

    private final Jedis jedis = new Jedis("localhost", 6379);

    /**
     * 定义切点 使用了@ServiceSwitch注解的类或方法都拦截
     */
    @Pointcut("@annotation(com.study.demo.commonswitch.annotation.ServiceSwitch)")
    public void serviceSwitchPointCut() {}

    @Around("serviceSwitchPointCut()")  // 拦截带有 @serviceSwitchPointCut 注解的方法
    public Object serviceSwitch(ProceedingJoinPoint proceedingJoinPoint) {
        // 获取当前被代理方法的参数
        Object[] args = proceedingJoinPoint.getArgs();

        // 获取通知签名
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();     // 拿到被注解的方法 = Result com.study.demo.commonswitch.service.impl.OrderServiceImpl.createOrder()
        // 直接通过签名获取被注解的方法
        Method method = methodSignature.getMethod();    // public com.study.demo.commonswitch.result.Result com.study.demo.commonswitch.service.impl.OrderServiceImpl.createOrder()
        try {
            /*  先获取Target获取被注解的对象，通过Target获取getClass().getMethod(methodName, methodParameter)指定方法名和参数，也可以拿到被注解的方法
             *  这种方法可以通过Target获取被注解的类中的其他方法，更适合于需要反射访问目标类中的特定公共方法的场景
             *  // 获取当前被代理的对象
             *  Object target = proceedingJoinPoint.getTarget();    // 拿到被注解的对象 = com.study.demo.commonswitch.service.impl.OrderServiceImpl@76c04a52
             *  // 获取被代理的方法
             *  Method method = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes()); // public com.study.demo.commonswitch.result.Result com.study.demo.commonswitch.service.impl.OrderServiceImpl.createOrder()
             *  log.info("methodSignature.getName() = {}", methodSignature.getName());  // 拿到方法名 createOrder
             *  log.info("methodSignature.getParameterTypes() = {}", methodSignature.getParameterNames());
             */
            // 获取方法上的注解
            ServiceSwitch annotation = method.getAnnotation(ServiceSwitch.class);
            // 核心业务逻辑
            if (annotation != null) {
                String switchKey = annotation.switchKey();
                String message = annotation.message();

                // 配置加载在Redis 查询Redis时获取
                String configVal = jedis.get(switchKey);
                if (ServiceSwitchConstant.SWITCH_CLOSED.equals(configVal)) {
                    return new Result<>(HttpStatus.FORBIDDEN.value(), message);
                }
            }
            // 放行
            return proceedingJoinPoint.proceed(args);   // 执行方法

        } catch (Throwable e) {
             return new GeneralException(e.getMessage(), e);
        }
    }
}

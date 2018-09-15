package com.server.log;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@Slf4j
@Component
@Aspect
public class ControllerLog {
    private static String rootPath="dev";

    @Value("${custom-config.log.mode}")
    public void setRootPath(String a) {
        rootPath = a;
    }

    @Pointcut("execution(public * com.server.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        StringBuilder logSB = new StringBuilder();
        logSB.append("[REQ]");
        logSB.append("; URL:").append(request.getRequestURL());
        logSB.append("; METHOD:").append(request.getMethod());
        logSB.append("; URI:").append(request.getRequestURI());
        logSB.append("; PARAMS:").append(request.getQueryString());
        logSB.append("; REMOTE_IP:").append(request.getRemoteAddr());
        logSB.append("; FUNCTION:").append(joinPoint.getSignature().getDeclaringTypeName()).append("#").append(joinPoint.getSignature().getName());
        Object[] argsObj = joinPoint.getArgs();
        logSB.append("; ARGS:").append((argsObj.length > 0 ? (argsObj.length > 1 ? Arrays.toString(argsObj) : argsObj[0].toString()) : ""));
        log.info(logFormat(logSB));
    }

    @AfterReturning(returning = "returnValue", pointcut = "webLog()")
    public void doAfterReturning(Object returnValue) {
        StringBuilder logSB = new StringBuilder();
        logSB.append("[RES]");
        logSB.append("; RESPONSE:").append(returnValue);
        log.info(logFormat(logSB));
    }

    private static String logFormat(StringBuilder logSB) {
        String logStr;
        if (rootPath.equals("dev")) {
            logStr = logSB.insert(0, "\n").toString().replaceAll(";", "\n");
        } else {
            logStr = logSB.toString();
        }
        return logStr;
    }

//    //    @Around("webLog()")
//    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
//        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();
//        log.info("-------------------------------------------------------");
//        log.info("URL      : {}", request.getRequestURL().toString());
//        log.info("METHOD   : {}", request.getMethod());
//        log.info("URI      : {}", request.getRequestURI());
//        log.info("IP       : {}", request.getRemoteAddr());
//        log.info("FUNCTION : {}", pjp.getSignature().getDeclaringTypeName());
//        log.info("PARAMS   : {}", request.getQueryString());
//
////        logger.info("PARAMS-JSON: {}", getRequestStr(request));
//        //        JSONObject paramJObj = JSON.parseObject(HttpKit.readData(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()));
////        logger.info("PARAM-JSON : " + (paramJObj == null ? "" : paramJObj.toString()));
////        try {
////            logger.info("PARAM-JSON : " + getRequestStr(request));
////        } catch (Exception e) {
////            logger.info("PARAM-JSON : 出错:" + e.getMessage());
////        }
//        log.info("-------------------------------------------------------");
//
//        // 执行被拦截方法，获取被拦截方法返回结果，result的值就是被拦截方法的返回值
//        Object result = pjp.proceed();
//
//        log.info("RESPONSE : " + result);
////        //拦截的实体类
////        Object target = pjp.getTarget();
////        //拦截的方法名称
////        String methodName = pjp.getSignature().getName();
////        //拦截的放参数类型
////        Class[] parameterTypes = ((MethodSignature)pjp.getSignature()).getMethod().getParameterTypes();
////        Method method = target.getClass().getMethod(methodName, parameterTypes);
////        Annotation[] annotations = method.getDeclaredAnnotations();
////        StringBuilder annotationsSB=new StringBuilder();
////        for(int i=0;i<annotations.length;i++){
////            annotationsSB.append(annotations[i].getClass());
////        }
////        logger.info("Annotations : " + annotationsSB);
////        Signature signature = pjp.getSignature();
////        MethodSignature methodSignature = (MethodSignature)signature;
////        Method targetMethod = methodSignature.getMethod();
////        Class clazz = targetMethod.getClass();
//        return result;
//    }

//    private static byte[] getRequestBytes(HttpServletRequest request) throws IOException {
//        int contentLength = request.getContentLength();
//        if (contentLength < 0) {
//            return null;
//        }
//        byte buffer[] = new byte[contentLength];
//        for (int i = 0; i < contentLength; ) {
//            int readLen = request.getInputStream().read(buffer, i, contentLength - i);
//            if (readLen == -1) {
//                break;
//            }
//            i += readLen;
//        }
//        return buffer;
//    }
//    private static String getRequestStr(HttpServletRequest request) throws IOException {
//        byte buffer[] = getRequestBytes(request);
//        String charEncoding = request.getCharacterEncoding();
//        if (charEncoding == null) {
//            charEncoding = "UTF-8";
//        }
//        return new String(buffer, charEncoding);
//    }


//    private static String getAllParameter(HttpServletRequest request){
//        //        //获取get方法参数或URL中携带所有参数：
//        Enumeration<String> enu = request.getParameterNames();
//        StringBuilder paramSB = new StringBuilder();
//        while (enu.hasMoreElements()) {
//            String paraName = enu.nextElement();
//            paramSB.append(paraName);
//            paramSB.append("=");
//            paramSB.append(request.getParameter(paraName));
//            paramSB.append("  ");
//        }
//        return paramSB.toString();
//    }
}

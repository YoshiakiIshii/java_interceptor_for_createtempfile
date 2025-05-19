package com.example.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Aspect
@Component
public class AACacheFileAspect {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AACacheFileHandler cacheFileHandler;

    @Around("@annotation(com.example.util.AACacheableInit)")
    public Object aroundInit(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AACacheableInit[] annotations = method.getAnnotationsByType(AACacheableInit.class);

        boolean isCacheFileExists = true;
        for (AACacheableInit annotation : annotations) {
            String cacheFilePathId = annotation.cacheFilePathId();
            String dataField = annotation.dataField();

            String cacheFilePath = (String) applicationContext.getBean(cacheFilePathId);

            Field dataFieldObj = target.getClass().getDeclaredField(dataField);
            dataFieldObj.setAccessible(true);
            
            // デシリアライズ
            if (cacheFileHandler.isCacheFileExists(cacheFilePath)) {
                try {
                    Object data = cacheFileHandler.loadCacheFromFile(cacheFilePath);
                    dataFieldObj.set(target, data);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                isCacheFileExists = false;
            }
        }

        if (isCacheFileExists) {
            // キャッシュファイルが存在する場合は、通常のinit処理をスキップ
            return null;
        }

        // 通常のinit処理（データ加工）
        Object result = joinPoint.proceed();

        for (AACacheableInit annotation : annotations) {
            String cacheFilePathId = annotation.cacheFilePathId();
            String dataField = annotation.dataField();

            String cacheFilePath = (String) applicationContext.getBean(cacheFilePathId);

            Field dataFieldObj = target.getClass().getDeclaredField(dataField);
            dataFieldObj.setAccessible(true);

            // シリアライズ
            try {
                Object data = dataFieldObj.get(target);
                cacheFileHandler.saveCacheToFile(cacheFilePath, data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
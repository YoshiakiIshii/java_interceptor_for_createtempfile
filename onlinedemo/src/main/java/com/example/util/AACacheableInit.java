package com.example.util;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AACacheableInits.class)
public @interface AACacheableInit {
    String cacheFilePathId();
    String dataField();
}
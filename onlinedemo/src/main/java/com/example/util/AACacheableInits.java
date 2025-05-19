package com.example.util;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AACacheableInits {
    AACacheableInit[] value();
}
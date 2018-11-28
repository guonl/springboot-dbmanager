package com.guonl.factory.test02;

import java.lang.annotation.*;

/**
 * Created by guonl
 * Date 2018/11/27 5:49 PM
 * Description:
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldAnnotation {

    String fieldName();

    FieldType fieldType();

    boolean pk();

}

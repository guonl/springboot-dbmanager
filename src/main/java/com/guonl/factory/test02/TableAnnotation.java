package com.guonl.factory.test02;

import java.lang.annotation.*;

/**
 * Created by guonl
 * Date 2018/11/27 5:51 PM
 * Description:
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableAnnotation {

    String tableName();


}

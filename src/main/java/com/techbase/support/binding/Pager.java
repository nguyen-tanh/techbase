package com.techbase.support.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nguyentanh
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Pager {
    /**
     * Query parameter for identify current page
     */
    String page() default "page";

    /**
     * Query parameter for identify sizing of page
     */
    String size() default "size";
}

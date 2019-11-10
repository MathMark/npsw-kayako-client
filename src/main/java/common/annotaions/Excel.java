package common.annotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {
    int cellNum() default 0; //used only when read .xlsx file and map to object
    String columnName() default "";
    boolean isCustomObject() default false;
}

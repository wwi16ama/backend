package com.WWI16AMA.backend_api.PlaneLog;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {ValiStartAndEndcountValidator.class})
public @interface ValidStartAndEndcount {

    String message() default "startCount has to be greater than endCount";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

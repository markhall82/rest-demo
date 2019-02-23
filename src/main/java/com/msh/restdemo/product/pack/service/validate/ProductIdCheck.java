package com.msh.restdemo.product.pack.service.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ProductIdValidator.class)
@Documented
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RUNTIME)
public @interface ProductIdCheck {
    String message() default "Invalid Product Id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

package com.sparta.usinsa.presentation.common.annotation;

import com.sparta.usinsa.presentation.common.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPasswordPatten {
  String message() default "영문+숫자+특수문자가 1글자 포함되어야합니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}


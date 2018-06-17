package com.licenta2018.spring.security;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(AllowedForApprovingAudiences.condition)
public @interface AllowedForApprovingAudiences {
	String condition = "@mySecurityService.canApproveAudiences(principal)";
}

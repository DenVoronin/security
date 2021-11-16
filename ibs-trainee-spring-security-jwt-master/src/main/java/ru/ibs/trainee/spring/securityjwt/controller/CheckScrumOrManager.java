package ru.ibs.trainee.spring.securityjwt.controller;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('MANAGER') " +
        "|| hasRole('SCRUM_MASTER')")
public @interface CheckScrumOrManager {
}


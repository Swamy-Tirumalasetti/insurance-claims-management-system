package com.aflac.Java_Aws_Aflac_Application.dto;

public record CustomerResponse(
        Long id,
        String name,
        String email,
        String phone
) {}
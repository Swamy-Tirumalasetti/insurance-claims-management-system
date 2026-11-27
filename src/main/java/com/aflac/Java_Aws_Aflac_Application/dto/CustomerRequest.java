package com.aflac.Java_Aws_Aflac_Application.dto;

public record CustomerRequest(
        String name,
        String email,
        String phone
) {
}
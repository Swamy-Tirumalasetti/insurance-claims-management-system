package com.aflac.Java_Aws_Aflac_Application.dto;

import java.math.BigDecimal;

public record PolicyRequest(
        String policyNumber,
        String policyType,
        BigDecimal coverageAmount,
        Long customerId
) {
}
package com.aflac.Java_Aws_Aflac_Application.dto;

import java.math.BigDecimal;

public record ClaimResponse(
        Long id,
        String claimNumber,
        String description,
        BigDecimal claimedAmount,
        String status,
        Long policyId,
        String policyNumber
) {}
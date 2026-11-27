package com.aflac.Java_Aws_Aflac_Application.dto;

import java.math.BigDecimal;

public record ClaimRequest(
        String claimNumber,
        String description,
        BigDecimal claimedAmount,
        Long policyId
) {}
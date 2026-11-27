package com.aflac.Java_Aws_Aflac_Application.service;

import com.aflac.Java_Aws_Aflac_Application.dto.ClaimRequest;
import com.aflac.Java_Aws_Aflac_Application.dto.ClaimResponse;
import com.aflac.Java_Aws_Aflac_Application.entity.Claim;
import com.aflac.Java_Aws_Aflac_Application.entity.Policy;
import com.aflac.Java_Aws_Aflac_Application.repository.ClaimRepository;
import com.aflac.Java_Aws_Aflac_Application.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;

    @Transactional
    public ClaimResponse submitClaim(ClaimRequest request) {
        Policy policy = policyRepository.findById(request.policyId())
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + request.policyId()));

        if (request.claimedAmount().compareTo(policy.getCoverageAmount()) > 0) {
            throw new IllegalArgumentException(
                    "Claimed amount (" + request.claimedAmount() + ") exceeds policy coverage (" + policy.getCoverageAmount() + ")"
            );
        }

        Claim claim = Claim.builder()
                .claimNumber(request.claimNumber())
                .description(request.description())
                .claimedAmount(request.claimedAmount())
                .status("SUBMITTED")
                .policy(policy)
                .build();

        Claim saved = claimRepository.save(claim);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ClaimResponse getClaimById(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found with id: " + id));
        return toResponse(claim);
    }

    @Transactional(readOnly = true)
    public List<ClaimResponse> getAllClaims() {
        return claimRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ClaimResponse toResponse(Claim claim) {
        return new ClaimResponse(
                claim.getId(),
                claim.getClaimNumber(),
                claim.getDescription(),
                claim.getClaimedAmount(),
                claim.getStatus(),
                claim.getPolicy().getId(),
                claim.getPolicy().getPolicyNumber()
        );
    }
}
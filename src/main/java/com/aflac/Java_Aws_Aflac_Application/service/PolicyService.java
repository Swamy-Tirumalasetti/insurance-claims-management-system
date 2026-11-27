package com.aflac.Java_Aws_Aflac_Application.service;

import com.aflac.Java_Aws_Aflac_Application.dto.PolicyRequest;
import com.aflac.Java_Aws_Aflac_Application.dto.PolicyResponse;
import com.aflac.Java_Aws_Aflac_Application.entity.Customer;
import com.aflac.Java_Aws_Aflac_Application.entity.Policy;
import com.aflac.Java_Aws_Aflac_Application.repository.CustomerRepository;
import com.aflac.Java_Aws_Aflac_Application.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public PolicyResponse createPolicy(PolicyRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + request.customerId()));

        Policy policy = Policy.builder()
                .policyNumber(request.policyNumber())
                .policyType(request.policyType())
                .coverageAmount(request.coverageAmount())
                .customer(customer)
                .build();

        Policy saved = policyRepository.save(policy);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PolicyResponse getPolicyById(Long id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + id));
        return toResponse(policy);
    }

    private PolicyResponse toResponse(Policy policy) {
        return new PolicyResponse(
                policy.getId(),
                policy.getPolicyNumber(),
                policy.getPolicyType(),
                policy.getCoverageAmount(),
                policy.getCustomer().getId(),
                policy.getCustomer().getName()
        );
    }
}
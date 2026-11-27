package com.aflac.Java_Aws_Aflac_Application.controller;

import com.aflac.Java_Aws_Aflac_Application.dto.PolicyRequest;
import com.aflac.Java_Aws_Aflac_Application.dto.PolicyResponse;
import com.aflac.Java_Aws_Aflac_Application.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping
    public ResponseEntity<PolicyResponse> createPolicy(@RequestBody PolicyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(policyService.createPolicy(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> getPolicy(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getPolicyById(id));
    }
}
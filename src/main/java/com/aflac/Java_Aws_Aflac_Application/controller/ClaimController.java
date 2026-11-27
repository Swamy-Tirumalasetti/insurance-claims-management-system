package com.aflac.Java_Aws_Aflac_Application.controller;

import com.aflac.Java_Aws_Aflac_Application.dto.ClaimRequest;
import com.aflac.Java_Aws_Aflac_Application.dto.ClaimResponse;
import com.aflac.Java_Aws_Aflac_Application.service.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping
    public ResponseEntity<ClaimResponse> submitClaim(@RequestBody ClaimRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(claimService.submitClaim(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponse> getClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClaimResponse>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAllClaims());
    }
}
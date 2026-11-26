package com.aflac.Java_Aws_Aflac_Application.repository;

import com.aflac.Java_Aws_Aflac_Application.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
}

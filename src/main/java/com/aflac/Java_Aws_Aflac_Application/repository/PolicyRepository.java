package com.aflac.Java_Aws_Aflac_Application.repository;


import com.aflac.Java_Aws_Aflac_Application.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
}
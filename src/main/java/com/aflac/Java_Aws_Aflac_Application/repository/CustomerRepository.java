package com.aflac.Java_Aws_Aflac_Application.repository;


import com.aflac.Java_Aws_Aflac_Application.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
package com.aflac.Java_Aws_Aflac_Application.service;

import com.aflac.Java_Aws_Aflac_Application.dto.CustomerRequest;
import com.aflac.Java_Aws_Aflac_Application.dto.CustomerResponse;
import com.aflac.Java_Aws_Aflac_Application.entity.Customer;
import com.aflac.Java_Aws_Aflac_Application.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .build();

        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return toResponse(customer);
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }
}
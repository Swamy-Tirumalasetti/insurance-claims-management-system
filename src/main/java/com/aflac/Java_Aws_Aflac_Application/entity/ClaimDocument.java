package com.aflac.Java_Aws_Aflac_Application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "claim_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String s3Bucket;

    @Column(nullable = false, unique = true)
    private String s3Key;

    @Column(nullable = false)
    private String processingStatus; // UPLOADED, PROCESSED, FAILED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;
}
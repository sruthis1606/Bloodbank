package com.example.bloodbank.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class IssueRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    private LocalDate issueDate;

    @Positive(message = "Units issued must be greater than zero")
    private int unitsIssued;

    @NotNull(message = "Blood unit is required")
    @OneToOne
    @JoinColumn(name = "blood_unit_id", nullable = false)
    private BloodUnit bloodUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public int getUnitsIssued() {
        return unitsIssued;
    }

    public void setUnitsIssued(int unitsIssued) {
        this.unitsIssued = unitsIssued;
    }

    public BloodUnit getBloodUnit() {
        return bloodUnit;
    }

    public void setBloodUnit(BloodUnit bloodUnit) {
        this.bloodUnit = bloodUnit;
    }
}
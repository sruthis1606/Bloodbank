package com.example.bloodbank.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloodbank.model.BloodUnit;
import com.example.bloodbank.model.IssueRecord;
import com.example.bloodbank.repository.BloodUnitRepository;
import com.example.bloodbank.repository.IssueRecordRepository;

@Service
public class IssueRecordService {

    private final IssueRecordRepository issueRecordRepository;
    private final BloodUnitRepository bloodUnitRepository;

    private static final int NEAR_EXPIRY_DAYS = 7;

    public IssueRecordService(
            IssueRecordRepository issueRecordRepository,
            BloodUnitRepository bloodUnitRepository) {

        this.issueRecordRepository = issueRecordRepository;
        this.bloodUnitRepository = bloodUnitRepository;
    }

    @Transactional
    public IssueRecord issueBloodUnit(IssueRecord issueRecord) {

        if (issueRecord.getBloodUnit() == null ||
                issueRecord.getBloodUnit().getId() == null) {

            throw new RuntimeException("Valid blood unit ID is required");
        }

        BloodUnit bloodUnit = bloodUnitRepository.findById(
                issueRecord.getBloodUnit().getId()
        ).orElseThrow(() ->
                new RuntimeException("Blood unit not found")
        );

        if (!"AVAILABLE".equals(bloodUnit.getStatus())) {
            throw new RuntimeException("Blood unit is not available");
        }

        LocalDate today = LocalDate.now();

        if (bloodUnit.getExpiryDate() == null) {
            throw new RuntimeException("Blood unit expiry date is required");
        }

        if (!bloodUnit.getExpiryDate().isAfter(
                today.plusDays(NEAR_EXPIRY_DAYS))) {

            throw new RuntimeException(
                    "Cannot issue expired or near-expiry blood unit"
            );
        }

        if (issueRecord.getUnitsIssued() <= 0) {
            throw new RuntimeException(
                    "Issued units must be greater than zero"
            );
        }

        if (issueRecord.getUnitsIssued() > bloodUnit.getUnits()) {
            throw new RuntimeException(
                    "Not enough blood units available"
            );
        }

        bloodUnit.setUnits(
                bloodUnit.getUnits() - issueRecord.getUnitsIssued()
        );

        if (bloodUnit.getUnits() == 0) {
            bloodUnit.setStatus("ISSUED");
        }

        bloodUnitRepository.save(bloodUnit);

        issueRecord.setBloodUnit(bloodUnit);
        issueRecord.setIssueDate(today);

        return issueRecordRepository.save(issueRecord);
    }

    public List<IssueRecord> getAllIssueRecords() {
        return issueRecordRepository.findAll();
    }

    public IssueRecord getIssueRecordById(Long id) {
        return issueRecordRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Issue record not found")
                );
    }

    @Transactional
    public void deleteIssueRecord(Long id) {

        IssueRecord issueRecord = getIssueRecordById(id);

        issueRecordRepository.delete(issueRecord);
    }
}
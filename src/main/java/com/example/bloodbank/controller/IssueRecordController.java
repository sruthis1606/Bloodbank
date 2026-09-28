package com.example.bloodbank.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bloodbank.model.IssueRecord;
import com.example.bloodbank.service.IssueRecordService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/issues")
public class IssueRecordController {

    private final IssueRecordService issueRecordService;

    public IssueRecordController(IssueRecordService issueRecordService) {
        this.issueRecordService = issueRecordService;
    }

    @PostMapping
    public ResponseEntity<IssueRecord> issueBloodUnit(
            @Valid @RequestBody IssueRecord issueRecord) {

        return ResponseEntity.ok(
                issueRecordService.issueBloodUnit(issueRecord)
        );
    }

    @GetMapping
    public ResponseEntity<List<IssueRecord>> getAllIssueRecords() {

        return ResponseEntity.ok(
                issueRecordService.getAllIssueRecords()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueRecord> getIssueRecordById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                issueRecordService.getIssueRecordById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIssueRecord(
            @PathVariable Long id) {

        issueRecordService.deleteIssueRecord(id);

        return ResponseEntity.ok(
                "Issue record deleted successfully"
        );
    }
}
package com.example.bloodbank.repository;

import com.example.bloodbank.model.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {

}
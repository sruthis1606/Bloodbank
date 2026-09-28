package com.example.bloodbank.repository;

import com.example.bloodbank.model.BloodUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long> {

}
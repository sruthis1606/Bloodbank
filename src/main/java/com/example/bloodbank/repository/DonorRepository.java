package com.example.bloodbank.repository;

import com.example.bloodbank.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository extends JpaRepository<Donor, Long> {

}
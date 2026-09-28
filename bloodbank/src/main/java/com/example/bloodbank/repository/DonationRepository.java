package com.example.bloodbank.repository;

import com.example.bloodbank.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {

}
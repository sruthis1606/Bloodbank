package com.example.bloodbank.service;

import com.example.bloodbank.model.Donor;
import com.example.bloodbank.repository.DonorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DonorService {

    private final DonorRepository donorRepository;

    private static final long MIN_DONATION_GAP_DAYS = 90;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Donor addDonor(Donor donor) {
        return donorRepository.save(donor);
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public Donor getDonorById(Long id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
    }

    public Donor updateDonor(Long id, Donor donor) {

        Donor existingDonor = getDonorById(id);

        existingDonor.setName(donor.getName());
        existingDonor.setBloodGroup(donor.getBloodGroup());
        existingDonor.setAge(donor.getAge());
        existingDonor.setPhone(donor.getPhone());

        return donorRepository.save(existingDonor);
    }

    public void deleteDonor(Long id) {
        donorRepository.deleteById(id);
    }

    public boolean checkEligibility(Long id) {

        Donor donor = getDonorById(id);

        if (donor.getLastDonationDate() == null) {
            return true;
        }

        long daysSinceLastDonation = ChronoUnit.DAYS.between(
                donor.getLastDonationDate(),
                LocalDate.now()
        );

        return daysSinceLastDonation >= MIN_DONATION_GAP_DAYS;
    }
}
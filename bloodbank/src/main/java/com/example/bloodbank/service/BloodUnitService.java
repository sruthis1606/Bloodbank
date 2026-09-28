package com.example.bloodbank.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloodbank.model.BloodUnit;
import com.example.bloodbank.model.Donation;
import com.example.bloodbank.repository.BloodUnitRepository;
import com.example.bloodbank.repository.DonationRepository;

@Service
public class BloodUnitService {

    private final BloodUnitRepository bloodUnitRepository;
    private final DonationRepository donationRepository;

    private static final int NEAR_EXPIRY_DAYS = 7;

    public BloodUnitService(
            BloodUnitRepository bloodUnitRepository,
            DonationRepository donationRepository) {

        this.bloodUnitRepository = bloodUnitRepository;
        this.donationRepository = donationRepository;
    }

    @Transactional
    public BloodUnit addBloodUnit(BloodUnit bloodUnit) {

        validateDates(bloodUnit);

        if (bloodUnit.getDonation() == null ||
                bloodUnit.getDonation().getId() == null) {

            throw new RuntimeException("Valid donation ID is required");
        }

        Donation donation = donationRepository.findById(
                bloodUnit.getDonation().getId()
        ).orElseThrow(() ->
                new RuntimeException("Donation not found")
        );

        bloodUnit.setDonation(donation);
        bloodUnit.setStatus("AVAILABLE");

        return bloodUnitRepository.save(bloodUnit);
    }

    public List<BloodUnit> getAllBloodUnits() {

        return bloodUnitRepository.findAll();
    }

    public BloodUnit getBloodUnitById(Long id) {

        return bloodUnitRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Blood unit not found")
                );
    }

    @Transactional
    public BloodUnit updateBloodUnit(
            Long id,
            BloodUnit bloodUnit) {

        BloodUnit existingUnit = getBloodUnitById(id);

        validateDates(bloodUnit);

        if (bloodUnit.getDonation() == null ||
                bloodUnit.getDonation().getId() == null) {

            throw new RuntimeException("Valid donation ID is required");
        }

        Donation donation = donationRepository.findById(
                bloodUnit.getDonation().getId()
        ).orElseThrow(() ->
                new RuntimeException("Donation not found")
        );

        existingUnit.setBloodGroup(
                bloodUnit.getBloodGroup()
        );

        existingUnit.setCollectionDate(
                bloodUnit.getCollectionDate()
        );

        existingUnit.setExpiryDate(
                bloodUnit.getExpiryDate()
        );

        existingUnit.setUnits(
                bloodUnit.getUnits()
        );

        existingUnit.setDonation(donation);

        if (existingUnit.getUnits() > 0) {
            existingUnit.setStatus("AVAILABLE");
        }

        return bloodUnitRepository.save(existingUnit);
    }

    @Transactional
    public void deleteBloodUnit(Long id) {

        BloodUnit bloodUnit = getBloodUnitById(id);

        bloodUnitRepository.delete(bloodUnit);
    }

    public List<BloodUnit> getNearExpiryUnits() {

        LocalDate today = LocalDate.now();

        LocalDate sevenDaysLater =
                today.plusDays(NEAR_EXPIRY_DAYS);

        return bloodUnitRepository.findAll()
                .stream()
                .filter(unit ->
                        unit.getExpiryDate() != null
                )
                .filter(unit ->
                        !unit.getExpiryDate().isBefore(today)
                )
                .filter(unit ->
                        !unit.getExpiryDate().isAfter(sevenDaysLater)
                )
                .filter(unit ->
                        "AVAILABLE".equals(unit.getStatus())
                )
                .toList();
    }

    public List<BloodUnit> getAvailableUnitsByBloodGroup(
            String bloodGroup) {

        LocalDate today = LocalDate.now();

        return bloodUnitRepository.findAll()
                .stream()
                .filter(unit ->
                        unit.getBloodGroup() != null
                )
                .filter(unit ->
                        unit.getBloodGroup()
                                .equalsIgnoreCase(bloodGroup)
                )
                .filter(unit ->
                        "AVAILABLE".equals(unit.getStatus())
                )
                .filter(unit ->
                        unit.getExpiryDate() != null
                )
                .filter(unit ->
                        unit.getExpiryDate().isAfter(
                                today.plusDays(NEAR_EXPIRY_DAYS)
                        )
                )
                .toList();
    }

    private void validateDates(BloodUnit bloodUnit) {

        if (bloodUnit.getCollectionDate() == null) {
            throw new RuntimeException(
                    "Collection date is required"
            );
        }

        if (bloodUnit.getExpiryDate() == null) {
            throw new RuntimeException(
                    "Expiry date is required"
            );
        }

        if (bloodUnit.getExpiryDate()
                .isBefore(bloodUnit.getCollectionDate())) {

            throw new RuntimeException(
                    "Expiry date cannot be before collection date"
            );
        }
    }
}
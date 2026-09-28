package com.example.bloodbank.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloodbank.model.Donation;
import com.example.bloodbank.model.Donor;
import com.example.bloodbank.repository.DonationRepository;
import com.example.bloodbank.repository.DonorRepository;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;

    private static final long MIN_DONATION_GAP_DAYS = 90;

    public DonationService(
            DonationRepository donationRepository,
            DonorRepository donorRepository) {

        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
    }

    @Transactional
    public Donation addDonation(Donation donation) {

        if (donation.getDonationDate() == null) {
            throw new RuntimeException("Donation date is required");
        }

        if (donation.getDonationDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Donation date cannot be in the future");
        }

        if (donation.getDonor() == null ||
                donation.getDonor().getId() == null) {

            throw new RuntimeException("Valid donor ID is required");
        }

        Donor donor = donorRepository.findById(
                donation.getDonor().getId()
        ).orElseThrow(() ->
                new RuntimeException("Donor not found")
        );

        LocalDate lastDonationDate =
                findLatestDonationDateForDonor(donor.getId(), null);

        if (donor.getLastDonationDate() != null &&
                (lastDonationDate == null ||
                 donor.getLastDonationDate().isAfter(lastDonationDate))) {

            lastDonationDate = donor.getLastDonationDate();
        }

        if (lastDonationDate != null) {

            long daysBetween = ChronoUnit.DAYS.between(
                    lastDonationDate,
                    donation.getDonationDate()
            );

            if (daysBetween < MIN_DONATION_GAP_DAYS) {
                throw new RuntimeException(
                        "Donation rejected. Donor must wait at least "
                        + MIN_DONATION_GAP_DAYS
                        + " days between donations."
                );
            }
        }

        donation.setDonor(donor);

        Donation savedDonation = donationRepository.save(donation);

        donor.setLastDonationDate(
                donation.getDonationDate()
        );

        donorRepository.save(donor);

        return savedDonation;
    }

    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    public Donation getDonationById(Long id) {

        return donationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Donation not found")
                );
    }

    @Transactional
    public Donation updateDonation(Long id, Donation donation) {

        Donation existingDonation = getDonationById(id);

        if (donation.getDonationDate() == null) {
            throw new RuntimeException("Donation date is required");
        }

        if (donation.getDonationDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Donation date cannot be in the future");
        }

        if (donation.getDonor() == null ||
                donation.getDonor().getId() == null) {

            throw new RuntimeException("Valid donor ID is required");
        }

        Donor newDonor = donorRepository.findById(
                donation.getDonor().getId()
        ).orElseThrow(() ->
                new RuntimeException("Donor not found")
        );

        LocalDate lastDonationDate =
                findLatestDonationDateForDonor(
                        newDonor.getId(),
                        existingDonation.getId()
                );

        if (lastDonationDate != null) {

            long daysBetween = ChronoUnit.DAYS.between(
                    lastDonationDate,
                    donation.getDonationDate()
            );

            if (daysBetween < MIN_DONATION_GAP_DAYS) {
                throw new RuntimeException(
                        "Donation update rejected. "
                        + "Minimum donation gap of "
                        + MIN_DONATION_GAP_DAYS
                        + " days has not been completed."
                );
            }
        }

        Donor oldDonor = existingDonation.getDonor();

        existingDonation.setDonationDate(
                donation.getDonationDate()
        );

        existingDonation.setUnits(
                donation.getUnits()
        );

        existingDonation.setDonor(newDonor);

        Donation updatedDonation =
                donationRepository.save(existingDonation);

        updateLastDonationDate(oldDonor);
        updateLastDonationDate(newDonor);

        return updatedDonation;
    }

    @Transactional
    public void deleteDonation(Long id) {

        Donation donation = getDonationById(id);

        Donor donor = donation.getDonor();

        donationRepository.delete(donation);

        if (donor != null) {
            updateLastDonationDate(donor);
        }
    }

    private LocalDate findLatestDonationDateForDonor(
            Long donorId,
            Long excludedDonationId) {

        return donationRepository.findAll()
                .stream()
                .filter(d -> d.getDonor() != null)
                .filter(d ->
                        Objects.equals(
                                d.getDonor().getId(),
                                donorId
                        )
                )
                .filter(d ->
                        excludedDonationId == null ||
                        !Objects.equals(
                                d.getId(),
                                excludedDonationId
                        )
                )
                .map(Donation::getDonationDate)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .orElse(null);
    }

    private void updateLastDonationDate(Donor donor) {

        LocalDate latestDate =
                findLatestDonationDateForDonor(
                        donor.getId(),
                        null
                );

        donor.setLastDonationDate(latestDate);

        donorRepository.save(donor);
    }
}
package com.example.bloodbank.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bloodbank.model.Donor;
import com.example.bloodbank.service.DonorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/donors")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping
    public ResponseEntity<Donor> addDonor(
            @Valid @RequestBody Donor donor) {

        return ResponseEntity.ok(donorService.addDonor(donor));
    }

    @GetMapping
    public ResponseEntity<List<Donor>> getAllDonors() {

        return ResponseEntity.ok(donorService.getAllDonors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donor> getDonorById(
            @PathVariable Long id) {

        return ResponseEntity.ok(donorService.getDonorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donor> updateDonor(
            @PathVariable Long id,
            @Valid @RequestBody Donor donor) {

        return ResponseEntity.ok(
                donorService.updateDonor(id, donor)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDonor(
            @PathVariable Long id) {

        donorService.deleteDonor(id);

        return ResponseEntity.ok(
                "Donor deleted successfully"
        );
    }

    @GetMapping("/{id}/eligibility")
    public ResponseEntity<String> checkEligibility(
            @PathVariable Long id) {

        boolean eligible = donorService.checkEligibility(id);

        if (eligible) {
            return ResponseEntity.ok(
                    "Donor is eligible to donate"
            );
        }

        return ResponseEntity.badRequest().body(
                "Donor is not eligible to donate. Minimum donation gap has not been completed."
        );
    }
}
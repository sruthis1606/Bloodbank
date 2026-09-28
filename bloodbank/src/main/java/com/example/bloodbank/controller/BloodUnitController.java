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

import com.example.bloodbank.model.BloodUnit;
import com.example.bloodbank.service.BloodUnitService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/blood-units")
public class BloodUnitController {

    private final BloodUnitService bloodUnitService;

    public BloodUnitController(BloodUnitService bloodUnitService) {
        this.bloodUnitService = bloodUnitService;
    }

    @PostMapping
    public ResponseEntity<BloodUnit> addBloodUnit(
            @Valid @RequestBody BloodUnit bloodUnit) {

        return ResponseEntity.ok(
                bloodUnitService.addBloodUnit(bloodUnit)
        );
    }

    @GetMapping
    public ResponseEntity<List<BloodUnit>> getAllBloodUnits() {

        return ResponseEntity.ok(
                bloodUnitService.getAllBloodUnits()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodUnit> getBloodUnitById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bloodUnitService.getBloodUnitById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BloodUnit> updateBloodUnit(
            @PathVariable Long id,
            @Valid @RequestBody BloodUnit bloodUnit) {

        return ResponseEntity.ok(
                bloodUnitService.updateBloodUnit(id, bloodUnit)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBloodUnit(
            @PathVariable Long id) {

        bloodUnitService.deleteBloodUnit(id);

        return ResponseEntity.ok(
                "Blood unit deleted successfully"
        );
    }

    @GetMapping("/near-expiry")
    public ResponseEntity<List<BloodUnit>> getNearExpiryUnits() {

        return ResponseEntity.ok(
                bloodUnitService.getNearExpiryUnits()
        );
    }

    @GetMapping("/stock/{bloodGroup}")
    public ResponseEntity<List<BloodUnit>> getAvailableUnitsByBloodGroup(
            @PathVariable String bloodGroup) {

        return ResponseEntity.ok(
                bloodUnitService.getAvailableUnitsByBloodGroup(bloodGroup)
        );
    }
}
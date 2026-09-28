@DeleteMapping("/{id}")
public ResponseEntity<String> deleteDonation(
        @PathVariable Long id) {

    donationService.deleteDonation(id);

    return ResponseEntity.ok(
            "Donation deleted successfully"
    );
}
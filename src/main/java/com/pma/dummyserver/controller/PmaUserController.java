package com.pma.dummyserver.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pma.dummyserver.enums.Classification;
import com.pma.dummyserver.model.PmaUser;
import com.pma.dummyserver.repository.PmaUserRepository;
import com.pma.dummyserver.service.PmaUserService;

@RestController
@RequestMapping("/api/pma-users")
public class PmaUserController {

    @Autowired
    private PmaUserRepository pmaUserRepository;

    @Autowired
    private PmaUserService pmaUserService;

    @GetMapping
    public List<PmaUser> getAllPmaUsers() {
        return pmaUserRepository.findAll();
    }

    @GetMapping("/classification/{cardNumber}")
    public ResponseEntity<?> getClassificationByCardNumber(@PathVariable("cardNumber") String cardNumber) {
        try {
            Classification classification = pmaUserService.getClassificationByCardNumber(cardNumber);
            return ResponseEntity.ok(classification);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update-shayyikli")
    public ResponseEntity<?> updateShayyikliNumber(@RequestParam String cardNumber,
            @RequestParam Integer shayyikliAccountNumber) {
        Optional<PmaUser> userOptional = pmaUserRepository.findByCardNumber(cardNumber);

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User with the given card number not found.");
        }

        PmaUser user = userOptional.get();
        user.setShayyikliAccountNumber(shayyikliAccountNumber);
        pmaUserRepository.save(user);

        return ResponseEntity.ok("Shayyikli account number updated successfully.");
    }

    @GetMapping("/returned-checks/{shayyikliAccountNumber}")
    public ResponseEntity<?> getReturnedChecksByshayyikliAccountNumber(@PathVariable("shayyikliAccountNumber") Integer shayyikliAccountNumber) {
        PmaUser user = pmaUserRepository.findByshayyikliAccountNumber(shayyikliAccountNumber)
                .orElseThrow(() -> new RuntimeException("User with card number " + shayyikliAccountNumber + " not found"));
        return ResponseEntity.ok(user.getReturnedChecks());
    }

    @PutMapping("/recalculate/{shayyikliAccountNumber}")
    public ResponseEntity<?> recalculateReturnedChecksAndClassification(@PathVariable("shayyikliAccountNumber") Integer shayyikliAccountNumber) {
        try {
            PmaUser updatedUser = pmaUserService.incrementReturnedChecksAndRecalculate(shayyikliAccountNumber);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{cardNumber}/update-last-returned-check-date")
    public ResponseEntity<?> updateLastReturnedCheckDate(
            @PathVariable("cardNumber") String cardNumber,
            @RequestParam("lastReturnedCheckDate") String lastReturnedCheckDateStr) {
        try {
            String decodedDateStr = java.net.URLDecoder.decode(lastReturnedCheckDateStr,
                    java.nio.charset.StandardCharsets.UTF_8.toString());
            LocalDateTime newDate = LocalDateTime.parse(decodedDateStr);
            PmaUser updatedUser = pmaUserService.updateLastReturnedCheckDate(cardNumber, newDate);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating lastReturnedCheckDate: " + e.getMessage());
        }
    }

    @PutMapping("/reset/{shayyikliAccountNumber}")
    public ResponseEntity<?> resetUserReturnedChecksAndClassification(@PathVariable("shayyikliAccountNumber") Integer shayyikliAccountNumber) {
        try {
            PmaUser updatedUser = pmaUserService.resetReturnedChecksAndClassification(shayyikliAccountNumber);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

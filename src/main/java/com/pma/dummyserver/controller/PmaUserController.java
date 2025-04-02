package com.pma.dummyserver.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    private PmaUserRepository pmaUserRepository;

    private PmaUserService pmaUserService;

    public PmaUserController(PmaUserRepository pmaUserRepository, PmaUserService pmaUserService) {
        this.pmaUserRepository = pmaUserRepository;
        this.pmaUserService = pmaUserService;
    }

    @GetMapping
    public List<PmaUser> getAllPmaUsers() {
        return pmaUserRepository.findAll();
    }

    @GetMapping("/classification/{shayyikliAccountNumber}")
    public ResponseEntity<Classification> getClassificationByShayyikliAccountNumber(
            @PathVariable("shayyikliAccountNumber") Integer shayyikliAccountNumber) {
        Classification classification = pmaUserService
                .getClassificationByShayyikliAccountNumber(shayyikliAccountNumber);
        return ResponseEntity.ok(classification);
    }

    @PutMapping("/update-shayyikli")
    public ResponseEntity<String> updateShayyikliNumber(@RequestParam String cardNumber,
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
    public ResponseEntity<Integer> getReturnedChecksByshayyikliAccountNumber(
            @PathVariable("shayyikliAccountNumber") Integer shayyikliAccountNumber) {
        PmaUser user = pmaUserRepository.findByshayyikliAccountNumber(shayyikliAccountNumber)
                .orElseThrow(
                        () -> new RuntimeException("User with card number " + shayyikliAccountNumber + " not found"));
        return ResponseEntity.ok(user.getReturnedChecks());
    }

    @PutMapping("/recalculate/{shayyikliAccountNumber}")
    public ResponseEntity<PmaUser> recalculateReturnedChecksAndClassification(
            @PathVariable("shayyikliAccountNumber") Integer shayyikliAccountNumber) {
        PmaUser updatedUser = pmaUserService.incrementReturnedChecksAndRecalculate(shayyikliAccountNumber);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{shayyikliAccountNumber}/update-last-returned-check-date")
    public ResponseEntity<PmaUser> updateLastReturnedCheckDate(
            @PathVariable("shayyikliAccountNumber") Integer shayyikliAccountNumber,
            @RequestParam("lastReturnedCheckDate") String lastReturnedCheckDateStr)
            throws UnsupportedEncodingException {
        String decodedDateStr = java.net.URLDecoder.decode(lastReturnedCheckDateStr,
                java.nio.charset.StandardCharsets.UTF_8.toString());
        LocalDateTime newDate = LocalDateTime.parse(decodedDateStr);
        PmaUser updatedUser = pmaUserService.updateLastReturnedCheckDateByAccountNumber(shayyikliAccountNumber,
                newDate);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/reset/{shayyikliAccountNumber}")
    public ResponseEntity<PmaUser> resetUserReturnedChecksAndClassification(
            @PathVariable("shayyikliAccountNumber") Integer shayyikliAccountNumber) {
        PmaUser updatedUser = pmaUserService.resetReturnedChecksAndClassification(shayyikliAccountNumber);
        return ResponseEntity.ok(updatedUser);
    }
}

package com.pma.dummyserver.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pma.dummyserver.enums.Classification;
import com.pma.dummyserver.model.ClassificationHistory;
import com.pma.dummyserver.model.PmaUser;
import com.pma.dummyserver.repository.ClassificationHistoryRepository;
import com.pma.dummyserver.repository.PmaUserRepository;

@Service
public class PmaUserService {

    @Autowired
    private PmaUserRepository pmaUserRepository;

    @Autowired
    private ClassificationHistoryRepository classificationHistoryRepository;

    private final String BANKING_API_URL = "http://localhost:8081/api/users/summary";
    private final Random random = new Random();

    public void fetchAndSaveUsers() {
        RestTemplate restTemplate = new RestTemplate();
        UserSummary[] users = restTemplate.getForObject(BANKING_API_URL, UserSummary[].class);

        if (users != null) {
            for (UserSummary userSummary : users) {
                int returnedChecks = generateRandomReturnedChecks();
                PmaUser pmaUser = new PmaUser(
                        userSummary.getIdNumber(),
                        userSummary.getFirstName(),
                        userSummary.getFamilyName(),
                        returnedChecks,
                        userSummary.getCardNumber());
                pmaUserRepository.save(pmaUser);
            }
        }
    }

    private int generateRandomReturnedChecks() {
        return random.nextInt(21);
    }

    private static class UserSummary {
        private String idNumber;
        private String firstName;
        private String familyName;
        private String cardNumber;

        public String getIdNumber() {
            return idNumber;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getFamilyName() {
            return familyName;
        }

        public String getCardNumber() {
            return cardNumber;
        }
    }

    public Classification getClassificationByCardNumber(String cardNumber) {
        PmaUser user = pmaUserRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("User with cardNumber " + cardNumber + " not found"));
        return user.getClassification();
    }

    public int getReturnedChecks(Long userId) {
        PmaUser user = pmaUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return user.getReturnedChecks();
    }

    public PmaUser incrementReturnedChecksAndRecalculate(Integer shayyikliAccountNumber) {
        // PmaUser user = pmaUserRepository.findByCardNumber(cardNumber)
        // .orElseThrow(() -> new RuntimeException("User with card number " + cardNumber
        // + " not found"));

        // Find the user by shayyikliAccountNumber

        PmaUser user = pmaUserRepository.findByshayyikliAccountNumber(shayyikliAccountNumber)
                .orElseThrow(() -> new RuntimeException(
                        "User with Shayyikli account number " + shayyikliAccountNumber + " not found"));

        int newReturnedChecks = user.getReturnedChecks() + 1;
        Classification oldClassification = user.getClassification();
        user.setReturnedChecks(newReturnedChecks);
        Classification newClassification = user.getClassification();
        pmaUserRepository.save(user);

        if (!oldClassification.equals(newClassification)) {
            // Pass the current returnedChecks value to the history
            ClassificationHistory history = new ClassificationHistory(
                    user,
                    LocalDateTime.now(),
                    oldClassification,
                    newClassification,
                    user.getReturnedChecks());
            classificationHistoryRepository.save(history);
        }
        return user;
    }

    public PmaUser updateLastReturnedCheckDate(String cardNumber, LocalDateTime newDate) {
        PmaUser user = pmaUserRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("User with card number " + cardNumber + " not found"));
        user.setLastReturnedCheckDate(newDate);
        return pmaUserRepository.save(user);
    }

    public PmaUser resetReturnedChecksAndClassification(Integer shayyikliAccountNumber) {
        PmaUser user = pmaUserRepository.findByshayyikliAccountNumber(shayyikliAccountNumber)
                .orElseThrow(() -> new RuntimeException("User with shayyikliAccountNumber" + shayyikliAccountNumber + " not found"));

        Classification oldClassification = user.getClassification();

        user.setReturnedChecks(0);

        if (!oldClassification.equals(user.getClassification())) {
            ClassificationHistory history = new ClassificationHistory(
                    user,
                    LocalDateTime.now(),
                    oldClassification,
                    user.getClassification(),
                    user.getReturnedChecks());
            classificationHistoryRepository.save(history);
        }

        return pmaUserRepository.save(user);
    }
}

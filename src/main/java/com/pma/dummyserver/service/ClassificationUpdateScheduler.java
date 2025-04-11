package com.pma.dummyserver.service;

import com.pma.dummyserver.enums.Classification;
import com.pma.dummyserver.model.PmaUser;
import com.pma.dummyserver.repository.PmaUserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ClassificationUpdateScheduler {

    private PmaUserRepository pmaUserRepository;

    public ClassificationUpdateScheduler(PmaUserRepository pmaUserRepository) {
        this.pmaUserRepository = pmaUserRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateInactiveUserClassifications() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime oneYearAgo = now.minusYears(1);
        List<PmaUser> users = pmaUserRepository.findByClassificationInAndLastReturnedCheckDateBefore(
                List.of(Classification.C, Classification.D), oneYearAgo);

        for (PmaUser user : users) {
            Classification oldClassification = user.getClassification();
            Classification newClassification = null;
            if (oldClassification == Classification.C) {
                newClassification = Classification.CO;
            } else if (oldClassification == Classification.D) {
                newClassification = Classification.DO;
            }
            if (newClassification != null) {
                user.setClassification(newClassification);
                pmaUserRepository.save(user);
            }
        }

        LocalDateTime twoYearsAgo = now.minusYears(2);
        List<PmaUser> coUsers = pmaUserRepository.findByClassificationAndLastReturnedCheckDateBefore(
                Classification.CO, twoYearsAgo);
        for (PmaUser user : coUsers) {
            Classification newClassification = Classification.A;

            user.setClassification(newClassification);
            pmaUserRepository.save(user);
        }

        LocalDateTime threeYearsAgo = now.minusYears(3);
        List<PmaUser> doUsers = pmaUserRepository.findByClassificationAndLastReturnedCheckDateBefore(
                Classification.DO, threeYearsAgo);
        for (PmaUser user : doUsers) {
            Classification newClassification = Classification.A;

            user.setClassification(newClassification);
            pmaUserRepository.save(user);
        }
    }
}
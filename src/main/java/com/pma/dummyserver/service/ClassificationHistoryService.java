package com.pma.dummyserver.service;

import com.pma.dummyserver.model.ClassificationHistory;
import com.pma.dummyserver.model.PmaUser;
import com.pma.dummyserver.repository.ClassificationHistoryRepository;
import com.pma.dummyserver.repository.PmaUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassificationHistoryService {

    private ClassificationHistoryRepository classificationHistoryRepository;

    private PmaUserRepository pmaUserRepository;

    public List<ClassificationHistory> getHistoryByUserId(Integer shayyikliAccountNumber) {
        PmaUser user = pmaUserRepository.findByshayyikliAccountNumber(shayyikliAccountNumber)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with id Integer shayyikliAccountNumber: " + shayyikliAccountNumber));
        return classificationHistoryRepository.findByPmaUser(user);
    }

}

package com.pma.dummyserver.repository;

import com.pma.dummyserver.model.ClassificationHistory;
import com.pma.dummyserver.model.PmaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClassificationHistoryRepository extends JpaRepository<ClassificationHistory, Long> {
    List<ClassificationHistory> findByPmaUser(PmaUser pmaUser);
}

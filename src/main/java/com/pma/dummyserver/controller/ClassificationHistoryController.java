package com.pma.dummyserver.controller;

import com.pma.dummyserver.model.ClassificationHistory;
import com.pma.dummyserver.service.ClassificationHistoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pma-users")
public class ClassificationHistoryController {

    private ClassificationHistoryService classificationHistoryService;

    public ClassificationHistoryController(ClassificationHistoryService classificationHistoryService) {
        this.classificationHistoryService = classificationHistoryService;
    }

    @GetMapping("/{shayyikliAccountNumber}/classification-history")
    public List<ClassificationHistory> getUserClassificationHistory(@PathVariable Integer shayyikliAccountNumber) {
        return classificationHistoryService.getHistoryByUserId(shayyikliAccountNumber);
    }
}

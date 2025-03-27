package com.pma.dummyserver.model;

import com.pma.dummyserver.enums.Classification;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "classification_history")
public class ClassificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pma_user_id", nullable = false)
    private PmaUser pmaUser;

    @Column(nullable = false)
    private LocalDateTime changeDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Classification previousClassification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Classification newClassification;

    @Column(name = "returned_checks", nullable = false)
    private int returnedChecks;

    public ClassificationHistory() {
    }

    public ClassificationHistory(PmaUser pmaUser, LocalDateTime changeDate,
            Classification previousClassification,
            Classification newClassification,
            int returnedChecks) {
        this.pmaUser = pmaUser;
        this.changeDate = changeDate;
        this.previousClassification = previousClassification;
        this.newClassification = newClassification;
        this.returnedChecks = returnedChecks;
    }

    public int getReturnedChecks() {
        return returnedChecks;
    }

    public void setReturnedChecks(int returnedChecks) {
        this.returnedChecks = returnedChecks;
    }

    public Long getId() {
        return id;
    }

    public PmaUser getPmaUser() {
        return pmaUser;
    }

    public void setPmaUser(PmaUser pmaUser) {
        this.pmaUser = pmaUser;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public Classification getPreviousClassification() {
        return previousClassification;
    }

    public void setPreviousClassification(Classification previousClassification) {
        this.previousClassification = previousClassification;
    }

    public Classification getNewClassification() {
        return newClassification;
    }

    public void setNewClassification(Classification newClassification) {
        this.newClassification = newClassification;
    }
}

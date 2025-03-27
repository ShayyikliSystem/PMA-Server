package com.pma.dummyserver.model;

import java.time.LocalDateTime;

import com.pma.dummyserver.enums.Classification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "pma_users")
public class PmaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "ID number is required")
    @Pattern(regexp = "\\d{9}", message = "ID number must be exactly 9 digits")
    private String idNumber;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String familyName;

    @Column(nullable = false)
    private int returnedChecks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Classification classification;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Card number is required")
    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 digits")
    private String cardNumber;

    @Column(name = "shayyikli_account_number", unique = true, length = 6)
    private Integer shayyikliAccountNumber;

    @Column(name = "last_returned_check_date")
    private LocalDateTime lastReturnedCheckDate;

    protected PmaUser() {
    }

    public PmaUser(String idNumber, String firstName, String familyName, int returnedChecks, String cardNumber) {
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.familyName = familyName;
        this.returnedChecks = returnedChecks;
        this.classification = determineClassification(returnedChecks);
        this.cardNumber = cardNumber;
    }

    public Long getId() {
        return id;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public int getReturnedChecks() {
        return returnedChecks;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setReturnedChecks(int returnedChecks) {
        this.returnedChecks = returnedChecks;
        this.classification = determineClassification(returnedChecks);
    }

    public Classification determineClassification(int returnedChecks) {
        if (returnedChecks == 0) {
            return Classification.A;
        } else if (returnedChecks >= 1 && returnedChecks <= 5) {
            return Classification.B;
        } else if (returnedChecks >= 6 && returnedChecks <= 15) {
            return Classification.C;
        } else {
            return Classification.D;
        }
    }

    public Integer getShayyikliAccountNumber() {
        return shayyikliAccountNumber;
    }

    public void setShayyikliAccountNumber(Integer shayyikliAccountNumber) {
        this.shayyikliAccountNumber = shayyikliAccountNumber;
    }

    public LocalDateTime getLastReturnedCheckDate() {
        return lastReturnedCheckDate;
    }

    public void setLastReturnedCheckDate(LocalDateTime lastReturnedCheckDate) {
        this.lastReturnedCheckDate = lastReturnedCheckDate;
    }
}
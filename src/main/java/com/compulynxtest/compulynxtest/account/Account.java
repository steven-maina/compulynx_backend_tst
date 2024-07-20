package com.compulynxtest.compulynxtest.account;


import com.compulynxtest.compulynxtest.transaction.AccountTransactions;
import com.compulynxtest.compulynxtest.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountType;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String branchName;

    @Column(unique = true)
    private String swiftCode;

    @Builder.Default
    private double balance = 0.0;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountTransactions> histories;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(nullable = true, updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Long lastModifiedBy;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        generateSwiftCode();
    }
    private void generateSwiftCode() {
        String bankCode = "SMPB"; // Your bank code
        String countryCode = "KE"; // Country code
        String locationCode = generateRandomAlphaNumeric(2); // Gen Random 2 alphanumeric characters
        String branchCode = generateRandomAlphaNumeric(3); // Random 3 alphanumeric characters

        this.swiftCode = bankCode + countryCode + locationCode + branchCode;
    }

    private String generateRandomAlphaNumeric(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }

        return result.toString();
    }
}

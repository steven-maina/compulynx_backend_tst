package com.compulynxtest.compulynxtest.transaction;

import com.compulynxtest.compulynxtest.account.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class AccountTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    private String description;

    @Column(nullable = false)
    private Double balanceAfter;

    @Column(unique = true, nullable = false)
    private String reference;

    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED
    }
}

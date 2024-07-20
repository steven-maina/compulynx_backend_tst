package com.compulynxtest.compulynxtest.user;

import com.compulynxtest.compulynxtest.account.Account;
import com.compulynxtest.compulynxtest.role.Role;
import com.compulynxtest.compulynxtest.transaction.AccountTransactions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_customers")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "middle_name")
    private String middlename;
    @Column(name = "last_name")
    private String lastname;
    @Column(name = "dob")
    private LocalDate dateOfBirth;
    @Column(unique = true, nullable = false)
    private String idno;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String pin;
    private boolean enabled=true;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedDate = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }
    @Override
    public String getPassword() {
        return pin;
    }

    @Override
    public String getUsername() {
        return idno;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String fullName() {
        return getFirstname() + " " + getLastname();
    }

    @Override
    public String getName() {
        return email;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "firstname = " + firstname + ", " +
                "middlename = " + middlename + ", " +
                "lastname = " + lastname + ", ";
    }
}

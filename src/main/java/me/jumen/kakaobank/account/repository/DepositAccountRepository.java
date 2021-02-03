package me.jumen.kakaobank.account.repository;

import me.jumen.kakaobank.account.DepositAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepositAccountRepository extends JpaRepository<DepositAccount, Long> {
    Optional<DepositAccount> findByOwnerId(Long owner_id);
}

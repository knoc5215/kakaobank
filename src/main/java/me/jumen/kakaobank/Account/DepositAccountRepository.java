package me.jumen.kakaobank.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositAccountRepository extends JpaRepository<Account, Long> {
}

package me.jumen.kakaobank.account.repository;

import me.jumen.kakaobank.account.MeetingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingAccountRepository extends JpaRepository<MeetingAccount, Long> {
     Optional<MeetingAccount> findByOwnerId(Long owner_id);
}

package me.jumen.kakaobank.owner;

import me.jumen.kakaobank.util.OwnerUtil;
import me.jumen.kakaobank.account.repository.DepositAccountRepository;
import me.jumen.kakaobank.account.repository.MeetingAccountRepository;
import me.jumen.kakaobank.owner.repository.OwnerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OwnerRepositoryTest {

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Autowired
    MeetingAccountRepository meetingAccountRepository;

    @Test
    @DisplayName("고객 만들기")
    public void makeClient() {
        Owner owner = OwnerUtil.getOwner("원주영", 29, "01038855215");
        Owner save = ownerRepository.save(owner);

        Optional<Owner> byName = ownerRepository.findById(save.getId());

        assertThat(byName.isEmpty()).isFalse();
        assertThat(byName.get().getName()).isEqualTo("원주영");
    }


}
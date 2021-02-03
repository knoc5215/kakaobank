package me.jumen.kakaobank.owner;

import me.jumen.kakaobank.account.DepositAccount;
import me.jumen.kakaobank.account.MeetingAccount;
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
        Owner owner = getClient("원주영", 29, "01038855215");
        Owner save = ownerRepository.save(owner);

        Optional<Owner> byName = ownerRepository.findByName(save.getName());

        assertThat(byName.get()).isNotNull();
        assertThat(byName.get().getName()).isEqualTo("원주영");
    }







    public Owner getClient(String name, Integer age, String phoneNumber) {
        return Owner.builder()
                .name(name)
                .age(age)
                .phoneNumber(phoneNumber)
                .build();
    }

    public DepositAccount getDepositAccount(Owner owner) {
        return DepositAccount.builder()
                .owner(owner)
                .build();
    }

    public MeetingAccount getMeetingAccount(Owner owner) {
        return MeetingAccount.builder()
                .owner(owner)
                .build();
    }

    public MeetingAccount getMeetingAccountWithTitle(Owner owner, String title) {
        return MeetingAccount.builder()
                .owner(owner)
                .title("[모임계좌] " + title)
                .build();
    }

    public Participant getParticipant(Owner owner) {
        return Participant.builder()
                .owner(owner)
                .build();
    }


}
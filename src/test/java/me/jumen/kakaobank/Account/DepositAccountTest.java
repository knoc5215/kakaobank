package me.jumen.kakaobank.Account;

import me.jumen.kakaobank.Client.ClientRepository;
import me.jumen.kakaobank.Client.Owner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
class DepositAccountTest {

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Test
    public void test() {
        Owner owner = getClient("원주영", 29, "01038855215");
        Owner save = clientRepository.save(owner);

        Account depositAccount = DepositAccount.builder()
                .owner(save)
                .build();

        assertThat(depositAccount.getOwner().getName()).isEqualTo("원주영");

        Account account = depositAccountRepository.save(depositAccount);

        Optional<Account> byId = depositAccountRepository.findById(account.getId());
        assertThat(byId.get().getOwner().getName()).isEqualTo("원주영");

    }

    public Owner getClient(String name, Integer age, String phoneNumber) {
        return Owner.builder()
                .name(name)
                .age(age)
                .phoneNumber(phoneNumber)
                .build();
    }

}
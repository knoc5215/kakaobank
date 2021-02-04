package me.jumen.kakaobank.account;

import me.jumen.kakaobank.account.repository.DepositAccountRepository;
import me.jumen.kakaobank.account.repository.MeetingAccountRepository;
import me.jumen.kakaobank.owner.Owner;
import me.jumen.kakaobank.owner.repository.OwnerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest()
class DepositAccountTest {

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Autowired
    MeetingAccountRepository meetingAccountRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Test
    @DisplayName("입출금계좌 개설하기")
    public void getDepositAccountTest() {
        Owner owner = OwnerUtil.getOwner("원주영", 29, "01038855215");
        Owner save = ownerRepository.save(owner);

        DepositAccount depositAccount = depositAccountRepository.save(AccountUtil.getDepositAccount(save));
        Optional<DepositAccount> byId = depositAccountRepository.findById(depositAccount.getAccountNumber());
        assertThat(byId.isEmpty()).isFalse();

        DepositAccount depositAccount1 = byId.get();
        System.out.println("accountNumber : " + depositAccount1.getAccountNumber());
        assertThat(depositAccount1.getOwner().getName()).isEqualTo("원주영");
    }


    @Test
    @DisplayName("입출금계좌에 입금하기")
    public void depositToDepositAccountTest() {
        Owner owner = OwnerUtil.getOwner("원주영", 29, "01038855215");
        Owner save = ownerRepository.save(owner);

        DepositAccount depositAccount = AccountUtil.getDepositAccount(save, "입출금계좌");

        DepositAccount saveDepositAccount = depositAccountRepository.save(depositAccount);

        Optional<DepositAccount> byId = depositAccountRepository.findById(saveDepositAccount.getAccountNumber());
        assertThat(byId.isEmpty()).isFalse();

        DepositAccount account = byId.get();
        account.deposit(owner, account.getAccountNumber(), 1000L);
        System.out.println("balance : " + account.getBalance());
        account.withDraw(owner, account.getAccountNumber(), 100L);
        System.out.println("balance :" + account.getBalance());

        account.withDraw(OwnerUtil.getOwner("도둑놈", 100, "01011112222"), account.getAccountNumber(), 100L);
        System.out.println("balance :" + account.getBalance());

        assertThat(account.getTransactions().size()).isEqualTo(2);

    }


}
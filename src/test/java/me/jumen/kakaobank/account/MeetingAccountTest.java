package me.jumen.kakaobank.account;

import me.jumen.kakaobank.account.repository.DepositAccountRepository;
import me.jumen.kakaobank.account.repository.MeetingAccountRepository;
import me.jumen.kakaobank.owner.Owner;
import me.jumen.kakaobank.owner.Participant;
import me.jumen.kakaobank.owner.repository.OwnerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest()
class MeetingAccountTest {

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Autowired
    MeetingAccountRepository meetingAccountRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Test
    @DisplayName("각각의 입출금계좌에 대해서 모임통장서비스 신청하기")
    public void convertToMeetingAccount() {
        Owner owner = OwnerUtil.getOwner("원주영", 29, "01038855215");
        Owner save = ownerRepository.save(owner);

        for (int i = 0; i < 10; i++) {
            DepositAccount depositAccount = AccountUtil.getDepositAccount(save, "입출금계좌 " + (i + 1));
            DepositAccount saveDepositAccount = depositAccountRepository.save(depositAccount);

            MeetingAccount meetingAccount = AccountUtil.getMeetingAccountFromDepositAccount(saveDepositAccount, owner, "모임통장 " + (i + 1));
            MeetingAccount saveMeetingAccount = meetingAccountRepository.save(meetingAccount);
        }

        System.out.println("입출금계좌는 SUSPEND 상태로");
        Set<DepositAccount> depositAccounts = save.getDepositAccounts();
        Iterator<DepositAccount> depositAccountIterator = depositAccounts.iterator();
        while (depositAccountIterator.hasNext()) {
            DepositAccount next = depositAccountIterator.next();
            System.out.println("[" + next.getType() + "]" + next.getTitle() + ", 계좌번호 " + next.getAccountNumber() + ", 계좌상태 : " + next.getStatus());
        }


        System.out.println("모임계좌는 ACTIVE 상태로");
        Set<MeetingAccount> meetingAccounts = save.getMeetingAccounts();
        Iterator<MeetingAccount> meetingAccountIterator = meetingAccounts.iterator();
        while (meetingAccountIterator.hasNext()) {
            MeetingAccount next = meetingAccountIterator.next();
            System.out.println("[" + next.getType() + "]" + next.getTitle() + ", 계좌번호 " + next.getAccountNumber() + ", 계좌상태 : " + next.getStatus() + ", 입출금계좌번호 : " + next.getDepositAccountId());
        }

    }

    @Test
    @DisplayName("모임통장에 모임멤버 추가, 제거하기")
    public void addAndRemoveParticipantsTest() {
        Owner owner = OwnerUtil.getOwner("원주영", 29, "01038855215");
        Owner save = ownerRepository.save(owner);

        DepositAccount depositAccount = AccountUtil.getDepositAccount(save, "입출금계좌");
        DepositAccount saveDepositAccount = depositAccountRepository.save(depositAccount);

        MeetingAccount meetingAccount = AccountUtil.getMeetingAccountFromDepositAccount(saveDepositAccount, owner, "모임통장");
        MeetingAccount saveMeetingAccount = meetingAccountRepository.save(meetingAccount);

        assertThat(saveMeetingAccount.getParticipants().size()).isEqualTo(0);

        System.out.println("모임통장 멤버 10명 추가");
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            Owner inviteOwner = OwnerUtil.getOwner("초대 " + (i), random.nextInt(50), "01012341234");
            saveMeetingAccount.addParticipant(inviteOwner);
        }
        assertThat(saveMeetingAccount.getParticipants().size()).isEqualTo(10);


        System.out.println("모임통장 멤버들에게 입금알림 출력");
        saveMeetingAccount.deposit(save, saveMeetingAccount.getAccountNumber(), 1000L);


        System.out.println("모임통장 멤버 1명 삭제");
        List<Participant> participants = saveMeetingAccount.getParticipants();
        saveMeetingAccount.removeParticipant(participants.get(0));
        assertThat(saveMeetingAccount.getParticipants().size()).isEqualTo(9);
    }

    @Test
    @DisplayName("모임주 개설한도 100개 테스트")
    public void addMeetingAccountsOver100() {
        Owner owner = OwnerUtil.getOwner("원주영", 29, "01038855215");
        Owner save = ownerRepository.save(owner);

        for (int i = 1; i <= 110; i++) {

            DepositAccount depositAccount = AccountUtil.getDepositAccount(save, "입출금계좌 " + (i));
            DepositAccount saveDepositAccount = depositAccountRepository.save(depositAccount);

            MeetingAccount meetingAccount = AccountUtil.getMeetingAccountFromDepositAccount(saveDepositAccount, owner, "모임통장 " + (i + 1));
            MeetingAccount saveMeetingAccount = meetingAccountRepository.save(meetingAccount);

        }

        System.out.println("입출금계좌는 SUSPEND 상태로");
        Set<DepositAccount> depositAccounts = save.getDepositAccounts();
        Iterator<DepositAccount> depositAccountIterator = depositAccounts.iterator();
        while (depositAccountIterator.hasNext()) {
            DepositAccount next = depositAccountIterator.next();
            System.out.println("[" + next.getType() + "]" + next.getTitle() + ", 계좌번호 " + next.getAccountNumber() + ", 계좌상태 : " + next.getStatus());
        }

        assertThat(save.getDepositAccounts().size()).isEqualTo(110);


        System.out.println("모임계좌는 ACTIVE 상태로");
        Set<MeetingAccount> meetingAccounts = save.getMeetingAccounts();
        Iterator<MeetingAccount> meetingAccountIterator = meetingAccounts.iterator();
        while (meetingAccountIterator.hasNext()) {
            MeetingAccount next = meetingAccountIterator.next();
            System.out.println("[" + next.getType() + "]" + next.getTitle() + ", 계좌번호 " + next.getAccountNumber() + ", 계좌상태 : " + next.getStatus() + ", 입출금계좌번호 : " + next.getDepositAccountId());
        }

        assertThat(save.getMeetingAccounts().size()).isEqualTo(100);    //모임계좌 100개까지만 보유 가능

    }

    @Test
    @DisplayName("모임멤버 참여한도 30개 테스트")
    public void heldMeetingsOver30() {
        Owner owner = OwnerUtil.getOwner("원주영", 29, "01038855215");
        Owner save = ownerRepository.save(owner);

        int depositAccountsSize = 30;
        int meetingAccountsSize = depositAccountsSize;

        for (int i = 1; i <= depositAccountsSize; i++) {

            DepositAccount depositAccount = AccountUtil.getDepositAccount(save, "입출금계좌 " + (i));
            DepositAccount saveDepositAccount = depositAccountRepository.save(depositAccount);

            MeetingAccount meetingAccount = AccountUtil.getMeetingAccountFromDepositAccount(saveDepositAccount, owner, "모임통장 " + (i));
            MeetingAccount saveMeetingAccount = meetingAccountRepository.save(meetingAccount);

        }
        assertThat(save.getDepositAccounts().size()).isEqualTo(depositAccountsSize);


        System.out.println("모임계좌는 ACTIVE 상태로");
        Set<MeetingAccount> meetingAccounts = save.getMeetingAccounts();
        Iterator<MeetingAccount> meetingAccountIterator = meetingAccounts.iterator();
        while (meetingAccountIterator.hasNext()) {
            MeetingAccount next = meetingAccountIterator.next();
            System.out.println("[" + next.getType() + "]" + next.getTitle() + ", 계좌번호 " + next.getAccountNumber() + ", 계좌상태 : " + next.getStatus() + ", 입출금계좌번호 : " + next.getDepositAccountId());

            next.addParticipant(save);
        }

        assertThat(save.getNumOfMeetingsHeld().size()).isEqualTo(meetingAccountsSize);  //모임에 30개까지만 참여 가능
    }

    @Test
    @DisplayName("입출금통장 1개당 모임통장서비스 1개만 신청 가능")
    public void convertToDepositAccountDuplicate() {
        Owner owner = OwnerUtil.getOwner("원주영", 29, "01038855215");
        Owner save = ownerRepository.save(owner);

        DepositAccount depositAccount = AccountUtil.getDepositAccount(save, "입출금계좌");
        DepositAccount saveDepositAccount = depositAccountRepository.save(depositAccount);

        int convertSuccessCnt = 0;
        for (int i = 0; i < 2; i++) {
            if (!depositAccount.isConverted()) {
                MeetingAccount meetingAccount = AccountUtil.getMeetingAccountFromDepositAccount(saveDepositAccount, owner, "모임통장");
                MeetingAccount saveMeetingAccount = meetingAccountRepository.save(meetingAccount);
                convertSuccessCnt++;
            } else {
                System.out.println(depositAccount.getType() + ", 계좌번호 : " + depositAccount.getAccountNumber() + " 계좌는 이미 모임통장으로 전환되었습니다.");

            }
        }

        assertThat(convertSuccessCnt).isEqualTo(1);
    }

    @Test
    @DisplayName("모임통장 거래내역 조회")
    public void selectTransactions() {
        Owner owner = OwnerUtil.getOwner("원주영", 29, "01038855215");
        Owner save = ownerRepository.save(owner);

        DepositAccount depositAccount = AccountUtil.getDepositAccount(save, "입출금계좌");
        DepositAccount saveDepositAccount = depositAccountRepository.save(depositAccount);

        MeetingAccount meetingAccount = AccountUtil.getMeetingAccountFromDepositAccount(saveDepositAccount, owner, "모임통장");
        MeetingAccount saveMeetingAccount = meetingAccountRepository.save(meetingAccount);

        assertThat(saveMeetingAccount.getParticipants().size()).isEqualTo(0);

        System.out.println("모임통장 멤버 10명 추가");
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            Owner inviteOwner = OwnerUtil.getOwner("초대 " + (i), random.nextInt(50), "01012341234");
            saveMeetingAccount.addParticipant(inviteOwner);
        }
        assertThat(saveMeetingAccount.getParticipants().size()).isEqualTo(10);

        assertThat(save.getMeetingAccounts().size()).isEqualTo(1);  // 현재 모임주는 1개의 모임통장을 갖고 있다.

        Iterator<MeetingAccount> meetingAccountIterator = save.getMeetingAccounts().iterator();
        while (meetingAccountIterator.hasNext()) {
            MeetingAccount next = meetingAccountIterator.next();

            System.out.println("모임통장 멤버들에게 입금알림 출력");
            for (int i = 0; i < 10; i++) {
                saveMeetingAccount.deposit(save, saveMeetingAccount.getAccountNumber(), 1000L); //10번 입금
            }

        }

        assertThat(saveMeetingAccount.getTransactions().size()).isEqualTo(10);  //해당 모임통장의 거래내역이 10개 존재한다.

    }


}
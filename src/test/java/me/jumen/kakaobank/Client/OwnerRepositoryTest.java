package me.jumen.kakaobank.Client;

import me.jumen.kakaobank.Account.DepositAccount;
import me.jumen.kakaobank.Account.DepositAccountRepository;
import me.jumen.kakaobank.Account.MeetingAccount;
import me.jumen.kakaobank.Account.MeetingAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OwnerRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Autowired
    MeetingAccountRepository meetingAccountRepository;

    @Test
    @DisplayName("고객 만들기")
    public void makeClient() {
        Owner owner = getClient("원주영", 29, "01038855215");
        Owner save = clientRepository.save(owner);

        Optional<Owner> byName = clientRepository.findByName(save.getName());

        assertThat(byName.get()).isNotNull();
        assertThat(byName.get().getName()).isEqualTo("원주영");
    }

    @Test
    @DisplayName("고객을 생성하고, 입출금계좌를 생성해서 연결해준다.")
    public void setDepositAccount() {
        Owner owner = getClient("원주영", 29, "01038855215");
        DepositAccount depositAccount = getDepositAccount(owner);
        DepositAccount saveDepositAccount = depositAccountRepository.save(depositAccount);

        owner.addDepositAccount(saveDepositAccount);

        Owner save = clientRepository.save(owner);

        Optional<Owner> byName = clientRepository.findByName(save.getName());

        assertThat(byName.isPresent()).isTrue();

        Owner client = byName.get();
        assertThat(client.getName()).isEqualTo("원주영");
        assertThat(client.getDepositAccounts().get(0).getOwner().getId()).isEqualTo(client.getId());

    }

    @Test
    @DisplayName("고객을 생성하고, 입출금계좌를 생성해서 연결한 후, 해당 입출금계좌를 제거한다.")
    public void removeDepositAccount() {
        //When
        Owner owner = getClient("원주영", 29, "01038855215");
        DepositAccount depositAccount = getDepositAccount(owner);
        owner.addDepositAccount(depositAccount);

        Owner save = clientRepository.save(owner);

        Optional<Owner> byName = clientRepository.findByName(save.getName());

        assertThat(byName.isPresent()).isTrue();

        Owner client = byName.get();
        assertThat(client.getName()).isEqualTo("원주영");
        assertThat(client.getDepositAccounts().size()).isEqualTo(1);

        //Then
        client.removeDepositAccount(depositAccount);

        save = clientRepository.save(client);
        assertThat(save.getDepositAccounts().size()).isEqualTo(0);
    }

    @Test
    public void makeParticipant() {
        Owner owner = getClient("원주영", 29, "01038855215");
        Participant participant = getParticipant(owner);

        assertThat(participant.getParticipant().getName()).isEqualTo(owner.getName());

    }

    @Test
    @DisplayName("고객을 생성하고, 모임계좌를 생성한다.")
    public void setMeetingAccount() {
        Owner owner = getClient("원주영", 29, "01038855215");
        MeetingAccount meetingAccount = getMeetingAccount(owner);
        MeetingAccount saveMeetingAccount = meetingAccountRepository.save(meetingAccount);

        owner.addMeetingAccount(saveMeetingAccount);

        Owner save = clientRepository.save(owner);

        Optional<Owner> byName = clientRepository.findByName(save.getName());

        assertThat(byName.isPresent()).isTrue();

        Owner client = byName.get();
        assertThat(client.getMeetingAccounts().get(0).getOwner().getId()).isEqualTo(client.getId());

    }

    @Test
    @DisplayName("고객을 생성하고, 모임계좌를 여러개 생성한다.")
    public void setMeetingAccountMany() {
        Owner owner = getClient("원주영", 29, "01038855215");

        for (int i = 0; i < 105; i++) {
            MeetingAccount meetingAccount = getMeetingAccountWithTitle(owner, (i + 1) + "");
            MeetingAccount saveMeetingAccount = meetingAccountRepository.save(meetingAccount);

            owner.addMeetingAccount(saveMeetingAccount);
        }
        Owner save = clientRepository.save(owner);

        Optional<Owner> byName = clientRepository.findByName(save.getName());

        assertThat(byName.isPresent()).isTrue();

        Owner client = byName.get();
        assertThat(client.getMeetingAccounts().size()).isEqualTo(100);
        assertThat(client.getMeetingAccounts().get(0).getOwner().getId()).isEqualTo(client.getId());

    }

    @Test
    public void findMeetingAccountByOwnerId() {
        Owner owner = getClient("원주영", 29, "01038855215");
        MeetingAccount meetingAccount = getMeetingAccount(owner);
        MeetingAccount saveMeetingAccount = meetingAccountRepository.save(meetingAccount);

        owner.addMeetingAccount(saveMeetingAccount);

        Owner save = clientRepository.save(owner);


        Optional<MeetingAccount> byOwnerId = meetingAccountRepository.findByOwnerId(save.getId());
        assertThat(byOwnerId.isPresent());
        assertThat(byOwnerId.get().getOwner().getId()).isEqualTo(save.getId());
        assertThat(byOwnerId.get().getParticipants().size()).isEqualTo(1);
        assertThat(byOwnerId.get().getParticipants().get(0).getParticipant().getId()).isEqualTo(save.getId());

    }

    @Test
    public void addParticipantIntMeetingAccount() {
        Owner owner = getClient("원주영", 29, "01038855215");
        MeetingAccount meetingAccount = getMeetingAccount(owner);
        Owner anotherOwner = getClient("김단우", 30, "01033334444");
        Participant participant = meetingAccount.addParticipant(Participant.builder().owner(anotherOwner).build());


        MeetingAccount saveMeetingAccount = meetingAccountRepository.save(meetingAccount);
        owner.addMeetingAccount(saveMeetingAccount);

        Owner save = clientRepository.save(owner);

        Optional<MeetingAccount> byOwnerId = meetingAccountRepository.findByOwnerId(save.getId());
        assertThat(byOwnerId.isPresent());
        assertThat(byOwnerId.get().getOwner().getId()).isEqualTo(save.getId());
        assertThat(byOwnerId.get().getParticipants().size()).isEqualTo(2);
    }

    @Test
    public void convertMeetingAccountFromDepositAccount() {
        Owner owner = getClient("원주영", 29, "01038855215");
        DepositAccount depositAccount = getDepositAccount(owner);
        DepositAccount saveDepositAccount = depositAccountRepository.save(depositAccount);

        owner.addDepositAccount(saveDepositAccount);

        Owner save = clientRepository.save(owner);
        clientRepository.flush();
        MeetingAccount meetingAccount = new MeetingAccount(depositAccount, "전환!");


        save = clientRepository.save(owner);

        Optional<Owner> byId = clientRepository.findById(save.getId());
        assertThat(byId.get().getDepositAccounts().size()).isEqualTo(0);
        assertThat(byId.get().getMeetingAccounts().size()).isEqualTo(1);

    }

    @Test
    public void depositTest() {
        Owner owner = getClient("원주영", 29, "01038855215");
        MeetingAccount meetingAccount = getMeetingAccount(owner);
        MeetingAccount save = meetingAccountRepository.save(meetingAccount);

        meetingAccount.deposit(1000L);
        meetingAccount.deposit(2000L);

        assertThat(meetingAccount.getBalance()).isEqualTo(3000L);

        meetingAccount.withDraw(1000L);
        assertThat(meetingAccount.getBalance()).isEqualTo(2000L);





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
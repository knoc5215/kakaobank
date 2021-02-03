package me.jumen.kakaobank.owner;

import lombok.*;
import me.jumen.kakaobank.account.AccountStatus;
import me.jumen.kakaobank.account.DepositAccount;
import me.jumen.kakaobank.account.MeetingAccount;
import me.jumen.kakaobank.account.observe.Observer;
import me.jumen.kakaobank.account.transaction.Transaction;
import me.jumen.kakaobank.account.transaction.TransactionType;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Owner implements Observer {
    private final static int MEETINGACCOUNT_RETENTION_LIMIT = 100;      //모임계좌는 100개까지 개설 가능
    private final static int MEETINGACCOUNT_PARTICIPATION_LIMIT = 30;   //모임계좌에 30개까지 참여 가능


    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer age;
    private String phoneNumber;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<DepositAccount> depositAccounts;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<MeetingAccount> meetingAccounts;

    @OneToMany(mappedBy = "participant", fetch = FetchType.LAZY)
    private Set<Participant> numOfMeetingsHeld;

    private Date created;
    private Date lastLogin;

    @Builder
    public Owner(String name, Integer age, String phoneNumber) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;

        this.depositAccounts = new HashSet<>();
        this.meetingAccounts = new HashSet<>();
        this.numOfMeetingsHeld = new HashSet<>();

        this.created = new Date();
        this.lastLogin = null;

    }


    public DepositAccount addDepositAccount(DepositAccount account) {
        this.getDepositAccounts().add(account);
        account.setOwner(this);
        System.out.println("현재 계좌 수 : " + this.getDepositAccounts().size());
        return account;
    }

    public void removeDepositAccount(DepositAccount account) {
        this.getDepositAccounts().remove(account);
        account.setStatus(AccountStatus.TERMINATE);
        account.setOwner(null);
    }

    public MeetingAccount addMeetingAccount(MeetingAccount account) {
        if (this.getMeetingAccounts().size() < MEETINGACCOUNT_RETENTION_LIMIT) {
            this.getMeetingAccounts().add(account);
            account.setOwner(this);
            return account;
        } else {
            System.out.println("명의자는 최대 100개의 모임계좌를 소유할 수 있습니다.");
            return null;
        }
    }

    public void removeMeetingAccount(MeetingAccount account) {
        if (this.getMeetingAccounts().size() > 1) {
            this.getMeetingAccounts().remove(account);
            account.setStatus(AccountStatus.TERMINATE);
            account.setOwner(null);
        }
    }

    public void joinToMeeting(Participant participant) {
        if (!this.getNumOfMeetingsHeld().contains(participant)) {
            this.getNumOfMeetingsHeld().add(participant);
        }
    }

    public void exitFromMeeting(Participant participant) {
        if (this.getNumOfMeetingsHeld().contains(participant)) {
            this.getNumOfMeetingsHeld().remove(participant);
        }
    }

    @Override
    public void update(Transaction transaction) {
        System.out.println("[알림대상] " + this.name + ", " + this.age);

        if (transaction.getTransactionType() == TransactionType.DEPOSIT) {
            System.out.println(this.name + "님이 " + "계좌번호 " + transaction.getAccountNumber() + " 로 " + transaction.getAmount() + "원을 입금하셨습니다. 현재 잔액은 " + transaction.getBalanceAfterTransaction() + "원 입니다.");
        } else {
            System.out.println(this.name + "님이 " + "계좌번호 " + transaction.getAccountNumber() + " 에서 " + transaction.getAmount() + "원을 출금하셨습니다. 현재 잔액은 " + transaction.getBalanceAfterTransaction() + "원 입니다.");
        }
        System.out.println(transaction.toString());
    }
}

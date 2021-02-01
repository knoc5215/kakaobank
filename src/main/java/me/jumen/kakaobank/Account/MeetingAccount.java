package me.jumen.kakaobank.Account;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jumen.kakaobank.Client.Owner;
import me.jumen.kakaobank.Client.Participant;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 입출금계좌
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class MeetingAccount {

    @Id
    @GeneratedValue
    private Long id;

    private Long depositAccountId;

    private String title;

    @ManyToOne
    private Owner owner;   //모임통장 명의자

    @OneToMany(mappedBy = "meetingAccount")
    private List<Participant> participants;

    private AccountType type;   //계좌종류
    private AccountStatus status; //계좌상태

    private Long accountNumber; //통장번호
    private Date created;   //생성시간
    private Date lastLogin; //마지막 로그인 시간

    private Date lastDepositWithdrawalTime; //마지막 입출금 거래 시간

    private Long balance;   //잔고


    @Builder
    public MeetingAccount(Owner owner, String title) {
        this.owner = owner;
        this.title = title;
        this.created = new Date();
        this.status = AccountStatus.ACTIVE;

        this.participants = new ArrayList<>();
        this.participants.add(Participant.builder().owner(this.owner).build());

        this.balance = 0L;
        this.type = AccountType.MEETING;
    }

    @Builder
    public MeetingAccount(DepositAccount depositAccount, String title) {
        this(depositAccount.getOwner(), title);

        Owner depositOwner = depositAccount.getOwner();
        this.depositAccountId = depositOwner.getId();
        depositAccount.getOwner().removeDepositAccount(depositAccount);

        depositOwner.addMeetingAccount(this);

    }


    public Participant addParticipant(Participant participant) {
        if (this.participants.size() < 100) {
            this.participants.add(participant);
            return participant;
        } else {
            System.out.println("모임계좌는 100명이 한도입니다.");
            return null;
        }
    }

    public MeetingAccount deposit(Long deposit) {
        synchronized (this) {
            balance += deposit;
        }
        return this;
    }

    public MeetingAccount withDraw(Long withDraw) {
        if (balance - withDraw < 0) {
            System.out.println("잔고가 부족합니다.");
            return null;
        } else {
            synchronized (this) {
                balance -= withDraw;
            }
            return this;
        }
    }

    public void invite(Participant participant) {
        if (!isAlreadyIn(participant)) {
            this.participants.add(participant);
        }
    }

    public boolean isAlreadyIn(Participant participant) {
        return this.participants.contains(participant);
    }


}

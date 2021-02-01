package me.jumen.kakaobank.Client;

import lombok.*;
import me.jumen.kakaobank.Account.Account;
import me.jumen.kakaobank.Account.DepositAccount;
import me.jumen.kakaobank.Account.MeetingAccount;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Owner {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer age;
    private String phoneNumber;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Account> depositAccounts;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<MeetingAccount> meetingAccounts;

    private Date created;
    private Date lastLogin;

    @Builder
    public Owner(String name, Integer age, String phoneNumber) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;

        this.depositAccounts = new ArrayList<>();
        this.meetingAccounts = new ArrayList<>();
        this.created = new Date();

    }


    public DepositAccount addDepositAccount(DepositAccount account) {
        this.getDepositAccounts().add(account);
        account.setOwner(this);
        return account;
    }

    public void removeDepositAccount(DepositAccount account) {
        this.getDepositAccounts().remove(account);
        account.setOwner(null);
    }

    public MeetingAccount addMeetingAccount(MeetingAccount account) {
        if (this.getMeetingAccounts().size() < 100) {
            this.getMeetingAccounts().add(account);
            account.setOwner(this);
            return account;
        } else {
            System.out.println("명의자는 최대 100개의 모임계좌를 소유할 수 있습니다.");
            return null;
        }
    }

    public void removeMeetingAccount(DepositAccount account) {
        if (this.getMeetingAccounts().size() > 1) {
            this.getMeetingAccounts().remove(account);
            account.setOwner(null);
        }
    }

}

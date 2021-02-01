package me.jumen.kakaobank.Account;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jumen.kakaobank.Client.Owner;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public abstract class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @ManyToOne
    private Owner owner;   //모임통장 명의자

    private AccountType type;   //계좌종류
    private AccountStatus status; //계좌상태

    private Long accountNumber; //통장번호
    private Date created;   //생성시간
    private Date lastLogin; //마지막 로그인 시간

    private Date lastDepositWithdrawalTime; //마지막 입출금 거래 시간

    private Long balance;   //잔고

    public Account(Owner owner) {
        this.owner = owner;
        this.status = AccountStatus.ACTIVE;
        this.created = new Date();
        this.lastLogin = null;
        this.lastDepositWithdrawalTime = null;
        this.balance = 0L;
    }

}

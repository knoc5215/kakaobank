package me.jumen.kakaobank.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.jumen.kakaobank.account.observe.Observable;
import me.jumen.kakaobank.account.observe.Observer;
import me.jumen.kakaobank.account.transaction.Transaction;
import me.jumen.kakaobank.account.transaction.TransactionType;
import me.jumen.kakaobank.owner.Owner;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 계좌 수퍼클래스
 */
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(exclude = {"transactions", "observers"})
public abstract class Account implements Observable {

    private String title;   //계좌명
    private AccountType type;   //계좌종류
    private AccountStatus status; //계좌상태
    private List<Transaction> transactions; //거래내역
    private List<Observer> observers;   //알림대상
    private Date created;   //생성시간
    private Date lastLogin; //마지막 로그인 시간
    private Date lastDepositWithdrawalTime; //마지막 입출금 거래 시간
    private Long balance;   //잔고

    public Account(AccountType type, String title) {
        this.type = type;
        this.title = title;
        this.status = AccountStatus.ACTIVE;

        this.transactions = new ArrayList<>();
        this.observers = new ArrayList<>();

        this.created = new Date();
        this.lastLogin = null;
        this.lastDepositWithdrawalTime = null;

        this.balance = 0L;
    }

    public void deposit(Owner owner, Long accountNumber, Long deposit) {
        synchronized (this) {
            Long before = this.balance;
            this.balance += deposit;
            Long after = this.balance;

            recordTransAction(TransactionType.DEPOSIT, accountNumber, owner.getId(), before, deposit, after);


        }
    }

    public void withDraw(Owner owner, Long accountNumber, Long withDraw) {
        if (this.balance - withDraw < 0) {
            System.out.println("잔고가 부족합니다.");
        } else {
            synchronized (this) {
                Long before = this.balance;
                this.balance -= withDraw;
                Long after = this.balance;

                recordTransAction(TransactionType.WITHDRAW, accountNumber, owner.getId(), before, withDraw, after);
            }
        }
    }

    private void recordTransAction(TransactionType transactionType, Long accountNumber, Long owner_id, Long before, Long amount, Long after) {
        Transaction transaction = Transaction.builder()
                .accountNumber(accountNumber)
                .owner_id(owner_id)
                .transactionType(transactionType)
                .balanceBeforeTransaction(before)
                .amount(amount)
                .balanceAfterTransaction(after)
                .transActionDate(new Date())
                .build();

        this.transactions.add(transaction);
        notifyOwner(transaction);
    }

    @Override
    public void subscribe(Owner owner) {
        this.observers.add(owner);

    }

    @Override
    public void unSubscribe(Owner owner) {
        this.observers.remove(owner);
    }

    @Override
    public void notifyOwner(Transaction transaction) {
        for (Observer observer : observers) {
            observer.update(transaction);
        }
    }
}

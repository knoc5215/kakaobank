package me.jumen.kakaobank.account;

import lombok.*;
import me.jumen.kakaobank.account.transaction.Transaction;
import me.jumen.kakaobank.account.transaction.TransactionType;
import me.jumen.kakaobank.owner.Owner;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 입출금계좌
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"owner"}, callSuper = true)
public class DepositAccount extends Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNumber; //통장번호

    @OneToMany(mappedBy = "depositAccount")
    private Set<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "owner_no")
    private Owner owner;    //명의자

    private Long meetingAccountId;  //전환된 모임통장ID
    private boolean isConverted;    //전환여부
    private Date convertedDate;  //전환시각

    @Builder
    public DepositAccount(Owner owner, String title) {
        super(AccountType.DEPOSIT, title);
        super.getObservers().add(owner);
        this.owner = owner;
        this.meetingAccountId = null;
        this.isConverted = false;
        this.convertedDate = null;
        this.transactions = new HashSet<>();
        owner.addDepositAccount(this);
    }

    @Override
    public void withDraw(Owner owner, Long accountNumber, Long withDraw) {
        if (this.owner.equals(owner)) {
            System.out.println("명의자가 출금한다.");
            super.withDraw(owner, accountNumber, withDraw);
        } else {
            System.out.println("명의자가 아니면 출금할 수 없습니다.");
        }
    }

    @Override
    public void deposit(Owner owner, Long accountNumber, Long deposit) {
        super.deposit(owner, accountNumber, deposit);
    }

    @Override
    public void recordTransAction(TransactionType transactionType, Long accountNumber, Long owner_id, Long before, Long amount, Long after) {
        Transaction transaction = Transaction.builder()
                .accountNumber(accountNumber)
                .owner_id(owner_id)
                .transactionType(transactionType)
                .balanceBeforeTransaction(before)
                .amount(amount)
                .balanceAfterTransaction(after)
                .transActionDate(super.getLastDepositWithdrawalTime())
                .build();

        this.transactions.add(transaction);
        super.notifyOwner(transaction);
    }








}

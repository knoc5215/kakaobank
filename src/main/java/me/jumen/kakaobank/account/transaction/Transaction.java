package me.jumen.kakaobank.account.transaction;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;    //일련번호

    private Long accountNumber; //계좌번호

    private TransactionType transactionType;
    private Long depositorId;   //입금고객ID
    private Long withdrawalId;  //출금고객ID

    private Long balanceBeforeTransaction;  //거래전잔액
    private Long amount;    //거래금액
    private Long balanceAfterTransaction;   //거래후잔액

    private Date transActionDate;   //거래시각


    @Builder
    public Transaction(Long accountNumber, Long owner_id, TransactionType transactionType, Long balanceBeforeTransaction, Long amount, Long balanceAfterTransaction, Date transActionDate) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        switch (transactionType) {
            case DEPOSIT:
                this.depositorId = owner_id;
                break;
            case WITHDRAW:
                this.withdrawalId = owner_id;
                break;
        }

        this.balanceBeforeTransaction = balanceBeforeTransaction;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.transActionDate = transActionDate;
    }
}

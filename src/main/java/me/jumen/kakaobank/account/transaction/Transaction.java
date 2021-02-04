package me.jumen.kakaobank.account.transaction;

import lombok.*;
import me.jumen.kakaobank.account.DepositAccount;
import me.jumen.kakaobank.account.MeetingAccount;
import me.jumen.kakaobank.owner.Owner;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //일련번호

    @ManyToOne
    @JoinColumn(name = "depositTrasaction_no")
    private DepositAccount depositAccount;

    @ManyToOne
    @JoinColumn(name = "meetingTrasaction_no")
    private MeetingAccount meetingAccount;



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

    public void printTransactionAlert(Owner owner) {
        StringBuffer stringBuffer = new StringBuffer();
        switch (this.transactionType) {
            case DEPOSIT:
                stringBuffer.append("[").append(owner.getName()).append("]").append("님이 ").append("계좌번호[").append(this.getAccountNumber()).append("]로 ").append(this.getAmount()).append("원을 입금하셨습니다.").append("\n");
                break;
            case WITHDRAW:
                stringBuffer.append("[").append(owner.getName()).append("]").append("님이 ").append("계좌번호[").append(this.getAccountNumber()).append("]에서 ").append(this.getAmount()).append("원을 출금하셨습니다.").append("\n");
                break;
        }
        stringBuffer.append("현재 잔액은 ").append(this.getBalanceAfterTransaction()).append("원 입니다.");
        System.out.println(stringBuffer);
    }

}

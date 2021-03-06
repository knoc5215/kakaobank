package me.jumen.kakaobank.account;

import lombok.*;
import me.jumen.kakaobank.account.transaction.Transaction;
import me.jumen.kakaobank.account.transaction.TransactionType;
import me.jumen.kakaobank.owner.Owner;
import me.jumen.kakaobank.owner.Participant;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 모임통장
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"owner", "participants"})
public class MeetingAccount extends Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNumber; //통장번호

    @OneToMany(mappedBy = "meetingAccount")
    private Set<Transaction> transactions;

    private Long depositAccountId;  //전환된 입출금계좌ID

    @ManyToOne
    @JoinColumn(name = "owner_no")
    private Owner owner;    //모임주(명의자)

    @OneToMany(mappedBy = "meetingAccount")
    private Set<Participant> participants; //모임멤버


    @Builder
    public MeetingAccount(DepositAccount depositAccount, Owner owner, String title) {
        super(AccountType.MEETING, title);
        this.depositAccountId = depositAccount.getAccountNumber();
        depositAccount.setStatus(AccountStatus.SUSPEND);
        depositAccount.setConverted(true);
        depositAccount.setConvertedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        this.owner = owner;
        owner.addMeetingAccount(this);
        this.participants = new HashSet<>();
        this.transactions = new HashSet<>();

    }

    public void addParticipant(Owner owner) {
        if (this.participants.size() >= 100) {
            System.out.println("더 이상 모임멤버를 추가할 수 없습니다.");
            return;
        }

        if (owner.getNumOfMeetingsHeld().size() >= 30) {
            System.out.println("해당 고객은 더 이상 모임에 참여할 수 없습니다.");
            return;
        }

        if (!this.participants.contains(owner)) {
            Participant participant = Participant.builder().owner(owner).build();
            boolean add = this.participants.add(participant);   //모임계좌에 멤버 추가
            if (add) {
                super.subscribe(owner);
                owner.joinToMeeting(participant);
                System.out.println(participant.getParticipant().getName() + " 모임멤버가 추가되었습니다.");
            }
        } else {
            System.out.println("이미 참여중인 모임멤버입니다.");
        }
    }

    public void removeParticipant(Participant participant) {
        if (this.participants.size() == 0) {
            System.out.println("제거할 모임멤버가 존재하지 않습니다.");
            return;
        }

        if (this.participants.contains(participant)) {
            boolean remove = this.participants.remove(participant); //모임멤버에서 멤거 제거
            if (remove) {
                super.unSubscribe(participant.getParticipant());
                participant.getParticipant().exitFromMeeting(participant);
                System.out.println(participant.getParticipant().getName() + " 모임멤버가 삭제되었습니다.");
            }
        } else {
            System.out.println("이미 제거된 모임멤법입니다.");
        }
    }

    @Override
    public void withDraw(Owner owner, Long accountNumber, Long withDraw) {
        if (this.owner.equals(owner)) {
            System.out.println("모임주가 출금한다.");
            super.withDraw(owner, accountNumber, withDraw);
        } else {
            System.out.println("모임주가 아니면 출금할 수 없습니다.");
        }
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

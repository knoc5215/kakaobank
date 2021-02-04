package me.jumen.kakaobank.account;

import lombok.*;
import me.jumen.kakaobank.owner.Owner;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "owner_no")
    private Owner owner;    //명의자

    private Long meetingAccountId;  //전환된 모임통장ID
    private boolean isConverted;    //전환여부


    @Builder
    public DepositAccount(Owner owner, String title) {
        super(AccountType.DEPOSIT, title);
        super.getObservers().add(owner);
        this.owner = owner;
        this.meetingAccountId = null;
        this.isConverted = false;
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

    //TODO 모임멤버 입금


}

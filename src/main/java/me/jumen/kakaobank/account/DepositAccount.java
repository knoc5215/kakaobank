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
@ToString(callSuper=true, includeFieldNames=true)
public class DepositAccount extends Account {


    @Id
    @GeneratedValue
    private Long accountNumber; //통장번호

    @ManyToOne
    @JoinColumn(name = "owner_no")
    private Owner owner;

    private Long meetingAccountId;
    private boolean isConverted;


    @Builder
    public DepositAccount(Owner owner, String title) {
        super(AccountType.DEPOSIT, title);
        super.getObservers().add(owner);
        this.owner = owner;
        this.meetingAccountId = null;
        this.isConverted = false;
        owner.addDepositAccount(this);
    }




}

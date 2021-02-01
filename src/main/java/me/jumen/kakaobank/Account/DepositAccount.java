package me.jumen.kakaobank.Account;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jumen.kakaobank.Client.Owner;

import javax.persistence.Entity;

/**
 * 입출금계좌
 */
@Entity
public class DepositAccount extends Account {
    @Builder
    public DepositAccount(Owner owner) {
        super(owner);

    }


    public DepositAccount() {

    }
}

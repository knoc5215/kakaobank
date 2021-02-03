package me.jumen.kakaobank.account;

import me.jumen.kakaobank.owner.Owner;

public class AccountUtil {
    public static DepositAccount getDepositAccount(Owner owner) {
        return DepositAccount.builder().owner(owner).build();
    }

    public static DepositAccount getDepositAccount(Owner owner, String title) {
        return DepositAccount.builder().owner(owner).title(title).build();
    }

    public static MeetingAccount getMeetingAccount(Owner owner) {
        return MeetingAccount.builder().owner(owner).build();
    }

    public static MeetingAccount getMeetingAccount(Owner owner, String title) {
        return MeetingAccount.builder().owner(owner).title("[모임계좌] " + title).build();
    }

    public static MeetingAccount getMeetingAccountFromDepositAccount(DepositAccount depositAccount, Owner owner, String title) {
        return MeetingAccount.builder().depositAccount(depositAccount).owner(owner).title(title).build();
    }

}

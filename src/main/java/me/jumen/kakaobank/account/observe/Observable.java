package me.jumen.kakaobank.account.observe;

import me.jumen.kakaobank.account.transaction.Transaction;
import me.jumen.kakaobank.owner.Owner;

public interface Observable {
    void subscribe(Owner owner);
    void unSubscribe(Owner owner);
    void notifyOwner(Transaction transaction);
}

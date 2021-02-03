package me.jumen.kakaobank.account.observe;

import me.jumen.kakaobank.account.transaction.Transaction;

public interface Observer {
    public void update(Transaction transaction);
}

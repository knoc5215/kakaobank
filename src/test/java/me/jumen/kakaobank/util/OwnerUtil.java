package me.jumen.kakaobank.util;

import me.jumen.kakaobank.owner.Owner;

public class OwnerUtil {
    public static Owner getOwner(String name, Integer age, String phoneNumber) {
        return Owner.builder().name(name).age(age).phoneNumber(phoneNumber).build();
    }

}

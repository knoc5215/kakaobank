package me.jumen.kakaobank.account;

import me.jumen.kakaobank.owner.Owner;
import me.jumen.kakaobank.owner.Participant;

public class ParticipantUtil {
    public static Participant getParticipant(Owner inviteOwner) {
        return Participant.builder().owner(inviteOwner).build();

    }
}

package me.jumen.kakaobank.Client;

import lombok.*;
import me.jumen.kakaobank.Account.MeetingAccount;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private MeetingAccount meetingAccount;

    @OneToOne
    private Owner participant;

    @Builder
    public Participant(Owner owner) {
        this.participant = owner;
    }


}

package me.jumen.kakaobank.owner;

import lombok.*;
import me.jumen.kakaobank.account.MeetingAccount;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue
    private Long id;

    private Date joinDate;  //모임참여시각

    @ManyToOne
    @JoinColumn(name = "meetingAccount_no")
    private MeetingAccount meetingAccount;  //모임계좌:모임멤버 = 1:N

    @ManyToOne
    @JoinColumn(name = "participant_no")
    private Owner participant;  //고객:모임멤버 = 1:N


    @Builder
    public Participant(Owner owner) {
        this.participant = owner;
        this.joinDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }




}

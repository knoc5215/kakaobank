package me.jumen.kakaobank.owner;

import lombok.*;
import me.jumen.kakaobank.account.MeetingAccount;
import me.jumen.kakaobank.account.transaction.FeeRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "participant")
    private Set<FeeRequest> feeRequests;    //회비요청

    @Builder
    public Participant(Owner owner) {
        this.participant = owner;
        this.joinDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        this.feeRequests = new HashSet<>();
    }

    public void receiveFeeRequest(FeeRequest feeRequest) {
        if (!this.feeRequests.contains(feeRequest)) {
            this.feeRequests.add(feeRequest);
        }
    }


}

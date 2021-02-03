package me.jumen.kakaobank.owner;

import lombok.*;
import me.jumen.kakaobank.account.MeetingAccount;
import me.jumen.kakaobank.account.transaction.FeeRequest;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "meetingAccount_no")
    private MeetingAccount meetingAccount;

    @ManyToOne
    @JoinColumn(name = "participant_no")
    private Owner participant;

    @OneToMany(mappedBy = "participant")
    private Set<FeeRequest> feeRequests;

    @Builder
    public Participant(Owner owner) {
        this.participant = owner;
        this.feeRequests = new HashSet<>();
    }

    public void receiveFeeRequest(FeeRequest feeRequest) {
        if (!this.feeRequests.contains(feeRequest)) {
            this.feeRequests.add(feeRequest);
        }
    }


}

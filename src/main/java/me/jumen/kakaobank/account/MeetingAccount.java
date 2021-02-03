package me.jumen.kakaobank.account;

import lombok.*;
import me.jumen.kakaobank.owner.Owner;
import me.jumen.kakaobank.owner.Participant;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, includeFieldNames = true)
public class MeetingAccount extends Account {

    @Id
    @GeneratedValue
    private Long accountNumber; //통장번호

    private Long depositAccountId;

    @ManyToOne
    @JoinColumn(name = "owner_no")
    private Owner owner;

    @OneToMany(mappedBy = "meetingAccount")
    private List<Participant> participants;


    @Builder
    public MeetingAccount(DepositAccount depositAccount, Owner owner, String title) {
        super(AccountType.MEETING, title);
        this.depositAccountId = depositAccount.getAccountNumber();
        depositAccount.setStatus(AccountStatus.SUSPEND);
        depositAccount.setConverted(true);

        this.owner = owner;
        owner.addMeetingAccount(this);
        this.participants = new ArrayList<>();

    }

    public void addParticipant(Owner owner) {
        if (this.participants.size() >= 100) {
            System.out.println("더 이상 모임멤버를 추가할 수 없습니다.");
            return;
        }

        if (owner.getNumOfMeetingsHeld().size() >= 30) {
            System.out.println("해당 고객은 더 이상 모임에 참여할 수 없습니다.");
            return;
        }

        if (!this.participants.contains(owner)) {
            Participant participant = Participant.builder().owner(owner).build();
            boolean add = this.participants.add(participant);
            if (add) {
                super.subscribe(owner);
                owner.getNumOfMeetingsHeld().add(participant);
                System.out.println(participant.getParticipant().getName() + " 모임멤버가 추가되었습니다.");
            }
        } else {
            System.out.println("이미 참여중인 모임멤버입니다.");
        }
    }

    public void removeParticipant(Participant participant) {
        if (this.participants.size() == 0) {
            System.out.println("제거할 모임멤버가 존재하지 않습니다.");
            return;
        }

        if (this.participants.contains(participant)) {
            boolean remove = this.participants.remove(participant);
            if (remove) {
                super.unSubscribe(participant.getParticipant());
                owner.getNumOfMeetingsHeld().remove(participant);
                System.out.println(participant.getParticipant().getName() + " 모임멤버가 삭제되었습니다.");
            }
        } else {
            System.out.println("이미 제거된 모임멤법입니다.");
        }
    }

}

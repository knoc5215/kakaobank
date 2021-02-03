package me.jumen.kakaobank.account.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jumen.kakaobank.owner.Participant;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FeeRequest {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    @JoinColumn(name = "participant_no")
    private Participant participant;


    private Date requestDate;   //요청시각
    private Date dueDate;   //회비입금기한시각

    private Long meetingAccountId;  //모임통장ID
    private Integer round;  //회차
    private Long dues;  //회비

    @Builder
    public FeeRequest(Date requestDate, Date dueDate, Long meetingAccountId, Integer round, Long dues) {
        this.requestDate = requestDate;
        this.dueDate = dueDate;
        this.meetingAccountId = meetingAccountId;
        this.round = round;
        this.dues = dues;
    }


}

package user.entity;

import javax.persistence.*;

enum RequestStatus {PENDING, ACCEPTED, REJECTED}


@Entity
@Table(name = "friend_requests")
public class friendRequestEntity {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private userEntity sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private userEntity receiver;
    
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private RequestStatus status;
}

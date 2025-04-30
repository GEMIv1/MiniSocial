package group.entity;

import javax.persistence.*;

import user.entity.userEntity;




enum RequestStatus {
    PENDING, APPROVED, REJECTED;
}

@Entity
@Table(name = "requests")
public class requestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private userEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private groupEntity group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

   

    
}

package group.entity;

import javax.persistence.*;

import user.entity.userEntity;


@Entity
@Table(name = "requests")
public class requestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private userEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private groupEntity group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;
    
    
    public requestEntity() {
    }

    public requestEntity(userEntity user, groupEntity group, RequestStatus status) {
        this.user = user;
        this.group = group;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public userEntity getUser() {
        return user;
    }

    public void setUser(userEntity user) {
        this.user = user;
    }

    public groupEntity getGroup() {
        return group;
    }

    public void setGroup(groupEntity group) {
        this.group = group;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
   

    
}

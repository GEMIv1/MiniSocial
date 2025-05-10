package user.entity;

import javax.persistence.*;



@Entity
@Table(name = "friend_requests")
public class friendRequestEntity {
	
	public friendRequestEntity() {
		
	}
	
	public friendRequestEntity(userEntity sender, userEntity receiver){
		this.sender = sender;
		this.receiver = receiver;
		this.status = RequestStatus.PENDING;
	}
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	

	@ManyToOne(optional = false)
	@JoinColumn(name = "sender", nullable = false)
    private userEntity sender;
    

    @ManyToOne(optional = false)  
    @JoinColumn(name = "receiver", nullable = false)
    private userEntity receiver;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
    
    public void setSender(userEntity sender) {this.sender = sender;}
    public void setReceiver(userEntity receiver) {this.receiver = receiver;}
    
    public userEntity getSender() {return sender;}
    public userEntity getReceiver() {return receiver;}
	public void setStatus(RequestStatus status) {this.status = status;}

}

package notifications.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import user.entity.userEntity;

@Entity
@Table(name="notifications", uniqueConstraints = @UniqueConstraint(columnNames = {"actor_user_id","recipient_user_id","type","post_id","group_id"}))
public class notificationEntity implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
		 
	 @Enumerated(EnumType.STRING)
	 @Column(name = "type", nullable = false)
	 private NotificationType type;
	 
	 @Column(name = "post_id")
	 private Integer postId;

	 @Column(name = "group_id")
	 private int groupId;
	 
	 @Column(name = "is_read", nullable = false)
	 private boolean read = false;
	 
	 @Column(name = "created_at", nullable = false, updatable = false)
	 private Instant createdAt = Instant.now();
	 
	 @ManyToOne()
	 @JoinColumn(name = "recipient_user_id", nullable = false)
	 private userEntity recipient;

	 @ManyToOne()
	 @JoinColumn(name = "actor_user_id", nullable = false)
	 private userEntity actor;
	 
	 public notificationEntity() { }

	 public notificationEntity(userEntity recipientUser, userEntity actorUser, NotificationType type, int postId, int groupId) {
	        this.recipient = recipientUser;
	        this.actor = actorUser;
	        this.type = type;
	        this.postId = postId;
	        this.groupId = groupId;
	    }

	    public int getId() {
	        return id;
	    }

	    public userEntity getRecipientUser() {
	        return recipient;
	    }

	    public void setRecipientUser(userEntity recipientId) {
	        this.recipient = recipientId;
	    }

	    public userEntity getActorUser() {
	        return actor;
	    }

	    public void setActorUser(userEntity actorUser) {
	        this.actor = actorUser;
	    }

	    public NotificationType getType() {
	        return type;
	    }

	    public void setType(NotificationType type) {
	        this.type = type;
	    }

	    public int getPostId() {
	        return postId;
	    }

	    public void setPostId(int postId) {
	        this.postId = postId;
	    }

	    public int getGroupId() {
	        return groupId;
	    }

	    public void setGroupId(int groupId) {
	        this.groupId = groupId;
	    }

	    public boolean isRead() {
	        return read;
	    }

	    public void setRead(boolean read) {
	        this.read = read;
	    }

	    public Instant getCreatedAt() {
	        return createdAt;
	    }

	    @PrePersist
	    protected void onCreate() {
	        createdAt = Instant.now();
	    }

		public int getNotificationId() {
			return id;
		}
}

package post.entity;

import java.sql.Date;

import javax.persistence.*;

import user.entity.userEntity;

@Entity
@Table(
	name = "likes",
	uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "post_id" })
	)
public class likeEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeId;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "user_id", nullable = false)
    private userEntity user;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "post_id", nullable = false)
    private postEntity post;

	public void setUser(userEntity user2) {this.user = user2;}

	public void setPost(postEntity post2) {this.post = post2;}
	
    /*
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    */
}

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
    private Long likeId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
    private userEntity user;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "post_id", nullable = false)
    private postEntity post;
	
    /*
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    */
}

package post.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import user.entity.userEntity;

@Entity
@Table(name="posts")
public class postEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
	
	@Column(nullable = false)
    private String content;
	
  
	@ManyToOne()
	@JoinColumn(name = "author_id")
	private userEntity author;
	
	@OneToMany(mappedBy = "post")
	private List<commentEntity> comments = new ArrayList<>();
	
    @OneToMany(mappedBy = "post")
    private List<likeEntity> likes = new ArrayList<>();
    
    
    /*
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    */
}


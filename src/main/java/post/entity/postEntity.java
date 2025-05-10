package post.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import group.entity.groupEntity;
import user.entity.userEntity;

@Entity
@Table(name="posts")
public class postEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
	
	@Column(nullable = false)
    private String content;
	
  
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private userEntity author;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<commentEntity> comments = new ArrayList<>();
	
    @OneToMany(mappedBy = "post",  cascade =  CascadeType.ALL)
    private List<likeEntity> likes = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private groupEntity group;
    
    public void setAuthor(userEntity author) {this.author = author;}
    
    public userEntity getAuthor() {
        return author;
    }
    
    public int getPostId() {
        return postId;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

	public List<commentEntity> getComments() {
		return comments;
	}
	public groupEntity getGroup() {
		 return group;
	}

	public void setGroup(groupEntity group) {
		this.group = group;
	}

    
    
    
    /*
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    */
}


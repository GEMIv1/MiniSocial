package post.entity;

import java.sql.Date;

import javax.persistence.*;

import user.entity.userEntity;

@Entity
@Table(name = "comments")
public class commentEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private userEntity author;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private postEntity post;
    
    public void setPost(postEntity post) {this.post = post;}
    public postEntity getComment() {return post;}
    public void setAuthor(userEntity author) {this.author = author;}
    public userEntity getAuthor() {return author;}
    public void setContent(String content) {this.content = content;}
    public String getContent() {return content;}
    
    /*
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
    */
}

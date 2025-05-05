package group.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import post.entity.postEntity;
import user.entity.userEntity;

@Entity
@Table(name = "groups")
public class groupEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
	private Long groupId;
	
	@Column(nullable = false, unique = true)
    private String name;
	
	private String description;
	
	@Column(name = "is_open")
    private boolean open;
	
	@ManyToOne()
    @JoinColumn(name = "creator_id")
    private userEntity creator;
	
	@ManyToMany(mappedBy = "groups")
	List<userEntity> users = new ArrayList<>();
	
	@OneToMany(mappedBy = "group")
	List<postEntity> grpPosts = new ArrayList<>();
	
	@OneToMany(mappedBy = "group")
	private List<requestEntity> requests = new ArrayList<>();
	
	
	/*@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
	*/
	
}

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
	private int groupId;
	
	@Column(nullable = false ,unique = true)
    private String name;
	
	private String description;
	
	@Column(name = "is_open", nullable = false ,columnDefinition = "bit default 0 not null")
    private boolean open;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private userEntity creator;
	
	@ManyToMany(mappedBy = "groups")
	List<userEntity> users = new ArrayList<>();
	
	@OneToMany(mappedBy = "group")
	List<postEntity> grpPosts = new ArrayList<>();
	
	@OneToMany(mappedBy = "group")
	private List<requestEntity> requests = new ArrayList<>();
	
	
	public int getGrpId() {return this.groupId;}
	public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
	public void setCreator(userEntity creator) {this.creator = creator;}
	public void setGrpStatus(boolean open) {this.open = open;}
	public boolean getGrpStatus() {return open;}
	public userEntity getCreator() {return creator;}
	public void setDescription(String description) {this.description = description;}
	public String getDescription() {return description;}
	public List<userEntity> getAllUsrInGrp(){return users;}
	public List<postEntity> getAllPostsInGrp(){return grpPosts;}
	public List<requestEntity> getAllRequestsInGrp(){return requests;}
	
	
	
	
	
	/*@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
	*/
	
}

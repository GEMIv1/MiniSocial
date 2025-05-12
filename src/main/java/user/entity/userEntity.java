package user.entity;
import java.util.*;
import javax.persistence.*;
import group.entity.groupEntity;
import group.entity.requestEntity;
import notifications.entity.notificationEntity;
import post.entity.commentEntity;
import post.entity.likeEntity;
import post.entity.postEntity;
@Entity
@Table(name="users")
public class userEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	int userId;
	@Column(name = "email", nullable = false, unique = true)
	String email;
	@Column(nullable = false)
	String password;
	String name;
	String bio;
	@Enumerated(EnumType.STRING)
	Role role;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
	List<postEntity> posts = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_friends",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
	List<userEntity> friends = new ArrayList<>();
	
	@ManyToMany(mappedBy = "friends")
    private List<userEntity> friendOf = new ArrayList<>();
		
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_groups", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
	List<groupEntity> groups = new ArrayList<>();
	
	@OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE)
	List<friendRequestEntity> friendRequestRecieved = new ArrayList<>();
	
	@OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
	List<friendRequestEntity> friendRequestSent = new ArrayList<>();
	
	@OneToMany(mappedBy = "creator")
	List<groupEntity> groupsCreated = new ArrayList<>();
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
	List<commentEntity> comments = new ArrayList<>();
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	List<likeEntity> likes = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<requestEntity> requests = new ArrayList<>();
	
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.REMOVE)
    private List<notificationEntity> notificationsReceived = new ArrayList<>();
    @OneToMany(mappedBy = "actor", cascade = CascadeType.REMOVE)
    private List<notificationEntity> notificationsSent = new ArrayList<>();
	
	public String getEmail() { return email; }
	public void   setEmail(String email) { this.email = email; }
	public String getPassword() { return password; }
	public void   setPassword(String password) { this.password = password; }
	public String getName() { return name; }
	public void   setName(String name) { this.name = name; }
	public String getBio() { return bio; }
	public void   setBio(String bio) { this.bio = bio; }
	public Role   getRole() { return role; }
	public void   setRole(Role role) { this.role = role; }
	public int getId() {return this.userId;}
	public List<friendRequestEntity> getFriendRequestRecieved(){return this.friendRequestRecieved;}
	public List<friendRequestEntity> getfriendRequestSent(){return this.friendRequestSent;}
	public List<userEntity> getFriends(){return this.friends;}
	public List<postEntity> getPosts() {return this.posts;}
	public List<groupEntity> getGroups(){return this.groups;}
	public List<userEntity> getFriendOf() {return this.friendOf;}
	public List<groupEntity> getGroupsCreated(){return this.groupsCreated;}
}
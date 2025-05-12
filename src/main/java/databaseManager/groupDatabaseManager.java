package databaseManager;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import group.entity.groupEntity;
import group.interfaces.repositories.IGroupRepository;
import post.entity.postEntity;
import user.entity.userEntity;

@Stateless
public class groupDatabaseManager implements IGroupRepository{

	@PersistenceContext(unitName = "hello")
	EntityManager em;
	
	@Override
	public void save(groupEntity grp) {
		em.persist(grp);
		
	}
	
	@Override
	public int addUserInGrp(int usrId, int grpId) {
		return em.createNativeQuery(
	            "INSERT INTO user_groups (user_id, group_id) VALUES (:uid, :gid)"
	        )
	      .setParameter("uid", usrId)
	      .setParameter("gid", grpId)
	      .executeUpdate();
		
	}
	
	@Override
	public int removeUserInGrp(int usrId, int grpId) {
		return em.createNativeQuery(
		        "DELETE FROM user_groups WHERE user_id = :uid AND group_id = :gid"
	        )
	      .setParameter("uid", usrId)
	      .setParameter("gid", grpId)
	      .executeUpdate();
		
	}

	@Override
	public List<userEntity> getAllUsersInGrp(int grpId) {
		return em.createQuery(
	            "SELECT u FROM groupEntity g JOIN g.members u WHERE g.groupId = :grpId",
	            userEntity.class
	        )
	        .setParameter("grpId", grpId)
	        .getResultList();
	}

	@Override
	public int createPostInGrp(int grpId, int authorId, postEntity post) {
		return em.createNativeQuery("INSERT INTO posts (content, author_id, group_id) VALUES (:content, :authorId, :groupId)")
	      .setParameter("content",   post.getContent())
	      .setParameter("authorId",  authorId)
	      .setParameter("groupId",   grpId)
	      .executeUpdate();
		
	}

	@Override
	public List<postEntity> getAllPostsInGrp(int grpId) {
		return em.createQuery("SELECT p FROM postEntity p WHERE p.group.groupId = :grpId",postEntity.class).setParameter("grpId", grpId).getResultList();
	}

	@Override
	public userEntity getAuthor(int groupId) {
		return em.createQuery(
		          "SELECT g.creator FROM groupEntity g WHERE g.groupId = :gid",
		          userEntity.class)
		        .setParameter("gid", groupId)
		        .getSingleResult();
	}

	@Override
	public int updateDescription(int grpId, String newDescription) {
		 return em.createQuery(
	                "UPDATE groupEntity g " +
	                "SET g.description = :newDescription " +
	                "WHERE g.groupId = :grpId")
	            .setParameter("newDescription", newDescription)
	            .setParameter("grpId", grpId)
	            .executeUpdate();		
	}

	@Override
	public int updateStatus(int grpId, boolean status) {
		return em.createQuery(
                "UPDATE groupEntity g " +
                "SET g.open = :status " +
                "WHERE g.groupId = :grpId")
            .setParameter("status", status)
            .setParameter("grpId", grpId)
            .executeUpdate();		
	}

	@Override
	public int deleteGroup(int grpId) {
	    groupEntity g = em.find(groupEntity.class, grpId);
	    if (g == null) {
	        return 0;
	    }

	    g.getAllUsrInGrp().clear();

	    em.remove(g);
	    return 1;
	}

	@Override
	public boolean getStatus(int grpId) {
		return (boolean) em.createQuery("SELECT g.open FROM groupEntity g WHERE g.groupId = :id").setParameter("id", grpId).getSingleResult();
	}

	@Override
	public groupEntity getGroupById(int grpId) {
		return em.createQuery("SELECT g FROM groupEntity g WHERE g.groupId = :id", groupEntity.class).setParameter("id", grpId).getSingleResult();
	}

	@Override
	public void removeAllGroupMembers(int grpId) {
		 em.createNativeQuery(
			        "DELETE FROM user_groups WHERE group_id = :grpId"
			    )
			    .setParameter("grpId", grpId)
			    .executeUpdate();
		
	}

	@Override
	public void deleteAllGroupPosts(int grpId) {
		em.createQuery(
	            "DELETE FROM postEntity p WHERE p.group.groupId = :grpId"
	        )
	        .setParameter("grpId", grpId)
	        .executeUpdate();
		
	}


}

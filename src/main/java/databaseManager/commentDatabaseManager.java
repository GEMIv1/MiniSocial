package databaseManager;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import post.entity.commentEntity;
import post.entity.postEntity;
import user.interfaces.repositories.ICommentRepository;

@Stateless
public class commentDatabaseManager implements ICommentRepository {

	
	@PersistenceContext(unitName = "hello")
	EntityManager em;
	
	@Override
	public void save(commentEntity comment) {
		em.persist(comment);
		
	}

	@Override
	public void delete(commentEntity comment) {
		em.createQuery("DELETE FROM commentEntity c WHERE c = :comment").setParameter("comment", comment);
		
	}

	@Override
	public long countCommentByPost(postEntity post) {
		long cnt = (long) em.createQuery("SELECT COUNT(c) FROM commentEntity c WHERE c.post = :post").setParameter("post", post).getSingleResult();
	    return cnt;
	}

}

package databaseManager;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import post.entity.likeEntity;
import post.entity.postEntity;
import user.entity.userEntity;
import user.interfaces.repositories.ILikeRepository;


@Stateless
public class likeDatabaseManager implements ILikeRepository {
	
	@PersistenceContext(unitName = "hello")
	EntityManager em;
	

	@Override
	public void save(likeEntity like) {
		em.persist(like);
	}


	@Override
	public likeEntity findByUserAndPost(userEntity user, postEntity post) {
		try {
	        return em.createQuery(
	            "SELECT l FROM likeEntity l WHERE l.user = :user AND l.post = :post",
	            likeEntity.class)
	          .setParameter("user", user)
	          .setParameter("post", post)
	          .getSingleResult();
	    } catch (NoResultException e) {
	        return null;
	    }
	}


	@Override
	public void delete(likeEntity existingLike) {
		likeEntity managed = em.contains(existingLike)
                ? existingLike
                : em.merge(existingLike);
			em.remove(managed);
	}


	@Override
	public long countLikesByPost(postEntity post) {
		long cnt =  (long) em.createQuery("SELECT COUNT(l) FROM likeEntity l WHERE l.post = :post").setParameter("post", post).getSingleResult();
		return cnt;
	}

}

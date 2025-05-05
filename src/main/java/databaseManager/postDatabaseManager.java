package databaseManager;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import post.entity.commentEntity;
import post.entity.postEntity;
import post.interfaces.repositories.IPostRepository;

@Stateless
public class postDatabaseManager implements IPostRepository{
	
	@PersistenceContext(unitName = "hello")
	EntityManager em;

	@Override
	public void createPost(postEntity post) {
		em.persist(post);
	}
	
	@Override
	public postEntity updateContent(int postId, String newContent, int authorId) {
		
		postEntity post = em.createQuery(
		        "SELECT p FROM postEntity p LEFT JOIN FETCH p.author WHERE p.id = :postId", 
		        postEntity.class
		    ).setParameter("postId", postId).getSingleResult();

		    post.setContent(newContent);
		    return em.merge(post);
	  }

	@Override
	public int delete(int postId) {
		return em.createQuery("DELETE FROM postEntity p WHERE p.postId = :id")
                .setParameter("id", postId)
                .executeUpdate();
   }

	@Override
	public postEntity findById(int postId) {
		
		return em.createQuery(
		        "SELECT p FROM postEntity p " +
		                "LEFT JOIN FETCH p.author " +
		                "WHERE p.id = :postId", postEntity.class)
		                .setParameter("postId", postId)
		                .getSingleResult();
	}
	
    public List<commentEntity> findByPostId(int postId) {
        return em.createQuery(
                "SELECT c FROM commentEntity c WHERE c.post.postId = :id",
                commentEntity.class)
            .setParameter("id", postId)
            .getResultList();
    }
}

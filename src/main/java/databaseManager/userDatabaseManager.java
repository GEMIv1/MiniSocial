package databaseManager;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import user.entity.friendRequestEntity;
import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;

// use named parameter query

@Stateless
public class userDatabaseManager implements IuserRepository {
	
	
	
	@PersistenceContext(unitName = "hello")
	EntityManager em;

	@Override
	public void save(userEntity user) {
		em.persist(user);	
	}

	@Override
	public userEntity findById(int id) {
		
		try {
			return em.createQuery("select u from userEntity u where u.id = :id", userEntity.class).setParameter("id", id).getSingleResult();
			
		}catch(NoResultException e) {
			System.out.print(e.getMessage());
			return null;
		}
	}

	@Override
	public userEntity findByEmail(String email) {
		  try {
		        return em.createQuery("SELECT u FROM userEntity u WHERE u.email = :email", userEntity.class).setParameter("email", email).getSingleResult();
		    } catch (NoResultException e) {
				System.out.print(e.getMessage());
		        return null;
		    }	
	}

	@Override
	public List<userEntity> findAll() {
		return em.createQuery("SELECT u FROM UserEntity u", userEntity.class).getResultList();
	}

	@Override
	public void update(userEntity user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(userEntity user) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void addNewFriend(int userId, int newFriendId) {
        em.createNativeQuery(
            "INSERT INTO user_friends (user_id, friend_id) VALUES (:uid, :fid)")
          .setParameter("uid", userId)
          .setParameter("fid", newFriendId)
          .executeUpdate();
    }

	public List<userEntity> getAllFriend(int id) {
	    return em.createQuery(
	        "SELECT f FROM userEntity u JOIN u.friends f WHERE u.id = :id",
	        userEntity.class)
	      .setParameter("id", id)
	      .getResultList();
	}

}

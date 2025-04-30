package databaseManager;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public userEntity findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
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

}

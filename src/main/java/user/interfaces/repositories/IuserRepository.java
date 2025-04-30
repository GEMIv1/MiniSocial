package user.interfaces.repositories;

import java.util.List;

import user.entity.userEntity;

public interface IuserRepository {

    void save(userEntity user);

    userEntity findById(int id);

    userEntity findByEmail(String email);

    List<userEntity> findAll();

    void update(userEntity user);

    void delete(userEntity user);
}
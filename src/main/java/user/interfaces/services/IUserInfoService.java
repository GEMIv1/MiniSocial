package user.interfaces.services;

import javax.ws.rs.core.Response;

import user.entity.userEntity;

public interface IUserInfoService <T>{
	Response updateName(userEntity user);
	Response updatePassword(userEntity user);
	Response updateEmail(userEntity user);
	Response updateBio(userEntity user);
}

package user.interfaces.services;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import user.entity.userEntity;

public interface ISearchService {



	List<userEntity> searchUsers(String searchTerm, HttpServletRequest servlet, int limit);

	List<Map<String, Object>> getFriendSuggestions(HttpServletRequest servlet, int limit);

}

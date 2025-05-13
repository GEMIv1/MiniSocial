package user.interfaces.services;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import user.entity.userEntity;

public interface ISearchService {



	Response searchUsers(String searchTerm, HttpServletRequest servlet, int limit);

	Response getFriendSuggestions(HttpServletRequest servlet, int limit);

}

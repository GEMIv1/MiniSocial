package user.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;
import user.interfaces.services.ISearchService;

@Stateless
@Local(ISearchService.class)
public class searchService implements ISearchService {
    
    @Inject
    private IuserRepository userDatabaseManager;
    
    @Override
    public Response searchUsers(String searchTerm, HttpServletRequest servlet, int limit) {
        HttpSession session = servlet.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("User not authenticated")
                           .build();
        }
        int currentUserId = (Integer) session.getAttribute("userId");

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return Response.ok(Collections.emptyList()).build();
        }

        List<userEntity> results =
            userDatabaseManager.searchUsers(searchTerm.trim().toLowerCase(), currentUserId);

        if (limit > 0 && results.size() > limit) {
            results = results.subList(0, limit);
        }
        
        List<Map<String,Object>> users = new ArrayList<>();
        for (userEntity u : results) {
            Map<String,Object> m = new HashMap<>();
            m.put("id",    u.getId());
            m.put("name",  u.getName());
            m.put("email", u.getEmail());
            m.put("friendCount", u.getFriends().size());
            users.add(m);
        }

        Map<String,Object> resp = new HashMap<>();
        resp.put("count", users.size());
        resp.put("users", users);

        return Response.ok(resp).build();

    }

    @Override
    public Response getFriendSuggestions(HttpServletRequest servlet, int limit) {
        HttpSession session = servlet.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("User not authenticated")
                           .build();
        }
        int userId = (Integer) session.getAttribute("userId");

        userEntity currentUser = userDatabaseManager.findById(userId);
        if (currentUser == null) {
            return Response.ok(Collections.emptyList()).build();
        }

        List<userEntity> currentUserFriends = currentUser.getFriends();
        List<Integer> currentUserFriendIds = currentUserFriends.stream()
            .map(userEntity::getId)
            .collect(Collectors.toList());

        List<userEntity> potentialFriends =
            userDatabaseManager.findNonFriendUsers(userId, currentUserFriendIds);

        List<Map<String, Object>> suggestions = new ArrayList<>();
        for (userEntity pf : potentialFriends) {
            List<userEntity> mutual = findMutualFriends(currentUserFriends, pf.getFriends());
            if (mutual.isEmpty()) continue;

            Map<String,Object> userMap = new HashMap<>();
            userMap.put("id",       pf.getId());
            userMap.put("name",     pf.getName());
            userMap.put("email",    pf.getEmail());

            List<Map<String,Object>> mutualMaps = new ArrayList<>();
            for (userEntity m : mutual) {
                Map<String,Object> mm = new HashMap<>();
                mm.put("id",    m.getId());
                mm.put("name",  m.getName());
                mm.put("email", m.getEmail());
                mutualMaps.add(mm);
            }

            Map<String,Object> suggestion = new HashMap<>();
            suggestion.put("user",         userMap);
            suggestion.put("mutualFriends", mutualMaps);
            suggestion.put("mutualCount",  mutualMaps.size());

            suggestions.add(suggestion);
        }

        suggestions.sort((a,b) -> ((Integer)b.get("mutualCount")) - ((Integer)a.get("mutualCount")));

        if (limit > 0 && suggestions.size() > limit) {
            suggestions = suggestions.subList(0, limit);
        }

        return Response.ok(suggestions).build();
    }

    
    private List<userEntity> findMutualFriends(List<userEntity> friends1, List<userEntity> friends2) {
        if (friends1.isEmpty() || friends2.isEmpty()) {
            return Collections.emptyList();
        }

        return friends1.stream()
                .filter(friends2::contains)
                .collect(Collectors.toList());
    }
}
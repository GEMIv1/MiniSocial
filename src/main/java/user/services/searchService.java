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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;
import user.interfaces.services.ISearchService;

@Stateless
@Local(ISearchService.class)
public class searchService implements ISearchService {
    
    @PersistenceContext
    private IuserRepository userDatabaseManager;
    
    @Override
    public List<userEntity> searchUsers(String searchTerm, HttpServletRequest servlet, int limit) {
    	
    	HttpSession Session = servlet.getSession(false);
    	int currentUserId = (Integer) Session.getAttribute("userId");
    	
    	
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        List<userEntity> results = userDatabaseManager.searchUsers(searchTerm.trim().toLowerCase(), currentUserId);

        if (limit > 0 && results.size() > limit) {
            return results.subList(0, limit);
        }
        
        return results;
    }
    
    @Override
    public List<Map<String, Object>> getFriendSuggestions(HttpServletRequest servlet, int limit) {
        
    	HttpSession Session = servlet.getSession(false);
    	int userId = (Integer) Session.getAttribute("userId");
    	
    	userEntity currentUser = userDatabaseManager.findById(userId);
        if (currentUser == null) {
            return Collections.emptyList();
        }

        List<userEntity> currentUserFriends = currentUser.getFriends();
        List<Integer> currentUserFriendIds = currentUserFriends.stream()
                .map(userEntity::getId)
                .collect(Collectors.toList());

        List<userEntity> potentialFriends = userDatabaseManager.findNonFriendUsers(userId, currentUserFriendIds);
        List<Map<String, Object>> suggestions = new ArrayList<>();

        for (userEntity potentialFriend : potentialFriends) {
            List<userEntity> mutualFriends = findMutualFriends(currentUserFriends, potentialFriend.getFriends());

            if (!mutualFriends.isEmpty()) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("user", potentialFriend);
                suggestion.put("mutualFriends", mutualFriends);
                suggestion.put("mutualCount", mutualFriends.size());
                suggestions.add(suggestion);
            }
        }

        suggestions.sort(Comparator.comparing(s -> -((Integer) s.get("mutualCount"))));

        if (limit > 0 && suggestions.size() > limit) {
            return suggestions.subList(0, limit);
        }

        return suggestions;
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
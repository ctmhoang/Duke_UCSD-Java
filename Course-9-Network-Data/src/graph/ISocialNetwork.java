package graph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface ISocialNetwork {
    Map<Integer, Set<Integer>> getPotentialFriends(int userId);

    HashSet<Integer> getMinToAnnounce(Mode mode);
}

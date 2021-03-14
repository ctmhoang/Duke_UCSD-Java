package graph;

import java.util.Map;
import java.util.Set;

public interface ISocialNetwork {
    Map<Integer, Set<Integer>> getPotentialFriends(int userId);
}

/** */
package graph;

import util.GraphLoader;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Your name here.
 *     <p>For the warm up assignment, you must implement your Graph in a class named CapGraph. Here
 *     is the stub file.
 */
public class CapGraph implements Graph, ISocialNetwork {

  private final HashMap<Integer, HashSet<Integer>> vertices;

  public CapGraph() {
    this.vertices = new HashMap<>();
  }

  /* (non-Javadoc)
   * @see graph.Graph#addVertex(int)
   */
  @Override
  public void addVertex(int num) {
    vertices.putIfAbsent(num, new HashSet<>());
  }

  /* (non-Javadoc)
   * @see graph.Graph#addEdge(int, int)
   */
  @Override
  public void addEdge(int from, int to) {
    addVertex(from);
    addVertex(to);
    vertices.get(from).add(to);
  }

  /* (non-Javadoc)
   * @see graph.Graph#getEgonet(int)
   */
  @Override
  public Graph getEgonet(int center) {
    Graph res = new CapGraph();
    if (vertices.containsKey(center)) {
      Set<Integer> directedVertices = vertices.get(center);
      populateGraph(directedVertices, res);
    }
    return res;
  }

  /* (non-Javadoc)
   * @see graph.Graph#getSCCs()
   */
  @Override
  public List<Graph> getSCCs() {
    // TODO Auto-generated method stub
    if (vertices.isEmpty()) return null;
    Set<Integer> discovered = new HashSet<>();

    Stack<Integer> res = new Stack<>();
    for (int ver : vertices.keySet()) {
      if (discovered.contains(ver)) continue;
      discovered.add(ver);
      res.addAll(DFSHelper(ver, discovered, this));
    }

    discovered.clear();
    List<Stack<Integer>> SCCs = new LinkedList<>();

    CapGraph transGraph = transpose();
    while (!(res.isEmpty())) {
      int tmp = res.pop();
      if (discovered.contains(tmp)) continue;
      discovered.add(tmp);
      SCCs.add(DFSHelper(tmp, discovered, transGraph));
    }

    return getGraphList(SCCs);
  }

  private List<Graph> getGraphList(List<Stack<Integer>> SCCs) {
    List<Graph> graps = new LinkedList<>();
    for (Stack<Integer> data : SCCs) {
      Graph e = new CapGraph();
      populateGraph(data, e);
      graps.add(e);
    }
    return graps;
  }

  private void populateGraph(Collection<Integer> data, Graph e) {
    data.forEach(e::addVertex);

    data.stream()
        .collect(
            Collectors.groupingByConcurrent(
                Function.identity(),
                Collectors.mapping(
                    v -> vertices.get(v).stream().filter(data::contains), Collectors.toSet())))
        .forEach(
            (key, svalues) -> svalues.forEach(vals -> vals.forEach(val -> e.addEdge(key, val))));
  }

  private Stack<Integer> DFSHelper(int vertex, Set<Integer> discovered, CapGraph data) {
    Set<Integer> connected = data.exportGraph().get(vertex);
    Stack<Integer> res = new Stack<>();
    for (int ver : connected) {
      if (!discovered.contains(ver)) {
        discovered.add(ver);
        res.addAll(DFSHelper(ver, discovered, data));
      }
    }
    res.push(vertex);
    return res;
  }

  private CapGraph transpose() {
    CapGraph res = new CapGraph();
    vertices.keySet().forEach(res::addVertex);
    vertices.forEach((key, values) -> values.forEach(val -> res.addEdge(val, key)));
    return res;
  }

  /* (non-Javadoc)
   * @see graph.Graph#exportGraph()
   */
  @Override
  public HashMap<Integer, HashSet<Integer>> exportGraph() {
    return vertices;
  }

  @Override
  public Map<Integer, Set<Integer>> getPotentialFriends(int userId) {
    if (!vertices.containsKey(userId)) return new HashMap<>();
    List<Integer> data = new ArrayList<>(vertices.get(userId));
    // Get all of potential friends after the current user's id index in the list
    // who are not friend with the currently processing user's id
    // and add them to the set of stream users id (jdk 8 do not support flatmap in
    // Collectors.mapping)
    Map<Integer, Set<Stream<Integer>>> tmp =
        data.stream()
            .collect(
                Collectors.groupingBy(
                    Function.identity(),
                    Collectors.mapping(
                        v ->
                            data.subList(data.indexOf(v) + 1, data.size()).stream()
                                .filter(i -> !vertices.get(v).contains(i)),
                        Collectors.toSet())));
    Map<Integer, Set<Integer>> res = new HashMap<>();
    // detach the map of actual user's id with their potential friend's ids from the streams of
    // user's ids
    tmp.forEach((k, svs) -> svs.forEach(sv -> res.putIfAbsent(k, sv.collect(Collectors.toSet()))));
    // populate potential friend's ids from the set back to the keys ( cuz in the previous step,
    // we only add potential friend's ids after the processing user's id)
    res.forEach((k, v) -> v.parallelStream().forEach(val -> res.get(val).add(k)));
    return res;
  }

  @Override
  public HashSet<Integer> getMinToAnnounce(Mode mode) {
    switch (mode) {
      case GREEDY_DFS_DYNA:
        return getMinByGDD();
      case RLS:
        // TODO: Implement this
      default:
        return null;
    }
  }

  /**
   * This function run DFS + Greedy + Dynamic Programming Algo to get mds
   *
   * @return mds
   */
  private HashSet<Integer> getMinByGDD() {
    HashSet<Integer> res = new HashSet<>();
    Set<Integer> discovered = new HashSet<>();
    Map<Integer, Set<Integer>> uncovered = new HashMap<>();
    // buffer to keep track of not yet populated ids
    Map<Integer, Set<Integer>> tempUnpopulated = new HashMap<>();
    for (int ver : vertices.keySet()) {
      if (uncovered.containsKey(ver)) continue;
      discovered.add(ver);
      DFSPopulateUncovered(ver, discovered, uncovered, tempUnpopulated);
    }
    System.out.println(uncovered);
    while (!uncovered.isEmpty()) {
      int vertex = getMostUncoveredVertex(uncovered);

      res.add(vertex);
      Set<Integer> coveredSet = uncovered.get(vertex);
      coveredSet.add(vertex);

      uncovered = updateUncoveredNode(coveredSet, uncovered);
    }
    return res;
  }

  /**
   * Update the current currentUncoveredNodes collection if the coveredVertices take out all of ids
   * in that
   *
   * @param coveredVertices all the ids need to take out of the currentUncoveredNodes
   * @param currentUncoveredNodes current uncovered collection of ids need to update
   * @return the new map with the updated ids
   */
  private Map<Integer, Set<Integer>> updateUncoveredNode(
      Set<Integer> coveredVertices, Map<Integer, Set<Integer>> currentUncoveredNodes) {
    Map<Integer, Set<Integer>> res = new HashMap<>(currentUncoveredNodes);
    return res.entrySet().stream()
        .filter(entry -> !coveredVertices.contains(entry.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * This method return the first ids with the most covered id in the graph
   *
   * @param data map of the id and the deeply associated ids
   * @return id of the user
   */
  private int getMostUncoveredVertex(Map<Integer, Set<Integer>> data) {
    return Collections.max(
            data.entrySet(), Comparator.comparingInt(entry -> entry.getValue().size()))
        .getKey();
  }

  /**
   * Perform DFS to populate pass-in `populatedData` params
   *
   * @param vertex id choose as root to operate DFS algo
   * @param discovered set of discovered ids
   * @param populatedData Map of user's ids and set of deeply-searched friend's ids
   * @param unpopulated Map of user's id and set of friend's related ids which have not had a DFS
   *     search result of this user's id
   * @return a set of ids in each recursive level which actually we do not need to look into :v
   *     <p>Remember in dfs we processing node in tree in top-down approach
   */
  private Set<Integer> DFSPopulateUncovered(
      int vertex,
      Set<Integer> discovered,
      Map<Integer, Set<Integer>> populatedData,
      Map<Integer, Set<Integer>> unpopulated) {

    // get directly connected ids with currently processing `vertex` id
    Set<Integer> connected = vertices.get(vertex);
    // current node we've travelled
    Set<Integer> currRes = new HashSet<>(connected);
    for (int ver : connected) {
      if (!discovered.contains(ver)) {
        // mark as discovered so we don't need to visit it again
        discovered.add(ver);
        // DFS it
        Set<Integer> vals = DFSPopulateUncovered(ver, discovered, populatedData, unpopulated);
        // need to remove vertex from vals cuz some situations, we put it accidentally in the
        // following stack
        vals.remove(vertex);
        currRes.addAll(vals);
        // check if the current vals we got from recursive calls is in unpopulated or discovered
        // above
        if (vals.stream()
            .anyMatch(val -> unpopulated.containsKey(val) || discovered.contains(val))) {
          // if it in unpopulated map, get the entry set with it as the key and add current
          // processing id
          // (has directly ship with the current pass-in index) as a value to populated it later
          vals.stream()
              .filter(unpopulated::containsKey)
              .forEach(val -> unpopulated.get(val).add(ver));

          // if discovered which means we had seen this id before and have searched all its neighbor
          // yet
          vals.stream()
              .filter(discovered::contains)
              .forEach(
                  val -> {
                    Set<Integer> tmp = unpopulated.get(val);
                    if (tmp != null) tmp.add(vertex);
                    else {
                      Set<Integer> newData = new HashSet<>(Collections.singletonList(vertex));
                      unpopulated.put(val, newData);
                    }
                  });
        }
      }
      // if discovered which means we had seen this id before and have searched all its neighbor yet
      else {
        // put it to the map
        if (!unpopulated.containsKey(ver)) {
          unpopulated.put(ver, new HashSet<>(Collections.singletonList(vertex)));
        } else {
          unpopulated.get(ver).add(vertex);
        }
      }
    }

    // put the current result to the data
    populatedData.put(vertex, new HashSet<>(currRes));

    // Check the unpopulated map has the vertex we already has the result
    if (unpopulated.containsKey(vertex)) {
      // if it has, populate it with the result value we get above
      Set<Integer> newData = populatedData.get(vertex);
      unpopulated
          .get(vertex)
          .forEach(
              unpol -> {
                Set<Integer> tmpData = new HashSet<>(newData);
                tmpData.remove(unpol);
                tmpData.addAll(populatedData.get(unpol));
                populatedData.put(unpol, tmpData);
              });
      // after the population in those record, remove all the associated with friend's ids and the
      // processing id
      unpopulated.remove(vertex);
    }

    // add the id to the current result
    currRes.add(vertex);
    return currRes;
  }

  public static void main(String[] args) {
    CapGraph a = new CapGraph();
    GraphLoader.loadGraph(a, "data/scc/test_" + 4 + ".txt");
    //    a.getSCCs().stream().map(Graph::exportGraph).forEach(System.out::println);
    //    a.getPotentialFriends(3).entrySet().forEach(System.out::println);
    a.getMinToAnnounce(Mode.GREEDY_DFS_DYNA).forEach(System.out::println);
  }
}

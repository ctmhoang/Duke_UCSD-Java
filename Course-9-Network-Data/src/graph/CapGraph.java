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
    tmp.forEach((k, svs) -> svs.forEach(sv -> res.putIfAbsent(k, sv.collect(Collectors.toSet()))));
    res.forEach((k, v) -> v.parallelStream().forEach(val -> res.get(val).add(k)));
    return res;
  }

  @Override
  public HashSet<Integer> getMinToAnnounce(Mode mode) {
    if (mode == Mode.GREEDY) return getMinByGreedy();
    return null;
  }

  private HashSet<Integer> getMinByGreedy() {
    HashSet<Integer> res = new HashSet<>();
    Set<Integer> discovered = new HashSet<>();
    Map<Integer, Set<Integer>> uncovered = new HashMap<>();
    System.out.println(vertices);
    for (int ver : vertices.keySet()) {
      if (uncovered.containsKey(ver)) continue;
      discovered.add(ver);
      res.addAll(DFSPopulateUncovered(ver, discovered, uncovered));
    }
      System.out.println(uncovered);
//    while (!uncovered.isEmpty()) {
//      int vertex = getMostUncoveredVertex(uncovered);
//      System.out.println(vertex);
//
//      uncovered.remove(vertex);
//
//      uncovered = updateUncoveredNode(res, uncovered);
//      System.out.println(uncovered);
//    }
//    return res;
    return null;
  }

  private Map<Integer, Set<Integer>> updateUncoveredNode(
      Set<Integer> coveredMode, Map<Integer, Set<Integer>> currentNodes) {
    return currentNodes.entrySet().stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                entry ->
                    entry.getValue().stream()
                        .filter(ver -> !coveredMode.contains(ver))
                        .collect(Collectors.toSet())))
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue().size() > 0)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private int getMostUncoveredVertex(Map<Integer, Set<Integer>> data) {
    Map<Integer,Integer> tmp = new HashMap<>();
    data.keySet().forEach(key -> tmp.put(key,0));
    data.values().forEach(set -> set.forEach(val -> tmp.put(val,tmp.get(val) + 1)));
    return Collections.max(
            tmp.entrySet(),
            Comparator.comparingInt(
                    Map.Entry::getValue))
        .getKey();
  }

  private Stack<Integer> DFSPopulateUncovered(int vertex, Set<Integer> discovered, Map<Integer, Set<Integer>> populatedData) {
    Set<Integer> connected = vertices.get(vertex);
    Stack<Integer> currRes = new Stack<>();
    for (int ver : connected) {
      if (!discovered.contains(ver)) {
        discovered.add(ver);
        currRes.addAll(DFSPopulateUncovered(ver, discovered, populatedData));
      }
    }
    populatedData.put(vertex, new HashSet<>(currRes));
    currRes.push(vertex);
    return currRes;
  }

  public static void main(String[] args) {
    CapGraph a = new CapGraph();
    GraphLoader.loadGraph(a, "data/scc/test_" + 4 + ".txt");
    //    a.getSCCs().stream().map(Graph::exportGraph).forEach(System.out::println);
    //    a.getPotentialFriends(3).entrySet().forEach(System.out::println);
    a.getMinToAnnounce(Mode.GREEDY).forEach(System.out::println);
  }
}

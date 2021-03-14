/** */
package graph;

import util.GraphLoader;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
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
                            data.subList(data.get(v) + 1, data.size()).stream()
                                .filter(i -> !vertices.get(v).contains(i)),
                        Collectors.toSet())));
    Map<Integer, Set<Integer>> res = new HashMap<>();
    tmp.forEach((k, svs) -> svs.forEach(sv -> res.putIfAbsent(k, sv.collect(Collectors.toSet()))));
    return res;
  }

  public static void main(String[] args) {
    Graph a = new CapGraph();
    GraphLoader.loadGraph(a, "data/scc/test_" + 4 + ".txt");
    a.getSCCs().stream().map(Graph::exportGraph).forEach(System.out::println);
  }
}

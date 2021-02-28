/** */
package graph;

import java.util.*;

/**
 * @author Your name here.
 *     <p>For the warm up assignment, you must implement your Graph in a class named CapGraph. Here
 *     is the stub file.
 */
public class CapGraph implements Graph {

  public final Map<Integer, Set<Integer>> vertices;

  public CapGraph() {
    this.vertices = new HashMap<>();
  }

  /* (non-Javadoc)
   * @see graph.Graph#addVertex(int)
   */
  @Override
  public void addVertex(int num) {
    vertices.put(num, new HashSet<>());
  }

  /* (non-Javadoc)
   * @see graph.Graph#addEdge(int, int)
   */
  @Override
  public void addEdge(int from, int to) {
    // TODO Auto-generated method stub
    if (vertices.containsKey(from)) throw new IllegalArgumentException("Vertex do not exist");
    vertices.get(from).add(to);
  }

  /* (non-Javadoc)
   * @see graph.Graph#getEgonet(int)
   */
  @Override
  public Graph getEgonet(int center) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see graph.Graph#getSCCs()
   */
  @Override
  public List<Graph> getSCCs() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see graph.Graph#exportGraph()
   */
  @Override
  public HashMap<Integer, HashSet<Integer>> exportGraph() {
    // TODO Auto-generated method stub
    return null;
  }
}

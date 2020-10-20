/**
 * @author UCSD MOOC development team and YOU
 *     <p>A class which reprsents a graph of geographic locations Nodes in the graph are
 *     intersections between
 */
package roadgraph;

import geography.GeographicPoint;
import util.GraphLoader;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author UCSD MOOC development team and YOU
 *     <p>A class which represents a graph of geographic locations Nodes in the graph are
 *     intersections between
 */
public class MapGraph {
  // Done: Add your member variables here in WEEK 3
  private final Map<GeographicPoint, VertexInfos> vertices;
  private int numOfEdges;

  private Set<List<GeographicPoint>> commonPath;

  /** Create a new empty MapGraph */
  public MapGraph() {
    // Done: Implement in this constructor in WEEK 3
    vertices = new HashMap<>();
    numOfEdges = 0;

    commonPath = new HashSet<>();
  }

  /**
   * Get the number of vertices (road intersections) in the graph
   *
   * @return The number of vertices in the graph.
   */
  public int getNumVertices() {
    // Done: Implement this method in WEEK 3
    return vertices.size();
  }

  /**
   * Return the intersections, which are the vertices in this graph.
   *
   * @return The vertices in this graph as GeographicPoints
   */
  public Set<GeographicPoint> getVertices() {
    // Done: Implement this method in WEEK 3
    return vertices.keySet();
  }

  /**
   * Get the number of road segments in the graph
   *
   * @return The number of edges in the graph.
   */
  public int getNumEdges() {
    // Done: Implement this method in WEEK 3
    return numOfEdges;
  }

  /**
   * Add a node corresponding to an intersection at a Geographic Point If the location is already in
   * the graph or null, this method does not change the graph.
   *
   * @param location The location of the intersection
   * @return true if a node was added, false if it was not (the node was already in the graph, or
   *     the parameter is null).
   */
  public boolean addVertex(GeographicPoint location) {
    // Done: Implement this method in WEEK 3
    if (vertices.containsKey(location) || location == null) return false;
    vertices.put(location, new VertexInfos());
    return true;
  }

  /**
   * Adds a directed edge to the graph from pt1 to pt2. Precondition: Both GeographicPoints have
   * already been added to the graph
   *
   * @param from The starting point of the edge
   * @param to The ending point of the edge
   * @param roadName The name of the road
   * @param roadType The type of the road
   * @param length The length of the road, in km
   * @throws IllegalArgumentException If the points have not already been added as nodes to the
   *     graph, if any of the arguments is null, or if the length is less than 0.
   */
  public void addEdge(
      GeographicPoint from, GeographicPoint to, String roadName, String roadType, double length)
      throws IllegalArgumentException {
    // Done: Implement this method in WEEK 3
    if (length < 0
        || from == null
        || to == null
        || roadName == null
        || roadType == null
        || !(vertices.containsKey(from) && vertices.containsKey(to)))
      throw new IllegalArgumentException();
    vertices.get(from).addEdge(new Edge(to, roadName, roadType, length));
    numOfEdges++;
  }

  /**
   * Find the path from start to goal using breadth first search
   *
   * @param start The starting location
   * @param goal The goal location
   * @return The list of intersections that form the shortest (unweighted) path from start to goal
   *     (including both start and goal).
   */
  public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
    // Dummy variable for calling the search algorithms
    Consumer<GeographicPoint> temp = (x) -> {};
    return bfs(start, goal, temp);
  }

  /**
   * Find the path from start to goal using breadth first search
   *
   * @param start The starting location
   * @param goal The goal location
   * @param nodeSearched A hook for visualization. See assignment instructions for how to use it.
   * @return The list of intersections that form the shortest (unweighted) path from start to goal
   *     (including both start and goal).
   */
  public List<GeographicPoint> bfs(
      GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
    // Done: Implement this method in WEEK 3
    // Check input
    if (start == null || goal == null) {
      return null;
    }
    Map<GeographicPoint, GeographicPoint> parentMap = new HashMap<>();

    boolean found = bfsHelper(start, goal, nodeSearched, parentMap);

    if (!found) return null;

    return reconstructPath(start, goal, parentMap);
  }

  /**
   * Do BFS and populate parent map for construct path
   *
   * @param start start node
   * @param goal end node
   * @param nodeSearched Consumer func notify GUI when a node is found
   * @param parentMap map of vertex with the other vertex which discovered it
   * @return true if we can find a path from start to end false otherwise
   */
  private boolean bfsHelper(
      GeographicPoint start,
      GeographicPoint goal,
      Consumer<GeographicPoint> nodeSearched,
      Map<GeographicPoint, GeographicPoint> parentMap) {
    // init
    Set<GeographicPoint> discovered = new HashSet<>(Collections.singletonList(start));
    Queue<GeographicPoint> vertexQueue = new ArrayDeque<>();

    // add start node
    vertexQueue.add(start);

    // do bfs
    while (!vertexQueue.isEmpty()) {
      GeographicPoint currPoint = vertexQueue.poll();

      // Hook for visualization.
      nodeSearched.accept(currPoint);

      if (goal.equals(currPoint)) {
        return true;
      }
      // get all unvisited neighbor which do not appear in discovered yet
      List<GeographicPoint> unvisitedNeighbor =
          vertices.get(currPoint).getNeighbors().stream()
              .map(Edge::getAdjVertex)
              .filter(coor -> !discovered.contains(coor))
              .collect(Collectors.toList());
      vertexQueue.addAll(unvisitedNeighbor);
      discovered.addAll(unvisitedNeighbor);
      // add next node as key and curr node as val in parent map
      unvisitedNeighbor.parallelStream().forEach(coor -> parentMap.put(coor, currPoint));
    }
    return false;
  }

  /**
   * Reconstruct path from parentMap
   *
   * @param start start node
   * @param goal end node
   * @param parentMap map of vertex with the other vertex which discovered it
   * @return List of vertices in order which we can travel from start node to end node
   */
  private List<GeographicPoint> reconstructPath(
      GeographicPoint start,
      GeographicPoint goal,
      Map<GeographicPoint, GeographicPoint> parentMap) {
    GeographicPoint traceback = goal;
    LinkedList<GeographicPoint> res = new LinkedList<>();

    while (traceback != start) {
      res.addFirst(traceback);
      traceback = parentMap.get(traceback);
    }
    res.addFirst(traceback);
    return res;
  }

  /**
   * Find the path from start to goal using Dijkstra's algorithm
   *
   * @param start The starting location
   * @param goal The goal location
   * @return The list of intersections that form the shortest path from start to goal (including
   *     both start and goal).
   */
  public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
    // Dummy variable for calling the search algorithms
    // You do not need to change this method.
    Consumer<GeographicPoint> temp = (x) -> {};
    return dijkstra(start, goal, temp);
  }

  /**
   * Find the path from start to goal using Dijkstra's algorithm
   *
   * @param start The starting location
   * @param goal The goal location
   * @param nodeSearched A hook for visualization. See assignment instructions for how to use it.
   * @return The list of intersections that form the shortest path from start to goal (including
   *     both start and goal).
   */
  public List<GeographicPoint> dijkstra(
      GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
    // DONE: Implement this method in WEEK 4
    if (start == null || goal == null) {
      return null;
    }

    Map<GeographicPoint, GeographicPoint> parentMap = new HashMap<>();

    boolean found = dijkstraHelper(start, goal, nodeSearched, parentMap);
    if (found) return reconstructPath(start, goal, parentMap);
    return null;
  }

  private boolean dijkstraHelper(
      GeographicPoint start,
      GeographicPoint goal,
      Consumer<GeographicPoint> nodeSearched,
      Map<GeographicPoint, GeographicPoint> parentMap) {
    Map<GeographicPoint, Double> distance = new HashMap<>();
    Set<GeographicPoint> discovered = new HashSet<>();
    PriorityQueue<GeographicPoint> pointQueue =
        new PriorityQueue<>(Comparator.comparingDouble(distance::get));

    distance.put(start, (double) 0);
    pointQueue.add(start);
    while (!pointQueue.isEmpty()) {
      GeographicPoint currPoint = pointQueue.poll();
      if (!discovered.contains(currPoint)) {
        discovered.add(currPoint);

        // Hook for visualization.  See writeup.
        nodeSearched.accept(currPoint);

        if (goal.equals(currPoint)) {
          return true;
        }

        double currDist = distance.get(currPoint);

        List<GeographicPoint> adjacentPoints =
            vertices.get(currPoint).getNeighbors().stream()
                .map(Edge::getAdjVertex)
                .collect(Collectors.toList());
        adjacentPoints.stream()
            .filter(vertex -> !parentMap.containsKey(vertex))
            .forEach(child -> parentMap.put(child, currPoint));
        adjacentPoints.forEach(
            point ->
                distance.merge(
                    point,
                    currDist + point.distance(currPoint),
                    (old, newOne) -> {
                      if (old > newOne) {
                        parentMap.replace(point, currPoint);
                        return newOne;
                      }
                      return old;
                    }));

        pointQueue.removeAll(adjacentPoints);
        pointQueue.addAll(adjacentPoints);
      }
    }
    return false;
  }

  /**
   * Find the path from start to goal using A-Star search
   *
   * @param start The starting location
   * @param goal The goal location
   * @return The list of intersections that form the shortest path from start to goal (including
   *     both start and goal).
   */
  public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
    // Dummy variable for calling the search algorithms
    Consumer<GeographicPoint> temp = (x) -> {};
    return aStarSearch(start, goal, temp);
  }

  /**
   * Find the path from start to goal using A-Star search
   *
   * @param start The starting location
   * @param goal The goal location
   * @param nodeSearched A hook for visualization. See assignment instructions for how to use it.
   * @return The list of intersections that form the shortest path from start to goal (including
   *     both start and goal).
   */
  public List<GeographicPoint> aStarSearch(
      GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
    // Done: Implement this method in WEEK 4
    if (start == null || goal == null) {
      return null;
    }

    Map<GeographicPoint, GeographicPoint> parentMap = new HashMap<>();
    boolean found = aStarHelper(start, goal, parentMap, nodeSearched);

    if (found) return reconstructPath(start, goal, parentMap);

    return null;
  }

  private boolean aStarHelper(
      GeographicPoint start,
      GeographicPoint goal,
      Map<GeographicPoint, GeographicPoint> parentMap,
      Consumer<GeographicPoint> nodeSearched) {
    Set<GeographicPoint> discovered = new HashSet<>();
    Map<GeographicPoint, Double> predictedDistance = new HashMap<>();
    Queue<GeographicPoint> pointQueue =
        new PriorityQueue<>(Comparator.comparingDouble(predictedDistance::get));
    predictedDistance.put(start, start.distance(goal));
    pointQueue.add(start);
    while (!pointQueue.isEmpty()) {
      GeographicPoint currPoint = pointQueue.poll();
      if (!discovered.contains(currPoint)) {
        discovered.add(currPoint);

        // Hook for visualization.  See writeup.
        nodeSearched.accept(currPoint);

        if (currPoint.equals(goal)) {
          return true;
        }
        double actualDistance = predictedDistance.get(currPoint) - currPoint.distance(goal);
        List<GeographicPoint> adjacentNodes =
            vertices.get(currPoint).getNeighbors().stream()
                .map(Edge::getAdjVertex)
                .collect(Collectors.toList());
        adjacentNodes.stream()
            .filter(node -> !parentMap.containsKey(node))
            .forEach(node -> parentMap.put(node, currPoint));
        adjacentNodes.forEach(
            node ->
                predictedDistance.merge(
                    node,
                    actualDistance + node.distance(currPoint) + node.distance(goal),
                    (old, newOne) -> {
                      if (old > newOne) {
                        parentMap.replace(node, currPoint);
                        return newOne;
                      }
                      return old;
                    }));
        pointQueue.removeAll(adjacentNodes);
        pointQueue.addAll(adjacentNodes);
      }
    }
    return false;
  }

  public List<GeographicPoint> findPath(GeographicPoint start, GeographicPoint goal) {
    final double MIN_DISTANCE = goal.distance(start);
    List<List<GeographicPoint>> capPath =
        commonPath.stream()
            .filter(
                path -> path.stream().noneMatch(vertex -> start.distance(vertex) > MIN_DISTANCE))
            .collect(Collectors.toList());
    if (capPath.size() > 0) {
      System.out.println("OLD PATH");
      List<GeographicPoint> bestPathFoundSoFar =
          Collections.min(
              capPath,
              Comparator.comparingDouble(
                  path ->
                      path.stream()
                          .mapToDouble(start::distance)
                          .boxed()
                          .min(Double::compareTo)
                          .orElseThrow(RuntimeException::new)));
      GeographicPoint closestNode =
          Collections.min(bestPathFoundSoFar, Comparator.comparingDouble(start::distance));

      commonPath.remove(bestPathFoundSoFar);

      bestPathFoundSoFar =
          bestPathFoundSoFar.subList(
              bestPathFoundSoFar.indexOf(closestNode), bestPathFoundSoFar.size());



      commonPath.add(bestPathFoundSoFar);
      if (start.equals(closestNode)) {
        return bestPathFoundSoFar;
      }
      List<GeographicPoint> res = aStarSearch(start, closestNode);
      res.subList(0, res.size() - 1).addAll(bestPathFoundSoFar);
      return res;
    }
    System.out.println("NEW PATH");
    List<GeographicPoint> res = aStarSearch(start, goal);
    commonPath.add(res);
    return res;
  }

  public static void main(String[] args) {
    //    MapGraph theMap = new MapGraph();
    //    System.out.print("DONE. \nLoading the map...");
    //    GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
    //    System.out.println("DONE.");
    //
    //    GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
    //    GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
    //
    //    List<GeographicPoint> route = theMap.dijkstra(start, end, System.out::println);
    //    System.out.println();
    //    List<GeographicPoint> route2 = theMap.aStarSearch(start, end, System.out::println);

    System.out.print("Making a new map...");
    MapGraph firstMap = new MapGraph();
    System.out.print("DONE. \nLoading the map...");
    GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
    System.out.println("DONE.");

    GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
    GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
    System.out.println(firstMap.findPath(testStart, testEnd));
    GeographicPoint str1 = new GeographicPoint(4.0, 1.0);
    System.out.println(firstMap.findPath(str1, testEnd));
    GeographicPoint str2 = new GeographicPoint(7.0, 3.0);
    System.out.println(testEnd.distance(str2));
    System.out.println(str2.distance(str1));
    System.out.println(firstMap.findPath(str2, testEnd));
    System.out.println(firstMap.findPath(str2, testEnd));
    System.out.println(firstMap.commonPath);

    //    System.out.println(firstMap.numOfEdges);
    //    System.out.println(firstMap.vertices.size());
    //    System.out.println(firstMap.bfs(testStart, testEnd));
    //    System.out.println((firstMap.dijkstra(testStart, testEnd, System.out::println)));

    // Week 4 Node count test
    //    MapGraph simpleTestMap = new MapGraph();
    //    GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
    //
    //
    //    GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
    //    GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
    //
    //    System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
    //    List<GeographicPoint> testroute =
    // simpleTestMap.dijkstra(testStart,testEnd,System.out::println);
    //    System.out.println();
    //    List<GeographicPoint> testroute2 =
    // simpleTestMap.aStarSearch(testStart,testEnd,System.out::println);
    //    System.out.println();
    //    System.out.println();
    //    System.out.println();
    //
    //    MapGraph testMap = new MapGraph();
    //    GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
    //
    //    // A very simple test using real data
    //    testStart = new GeographicPoint(32.869423, -117.220917);
    //    testEnd = new GeographicPoint(32.869255, -117.216927);
    //    System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
    //    testroute = testMap.dijkstra(testStart,testEnd,System.out::println);
    //    System.out.println();
    //    testroute2 = testMap.aStarSearch(testStart,testEnd,System.out::println);
    //    System.out.println();
    //    System.out.println();
    //    System.out.println();
    //
    //    // A slightly more complex test using real data
    //    testStart = new GeographicPoint(32.8674388, -117.2190213);
    //    testEnd = new GeographicPoint(32.8697828, -117.2244506);
    //    System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
    //    testroute = testMap.dijkstra(testStart,testEnd,System.out::println);
    //    System.out.println();
    //    testroute2 = testMap.aStarSearch(testStart,testEnd,System.out::println);
    // End Test week 4
    // You can use this method for testing.

    /* Here are some test cases you should try before you attempt
     * the Week 3 End of Week Quiz, EVEN IF you score 100% on the
     * programming assignment.
     */
    /*
    MapGraph simpleTestMap = new MapGraph();
    GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);

    GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
    GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);

    System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
    List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
    List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);


    MapGraph testMap = new MapGraph();
    GraphLoader.loadRoadMap("data/maps/utc.map", testMap);

    // A very simple test using real data
    testStart = new GeographicPoint(32.869423, -117.220917);
    testEnd = new GeographicPoint(32.869255, -117.216927);
    System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
    testroute = testMap.dijkstra(testStart,testEnd);
    testroute2 = testMap.aStarSearch(testStart,testEnd);


    // A slightly more complex test using real data
    testStart = new GeographicPoint(32.8674388, -117.2190213);
    testEnd = new GeographicPoint(32.8697828, -117.2244506);
    System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
    testroute = testMap.dijkstra(testStart,testEnd);
    testroute2 = testMap.aStarSearch(testStart,testEnd);
    */

    /* Use this code in Week 3 End of Week Quiz */
    /*MapGraph theMap = new MapGraph();
    System.out.print("DONE. \nLoading the map...");
    GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
    System.out.println("DONE.");

    GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
    GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);


    List<GeographicPoint> route = theMap.dijkstra(start,end);
    List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

    */

  }
}

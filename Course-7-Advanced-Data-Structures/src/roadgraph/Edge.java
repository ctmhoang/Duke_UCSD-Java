package roadgraph;

import geography.GeographicPoint;

import java.util.Objects;

public class Edge {
  private final GeographicPoint adjVertexLoc;
  private final String stName;
  private final String stType;
  private final double dst;

  public Edge(GeographicPoint adjVertexLoc, String stName, String stType, double dst) {
    this.adjVertexLoc = adjVertexLoc;
    this.stName = stName;
    this.stType = stType;
    this.dst = dst;
  }

  public GeographicPoint getAdjVertex() {
    return adjVertexLoc;
  }

  public String getStName() {
    return stName;
  }

  public String getStType() {
    return stType;
  }

  public double getDst() {
    return dst;
  }

  @Override
  public int hashCode() {
    return Objects.hash(stName, dst, adjVertexLoc);
  }
}

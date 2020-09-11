package roadgraph;


import java.util.HashSet;
import java.util.Set;

public class VertexInfos
{
    private final Set<Edge> neighbors;

    public VertexInfos()
    {
        neighbors = new HashSet<>();
    }

    public void addEdge(Edge edge)
    {
        neighbors.add(edge);
    }


    public Set<Edge> getNeighbors()
    {
        return neighbors;
    }
}

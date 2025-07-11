import java.util.*;

public class Dijkstra1 {

  static class Vertex {
    int id;
    double distance;
    PriorityQueueNode locator; // Locator in the priority queue

    public Vertex(int id) {
      this.id = id;
      this.distance = Double.POSITIVE_INFINITY;
    }
  }

  static class Edge {
    Vertex from, to;
    double distance;

    public Edge(Vertex from, Vertex to, double distance) {
      this.from = from;
      this.to = to;
      this.distance = distance;
    }

    public Vertex opposite(Vertex vertex) {
      return (vertex == from) ? to : from;
    }
  }

  static class PriorityQueueNode {
    Vertex vertex;
    double distance;

    public PriorityQueueNode(Vertex vertex, double distance) {
      this.vertex = vertex;
      this.distance = distance;
    }
  }

  static class LocatorPriorityQueue {
    private final Map<Vertex, PriorityQueueNode> locators = new HashMap<>();
    private final PriorityQueue<PriorityQueueNode> queue = new PriorityQueue<>(Comparator.comparingDouble(n -> n.distance));

    public void insert(Vertex v, double key) {
      PriorityQueueNode node = new PriorityQueueNode(v, key);
      queue.add(node);
      locators.put(v, node);
      v.locator = node;
    }

    public boolean isEmpty() {
      return queue.isEmpty();
    }

    public Vertex removeMin() {
      PriorityQueueNode node = queue.poll();
      locators.remove(node.vertex);
      return node.vertex;
    }

    public void replaceKey(Vertex v, double newKey) {
      PriorityQueueNode oldNode = locators.remove(v);
      queue.remove(oldNode);
      insert(v, newKey);
    }
  }

  public static void dijkstraDistances(List<Vertex> vertices, Map<Vertex, List<Edge>> adjList, Vertex source) {
    LocatorPriorityQueue pQueue = new LocatorPriorityQueue();

    for (Vertex vertex : vertices) {
      if (vertex == source) {
        vertex.distance = 0;
      } else {
        vertex.distance = Double.POSITIVE_INFINITY;
      }
      pQueue.insert(vertex, vertex.distance);
    }

    while (!pQueue.isEmpty()) {
      Vertex u = pQueue.removeMin();

      for (Edge e : adjList.get(u)) {
        Vertex z = e.opposite(u);
        double r = u.distance + e.distance;

        if (r < z.distance) {
          z.distance = r;
          pQueue.replaceKey(z, r);
        }
      }
    }
  }
}

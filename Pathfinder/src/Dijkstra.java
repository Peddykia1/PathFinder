import java.util.*;

public class Dijkstra extends Observable {
    private PriorityQueue<Node> unvisited;
    private PriorityQueue<Node> visited;

    private Node start;
    private Node endNode;
    private static final double DEFAULT_DISTANCE = Double.MAX_VALUE;
    private Grid  grid;
    public boolean hasSolution;
    private List<Node> path;
    public static final String UNVISITED = "unvisited";
    public static final String VISITED = "visited";



    public Dijkstra(Node start, Node endNode,  int row, int col) {
        path = new LinkedList<>();
        hasSolution = false;
        unvisited = new PriorityQueue<>();
        visited = new PriorityQueue<>();
        this.start = start;
        start.f = 0;
        grid = new Grid(row, col);
        this.endNode = endNode;
        unvisited.add(start);
        for (int i = 0 ; i < grid.getCols() ; i++) {
            for (int j = 0 ; j < grid.getRows() ; j++) {
                Node node = grid.getNode(i, j);
                if (node != start) {
                    node.f = DEFAULT_DISTANCE;
                }
            }
        }
    }

    public Node findEndNode() {
        while (!unvisited.isEmpty()) {
            Node curr = unvisited.poll();
            visited.add(curr);
            setChanged();
            notifyObservers(VISITED);
            if (curr.equals(endNode)) {
                hasSolution = true;
                return curr;
            }
            List<Node> children = findChildren(curr);
            for (Node neighbor : children) {
                if (neighbor != null) {
                    double tempf;

                    if (Math.abs(curr.getX() - neighbor.getX()) == 1 && Math.abs(curr.getY() - neighbor.getY()) == 1) {
                        tempf = curr.f + 1.4;
                    }
                    else {
                        tempf = curr.f + 1;
                    }

                    if (!visited.contains(neighbor)) {
                        if (tempf < neighbor.f) {
                            neighbor.f = tempf;
                            neighbor.parent = curr;
                            unvisited.add(neighbor);
                            setChanged();
                            notifyObservers(UNVISITED);
                        }
                    }
                }
            }
        }
        hasSolution = false;
        return null;
    }


    public List<Node> findPath(Node end) {
        //since each node only has ONE parent, we can simply traverse back to the starting point
        if (end == null) {
            return path;
        }
        path.add(end);
        Node parent = end.parent;
        findPath(parent);
        return path;
    }


    public Node getEndNode() {
        Node ans = null;
        for (Node node : visited) {
            if (node.equals(endNode)) {
                ans =  node;
            }
        }
        return ans;
    }


    public PriorityQueue<Node> getUnvisited() {
        return unvisited;
    }

    public PriorityQueue<Node> getVisited() {
        return visited;
    }


    public List<Node> findChildren(Node node) {
        ArrayList<Node> children = new ArrayList<>();
        int rows = grid.getRows();
        int cols = grid.getCols();
        int x = node.getX();
        int y = node.getY();

        if (1 <= node.getX() && node.getX() <= rows - 2 && 1 <= node.getY() && node.getY() <= cols - 2) {
            children.add(addChildren(x - 1, y - 1));
            children.add(addChildren(x - 1, y));
            children.add(addChildren(x - 1, y + 1));
            children.add(addChildren(x, y - 1));
            children.add(addChildren(x, y + 1));
            children.add(addChildren(x + 1, y - 1));
            children.add(addChildren(x + 1, y));
            children.add(addChildren(x + 1, y + 1));
        } else if (node.getX() == 0 && node.getY() == 0) {
            children.add(addChildren(0, 1));
            children.add(addChildren(1, 1));
            children.add(addChildren(1, 0));
        } else if (node.getX() == 0 && node.getY() == cols - 1) {
            children.add(addChildren(0, cols - 2));
            children.add(addChildren(1, cols - 2));
            children.add(addChildren(1, cols - 1));
        } else if (node.getX() == rows - 1 && node.getY() == 0) {
            children.add(addChildren(rows - 2, 0));
            children.add(addChildren(rows - 2, 1));
            children.add(addChildren(rows - 1, 1));
        } else if (node.getX() == rows - 1 && node.getY() == cols - 1) {
            children.add(addChildren(rows - 2, cols - 1));
            children.add(addChildren(rows - 2, cols - 2));
            children.add(addChildren(rows - 1, cols - 2));
        } else if (node.getX() == 0) {
            children.add(addChildren(0, y - 1));
            children.add(addChildren(0, y + 1));
            children.add(addChildren(1, y - 1));
            children.add(addChildren(1, y));
            children.add(addChildren(1, y + 1));
        } else if (node.getX() == rows - 1) {
            children.add(addChildren(rows - 1, y - 1));
            children.add(addChildren(rows - 1, y + 1));
            children.add(addChildren(rows - 2, y - 1));
            children.add(addChildren(rows - 2, y));
            children.add(addChildren(rows - 2, y - 1));
        } else if (node.getY() == 0) {
            children.add(addChildren(x - 1, 0));
            children.add(addChildren(x + 1, 0));
            children.add(addChildren(x - 1, 1));
            children.add(addChildren(x, 1));
            children.add(addChildren(x + 1, 1));
        } else if (node.getY() == cols - 1) {
            children.add(addChildren(x - 1, cols - 1));
            children.add(addChildren(x + 1, cols - 1));
            children.add(addChildren(x - 1, cols - 2));
            children.add(addChildren(x, cols - 2));
            children.add(addChildren(x - 1, cols - 2));
        }

        return children;
    }

    private Node addChildren(int x, int y) {
        if (grid.isValidNode(x, y)) {
            return grid.getNode(x, y);
        } else{
            return null;
        }
    }


    public Grid getGrid() {
        return grid;
    }
}
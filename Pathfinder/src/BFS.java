import java.util.*;

public class BFS extends Observable {
    private Queue<Node> unvisited;
    private Node start;
    private Node endNode;
    private static final double DEFAULT_DISTANCE = Double.MAX_VALUE;
    private Grid  grid;
    public boolean hasSolution;
    private List<Node> path;


    public BFS(Node start, Node endNode, int row, int col) {
        path = new LinkedList<>();
        hasSolution = false;
        unvisited = new PriorityQueue<>();
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

    public Node BFS() {
        int i = 0;
        while (!unvisited.isEmpty()) {
            Node curr = unvisited.poll();
            notifyObservers("uv");
            if (curr.getY() == endNode.getY() && curr.getX() == endNode.getX()) {
                hasSolution = true;
                return curr;
            }
            List<Node> children = findChildren(curr);
            for (Node neighbor : children) {
                if (neighbor != null) {
                    i++;
                    if (neighbor.f == DEFAULT_DISTANCE) {
                        neighbor.f = i;
                        neighbor.parent = curr;
                        unvisited.add(neighbor);
                        notifyObservers("uv");
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



    public Queue<Node> getBFSunvisited() {
        return unvisited;
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
            children.add(addChildren(1, 0));
            children.add(addChildren(1, 1));
        } else if (node.getX() == 0 && node.getY() == cols - 1) {
            children.add(addChildren(0, cols - 2));
            children.add(addChildren(1, cols - 1));
            children.add(addChildren(1, cols - 2));
        } else if (node.getX() == rows - 1 && node.getY() == 0) {
            children.add(addChildren(rows - 2, 0));
            children.add(addChildren(rows - 1, 1));
            children.add(addChildren(rows - 2, 1));
        } else if (node.getX() == rows - 1 && node.getY() == cols - 1) {
            children.add(addChildren(rows - 2, cols - 1));
            children.add(addChildren(rows - 1, cols - 2));
            children.add(addChildren(rows - 2, cols - 2));
        } else if (node.getX() == 0) {
            children.add(addChildren(0, y - 1));
            children.add(addChildren(0, y + 1));
            children.add(addChildren(1, y));
            children.add(addChildren(1, y - 1 ));
            children.add(addChildren(1, y + 1));
        } else if (node.getX() == rows - 1) {
            children.add(addChildren(rows - 1, y - 1));
            children.add(addChildren(rows - 1, y + 1));
            children.add(addChildren(rows - 2, y));
            children.add(addChildren(rows - 2, y + 1));
            children.add(addChildren(rows - 2, y - 1));
        } else if (node.getY() == 0) {
            children.add(addChildren(x - 1, 0));
            children.add(addChildren(x + 1, 0));
            children.add(addChildren(x, 1));
            children.add(addChildren(x - 1 , 1));
            children.add(addChildren(x + 1, 1));
        } else if (node.getY() == cols - 1) {
            children.add(addChildren(x - 1, cols - 1));
            children.add(addChildren(x + 1, cols - 1));
            children.add(addChildren(x, cols - 2));
            children.add(addChildren(x - 1, cols - 2));
            children.add(addChildren(x + 1, cols - 2));
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

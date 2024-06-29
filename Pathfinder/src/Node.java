import java.util.Objects;

public class Node implements Comparable<Node>{

    //g(x) --> absolute distance moving from one node to another
    //f(x) --> full estimation of cost
    //h(x) --> from n to target end point

    public Node parent;
    public double f;
    public double g;
    public double h;
    private Boolean isNode; //identifies a node as either a node or block
    private final int x;
    private final int y;
    private boolean isDoor;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        isDoor = false;
        h = 0;
        g = 0;
        f = 0;
        parent = null;
        isNode = true;
    }


    @Override
    public int compareTo(Node other) {
        if (this.f > other.f) {
            return 1; //1 means gets added to the end
        } else {
            return this.f < other.f ? - 1: 0;  // -1 means add it to the front
        }
    }

    public double getF() { //getF for comparisons
        return f;
    }

    public double getG() { //getG to calculate F
        return g;
    }

    public double getH() { //getH to calculate F
        return h;
    }


    public int getX() { //get coordinates if its needed
        return x;
    }

    public int getY() {
        return y;
    }


    //setters F,G,H are constantly changing and isNode changes based on input
    public double calcF() {
        this.f = g + h;
        return f;
    }

    public double calcG() {
        this.g = f - h;
        return g;
    }

    public double calcH(Node b) { //node b is the destination, node a is current node
        this.h = Math.sqrt(Math.abs((x-b.getX())*(x-b.getX())+(y-b.getY())*(y-b.getY())));
        return this.h;
    }

    //
    public void makeBlock() {
        isNode = false;
        isDoor = false;

    }
    public void flipNode() {
        isNode = !isNode;
    }

    public void makePath() {
        isNode = true;
    }

    public boolean isNode(){
        return isNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public void makeDoor() {
        isDoor = true;
    }

    public boolean isDoor() {
        return isDoor;
    }
}



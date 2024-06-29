public class Grid {
    private final Node[][] grid;// 2 dimensional grid
    private final int rows;
    private final int cols;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new Node[rows][cols];
        for(int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                Node temp = new Node(x, y);
                grid[x][y] = temp;
            }
        }
    }

    public int getRows(){
        return this.rows;
    }
    public int getCols(){
        return this.cols;
    }

    public Node getNode(int row, int column){
        return grid[row][column];
    }

    public void makeBlock(int row, int column){
        grid[row][column].makeBlock();
    }

    public void flipNode(int row, int column){
        grid[row][column].flipNode();
    }

    public void makePath(int row, int column) {
        grid[row][column].makePath();
    }

    public boolean isNotDoor(int row, int column) {
        return !grid[row][column].isDoor();
    }

    public boolean isValidNode(int row, int column){
        return grid[row][column].isNode();
    }

}
public class main {

    public static void main(String[] args) {

        MazeGenerator mazeGenerator = new MazeGenerator(new Grid(5,5));
        mazeGenerator.makeMaze(0,0, 5,5);
        new GUI();


    }
}
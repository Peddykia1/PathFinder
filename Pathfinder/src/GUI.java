
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public class GUI extends JPanel implements MouseListener, KeyListener, ActionListener, MouseMotionListener, Observer{

    private JFrame frame;

    //algorithms:
    private Dijkstra dijkstraAlg;
    private BFS bfs;
    private MazeGenerator mazeGenerator;

    //starting node and ending node:
    private Node pathFinderStartNode;
    private Node pathFinderEndNode;
    private Node GUIstartNode;
    private Node GUIendNode;


    //Queues:
    //for DijkstraAlg
    private PriorityQueue<Node> unvisited;
    private PriorityQueue<Node> visited;

    //for BFS
    private Queue<Node> options;

    //GUI manipulations:
    private final static int gridDimention = 30;
    private Character keyRightNow;
    int isStartOn = 0;
    int isEndOn = 0;
    private List<Node> path;
    private Set<Node> blocks;
    private JComboBox selectMode;// drop-down menu of choices for the user

    String[] modes = {"Dijkstra", "BFS"};

    public GUI() {
        mazeGenerator = new MazeGenerator(new Grid(900 / gridDimention,900 / gridDimention));

        //Queues:
        unvisited = new PriorityQueue<>();
        visited = new PriorityQueue<>();

        options = new PriorityQueue<>();

        //nodes:
        pathFinderStartNode = null;
        pathFinderEndNode = null;
        path = null;
        blocks = new HashSet<>();

        //GUI:
        addMouseListener(this);
        addKeyListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        keyRightNow = (char) '0';
        GUIstartNode = null;
        GUIendNode = null;
        frame = new JFrame();
        frame.setContentPane(this);
        frame.getContentPane().setPreferredSize(new Dimension(900,900));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //how it would close
        frame.setTitle("PathFinder");
        frame.pack();
        frame.setVisible(true);
        revalidate();
        repaint();
    }

    {
        JFrame frame = new JFrame();
        frame.setTitle("Control Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton clearObstacles, clearEverything, findPath, makeMaze;

        JLabel mode;


        mode = new JLabel("mode: ");
        mode.setHorizontalAlignment(JLabel.CENTER);
        selectMode = new JComboBox<>();
        selectMode.addItem(modes[0]);
        selectMode.addItem(modes[1]);
        selectMode.setSelectedIndex(0);


        clearObstacles = new JButton("Clear Obstacles");
        clearObstacles.setSize(new Dimension(10, 40));
        clearObstacles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blocks = new HashSet<>();
                unvisited = new PriorityQueue<>();
                visited = new PriorityQueue<>();
                options = new PriorityQueue<>();
                path = null;
                repaint();
                JOptionPane.showMessageDialog(null, "Obstacles cleared!");
            }
        });


        makeMaze = new JButton("recursively make random maze");
        makeMaze.setSize(new Dimension(10,40));
        makeMaze.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Set<Node> blocks2 = mazeGenerator.makeMaze(0,0,900 / gridDimention,900 / gridDimention);

                for (Node block : blocks2) {
                    Node pathfindingBlock = new Node(block.getX() * gridDimention,block.getY() * gridDimention);;
                    blocks.add(pathfindingBlock);
                }
                repaint();
            }
        });



        clearEverything = new JButton("Clear Everything");
        clearEverything.setSize(new Dimension(10, 40));

        clearEverything.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blocks.clear();
                GUIendNode = null;
                GUIstartNode = null;
                pathFinderStartNode = null;
                pathFinderEndNode = null;

                isEndOn = 0;
                isStartOn = 0;
                path = null;

                unvisited = new PriorityQueue<>();
                visited = new PriorityQueue<>();

                options = new PriorityQueue<>();
                repaint();
                JOptionPane.showMessageDialog(null, "Everything cleared!");
            }
        });

        findPath = new JButton("Run");
        findPath.setSize(new Dimension(10, 40));

        findPath.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = (String) selectMode.getSelectedItem();
                if (selection.equals("Dijkstra")) {
                    if (pathFinderEndNode != null && pathFinderStartNode != null) {
                        dijkstraAlg = new Dijkstra(pathFinderStartNode, pathFinderEndNode, 900 / gridDimention, 900 / gridDimention);
                        dijkstraAlg.addObserver(GUI.this);
                        for (Node block : blocks) {
                            Node dijBlock = new Node(block.getX() / gridDimention == 90 ? 89 : block.getX() / gridDimention, block.getY() / gridDimention == 90 ? 89 : block.getY() / gridDimention
                            );
                            dijkstraAlg.getGrid().makeBlock(dijBlock.getX(), dijBlock.getY());
                        }
                        path = dijkstraAlg.findPath(dijkstraAlg.findEndNode());
                        if (!dijkstraAlg.hasSolution) {
                            JOptionPane.showMessageDialog(null, "there is no path to the end point");
                        }
                        repaint();
                    }

                }

                else if(selection.equals("BFS")){
                    if (pathFinderEndNode != null && pathFinderStartNode != null) {
                        bfs = new BFS(pathFinderStartNode, pathFinderEndNode, 900 / gridDimention, 900 / gridDimention);
                        bfs.addObserver(GUI.this);
                        for (Node block : blocks) {
                            Node bfsBlock = new Node(block.getX() / gridDimention == 90 ? 89 : block.getX() / gridDimention, block.getY() / gridDimention == 90 ? 89 : block.getY() / gridDimention
                            );
                            bfs.getGrid().makeBlock(bfsBlock.getX(), bfsBlock.getY());
                        }
                        path = bfs.findPath(bfs.BFS());
                        if (!bfs.hasSolution) {
                            JOptionPane.showMessageDialog(null, "there is no path to the end point");
                        }
                        repaint();
                    }

                }

            }
        });


        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(7,1));
        p2.add(clearObstacles);
        p2.add(clearEverything);
        p2.add(findPath);
        p2.add(mode);
        p2.add(makeMaze);
        p2.add(selectMode);

        frame.setLayout(new BorderLayout());
        frame.add(p2,BorderLayout.LINE_END);
        frame.pack();
        frame.setVisible(true);
    }




    @Override
    public void paintComponent(Graphics g) {

        g.setColor(Color.ORANGE);
        for (Node node : unvisited) {
            g.fillRect(node.getX() * gridDimention + 1, node.getY() * gridDimention + 1, gridDimention - 1, gridDimention - 1);
        }

        g.setColor(Color.yellow);
        for (Node node : visited) {
            g.fillRect(node.getX() * gridDimention + 1, node.getY() * gridDimention + 1, gridDimention - 1, gridDimention - 1);
        }


        g.setColor(Color.ORANGE);
        for (Node node : options) {
            g.fillRect(node.getX() * gridDimention + 1, node.getY() * gridDimention + 1, gridDimention - 1, gridDimention - 1);
        }

        g.setColor(Color.gray);
        for (int y = 0; y < this.getHeight(); y += gridDimention) { //cp
            for (int x = 0; x < this.getWidth(); x += gridDimention) {
                g.drawRect(y, x , gridDimention, gridDimention);
            }
        }

        if (GUIendNode != null) {
            g.setColor(Color.red);
            g.fillRect(GUIendNode.getX() + 1, GUIendNode.getY() + 1, gridDimention - 1, gridDimention - 1);
        }

        if (GUIstartNode != null) {
            g.setColor(Color.blue);
            g.fillRect(GUIstartNode.getX() + 1, GUIstartNode.getY() + 1, gridDimention - 1, gridDimention - 1);
        }



        if (path != null) {
            g.setColor(Color.CYAN);
            path.remove(pathFinderEndNode);
            path.remove(pathFinderStartNode);
            for (Node node : path) {
                g.fillRect(node.getX() * gridDimention + 1, node.getY() * gridDimention + 1, gridDimention - 1, gridDimention - 1);
            }
        }


        if (blocks != null) {
            g.setColor(Color.black);
            for (Node block : blocks) {
                g.fillRect(block.getX() + 1, block.getY() + 1, gridDimention - 1, gridDimention - 1);
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String selection = (String) selectMode.getSelectedItem();
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (keyRightNow == 'e' || keyRightNow == 'E') {
                isEndOn++;
                int xSub = e.getX() % gridDimention;
                int ySub = e.getY() % gridDimention;
                if (isEndOn % 2 == 1 && GUIendNode == null) {
                    GUIendNode = new Node(e.getX() - xSub, e.getY() - ySub);
                    pathFinderEndNode = new Node(GUIendNode.getX() / gridDimention, GUIendNode.getY() / gridDimention);
                } else {
                    GUIendNode = null;
                }
                repaint();
            } else if (keyRightNow == 's' || keyRightNow == 'S') {
                isStartOn++;
                int xSub = e.getX() % gridDimention;
                int ySub = e.getY() % gridDimention;
                if (isStartOn % 2 == 1 && GUIstartNode == null) {
                    GUIstartNode = new Node(e.getX() - xSub, e.getY() - ySub);
                    pathFinderStartNode = new Node(GUIstartNode.getX() / gridDimention, GUIstartNode.getY() / gridDimention);
                } else {
                    GUIstartNode = null;
                }
                repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }


    @Override
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        keyRightNow = key;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int xSub = e.getX() % gridDimention;
        int ySub = e.getY() % gridDimention;

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (keyRightNow == 'b') {
                Node block = new Node(e.getX() - xSub, e.getY() - ySub);
                blocks.add(block);
            }
            repaint();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        String selection = (String) selectMode.getSelectedItem();

        if (selection.equals("Dijkstra")){

            String operation = (String) arg;
            Dijkstra d = (Dijkstra) o;

            if (operation.equals(d.UNVISITED)) {
                PriorityQueue<Node> unvisited = d.getUnvisited();
                this.unvisited = unvisited;

                this.unvisited.remove(pathFinderStartNode);
                Graphics x = getGraphics();

                x.setColor(Color.MAGENTA);
                for (Node node : this.unvisited) {
                    x.fillRect(node.getX() * gridDimention + 1, node.getY() * gridDimention + 1, gridDimention - 1, gridDimention - 1);
                }
            } else if (operation.equals(d.VISITED)) {
                PriorityQueue<Node> visit = d.getVisited();
                visited = visit;
                visited.remove(pathFinderStartNode);
                Graphics x = getGraphics();
                x.setColor(Color.green);
                for (Node node : visited) {
                    x.fillRect(node.getX() * gridDimention + 1, node.getY() * gridDimention + 1, gridDimention - 1, gridDimention - 1);
                }
            }
        } else if(selection.equals("BFS")){
            String operation = (String) arg;
            BFS b = (BFS) o;
            if(operation.equals("uv")){
                Queue<Node> unvisited = b.getBFSunvisited();
                unvisited.remove(pathFinderStartNode);
                this.options = unvisited;
                Graphics x = getGraphics();
                x.setColor(Color.green);
                for (Node node : unvisited) {
                    x.fillRect(node.getX() * gridDimention + 1, node.getY() * gridDimention + 1, gridDimention - 1, gridDimention - 1);
                }
            }
        }
    }
}

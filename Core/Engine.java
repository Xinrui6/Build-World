package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Engine {
    private static final File CWD = new File(System.getProperty("user.dir"));
    private static final File GAMEDir = Loading.join(CWD, "GameDir");
    private static final File NAME = Loading.join(GAMEDir, "name");
    private static final File POSITION = Loading.join(GAMEDir, "Position");
    private static final File SEED = Loading.join(GAMEDir, "seed");
    private static final Font titleF = new Font("Monaco", Font.BOLD, 50);
    private static final Font textF = new Font("Monaco", Font.BOLD, 30);
    public static final int WIDTH = 90;
    public static final int HEIGHT = 40;
    TERenderer ter = new TERenderer();
    private World world;
    private TETile[][] tWorld;
    private String avatarName;
    /**
     * Constructor of an Engine, initial a canvas with background color and size
     * */
    public Engine() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        engineInit();
    }
    /** Initialize the engine,
     * enable the input with keyboard
     * draw the main menu and title*/
    private void engineInit() {
        StdDraw.enableDoubleBuffering();
        startGame();
        manuInteraction();
        titleOfGame();
        ter.renderFrame(tWorld);
    }
    /**
     * draw the main menu, arrange their position and size
     * */
    private void startGame() {
        String s = "No Name for This Game Yet";
        StdDraw.setFont(titleF);
        drawFrame(s, 0.5, 0.65);
        StdDraw.setFont(textF);
        String ng = "New Game (N)";
        drawFrame(ng, 0.5, 0.45);
        String lg = "Load Game (L)";
        drawFrame(lg, 0.5, 0.375);
        String qg = "Quit Game (Q)";
        drawFrame(qg, 0.5, 0.3);

    }
    /**
     * helper for keyboard input, ensure all chars are uppercase
     * */
    private char charInput() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                return Character.toUpperCase(c);
            }
        }
    }
    /**
     * draw the title of the game,
     * name is given by player
     * */
    private void titleOfGame() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(titleF);
        drawFrame("Adventure of " + avatarName, 0.5, 0.6);
        StdDraw.pause(2000);
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont();
    }
    /**
     * Initialize a 2D-tile world with given size and generate a random seed
     * */
    private void worldInit() {
        this.world = new World(WIDTH, HEIGHT, RandomUtils.uniform(new Random(), 0, 999999999));
        this.tWorld = world.createRandomWorld(ter, avatarName);
    }
    /**
     * load a generated world and restore the status before last exiting
     * */
    private void loadInit(long seed, int x, int y){
        this.world = new World(WIDTH, HEIGHT, seed);
        this.tWorld = world.createRandomWorld(ter, avatarName);
        world.newA.setPos(new Room.Position(x, y), tWorld);
    }
    /**
     * interaction based on the input char
     * N for start a new game
     * L for load a game
     * Q for quiting
     * */
    private void manuInteraction() {
        while (true) {
            if (charInput() == 'N') {
                StdDraw.clear(Color.BLACK);
                nameInit();
                String name = createName();
                nameFrame(name);
                worldInit();
                break;
            } else if (charInput() == 'L') {
                avatarName = Loading.readObject(NAME, String.class);
                String pos = Loading.readObject(POSITION, String.class);
                long seed = Loading.readObject(SEED, Long.class);
                int x = Integer.parseInt(pos.substring(0, 2));
                int y = Integer.parseInt(pos.substring(2, 4));
                loadInit(seed, x, y);

                break;
            } else if (charInput() == 'Q') {
                System.exit(0);
            }
        }
    }
    /**
     * draw the name menu
     * */
    private void nameInit() {
        StdDraw.setFont(textF);
        String message = "Hit enter to confirm";
        drawFrame(message, 0.5, 0.8);
        String name = "Please enter your name: ";
        drawFrame(name, 0.4, 0.5);
    }
    /**
     * recursion helper of name creating
     * */
    private String createName() {
        String s = nameHelper();
        if (checkLength(s)) {
            avatarName = s;
            return s;
        }
        nameInit();
        return createName();
    }
    /**
     * take player's name
     * hit enter to confirm
     * does not support delete char yet
     * */
    private String nameHelper() {
        char c;
        String s = "";
        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                if (StdDraw.isKeyPressed(10)) {
                    return s;
                }
                c = StdDraw.nextKeyTyped();
                s = s + c;
                nameFrame(s);
            }
        }
    }
    /**
     * check length for player's name
     * */
    private boolean checkLength(String s) {
        if (s.length() < 3) {
            String m = "Too short, please be serious!";
            drawFrame(m, 0.5, 0.4);
            StdDraw.pause(2000);
            StdDraw.clear(Color.black);
            return false;
        } else if (s.length() > 12) {
            String m = "Your name is too LOOOOOOOONG";
            drawFrame(m, 0.5, 0.4);
            StdDraw.pause(2000);
            StdDraw.clear(Color.black);
            return false;
        }
        return true;
    }
    /**
     * set the name's position
     * */
    private void nameFrame(String s) {
        StdDraw.setPenColor(Color.ORANGE);
        StdDraw.textLeft(WIDTH * 0.55, HEIGHT * 0.5, s);
        StdDraw.show();
    }

    /**
     * helper for all text drawing except name
     * */
    private void drawFrame(String s, double xPos, double yPos) {
        StdDraw.setPenColor(Color.ORANGE);
        StdDraw.text(WIDTH * xPos, HEIGHT * yPos, s);
        StdDraw.show();
    }

    /**
     * convert x or y position into two numbers by add 0 before it
     * */
    private String posToString(String p) {
        if (p.length() == 1) {
            return "0" + p;
        }
        return p;
    }
    /**
     * set for save game
     * */
    private void setPersistence() throws IOException {
        GAMEDir.mkdir();
        NAME.createNewFile();
        SEED.createNewFile();
        POSITION.createNewFile();
    }
    /**
     * save the game
     * also save the avatar's position
     * */
    private void saveGame() {
        Loading.writeObject(NAME, avatarName);
        String x = posToString(String.valueOf(world.newA.getaP().x));
        String y = posToString(String.valueOf(world.newA.getaP().y));
        Loading.writeObject(POSITION, x + y);
        Loading.writeObject(SEED, world.seed);
    }
    /**
     * set up direction interaction
     * */
    public void interactWithKeyboard() throws IOException {
        while (true) {
            mouseInteraction();
            char c = charInput();
            if (c == 'W') {
                world.newA.moveHelper(0, 1, tWorld);
            } else if (c == 'S') {
                world.newA.moveHelper(0, -1, tWorld);
            } else if (c == 'D') {
                world.newA.moveHelper(1, 0, tWorld);
            } else if (c == 'A') {
                world.newA.moveHelper(-1, 0, tWorld);
            } else if (c == '0') {
                setPersistence();
                saveGame();
                System.exit(0);
            }
            ter.renderFrame(tWorld);
        }

    }
    /**
     * set up mouse interaction
     * */
    private void mouseInteraction() {
        while (true) {
            TETile t = tWorld[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()];
            drawFrame(t.description(), 0.05, 0.9);
            drawFrame("", 0.05, 0.9);
            break;
        }
    }
}

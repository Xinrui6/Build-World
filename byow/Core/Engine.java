package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static byow.Core.Loading.join;

public class Engine {
    public static final File CWD = new File(System.getProperty("user.dir"));
    private static final File GAMEDir = join(CWD, "GameDir");
    private static final File NAME = join(GAMEDir, "name");
    private static final File POSITION = join(GAMEDir, "Position");
    private static final File SEED = join(GAMEDir, "seed");
    private static final File MAP = join(GAMEDir, "map");
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
    public Engine() throws IOException {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        engineInit();
    }
    public Engine(String name) throws IOException {

        StdDraw.clear(Color.BLACK);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        newInit(name);

    }
    /** Initialize the engine,
     * enable the input with keyboard
     * draw the main menu and title*/
    private void engineInit() throws IOException {
        StdDraw.enableDoubleBuffering();
        startGame();
        manuInteraction();
        titleOfGame();
        ter.renderFrame(tWorld);
        StdDraw.pause(1000);

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
        Loading.writeObject(MAP, tWorld);
    }
    /**
     * load a generated world and restore the status before last exiting
     * */
    private void loadInit(long seed, int x, int y)  {
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
    private void manuInteraction() throws IOException {
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
    private static void drawFrame(String s, double xPos, double yPos) {
        StdDraw.setPenColor(Color.ORANGE);
        StdDraw.text(WIDTH * xPos, HEIGHT * yPos, s);
        StdDraw.show();
    }
    public void keyFrame() {
        while (true) {
        if (world.getNewA().isHasKey()) {
            StdDraw.picture(80, 37, world.getPath(),2,2);
            StdDraw.show();
            }
            break;
        }
    }
    public static void lockframe() {

        String s = "You need a key to pass.";
        drawFrame(s, 0.5, 0.9);
        StdDraw.pause(1000);
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
        TETile[][] myWorld = Loading.readObject(MAP, TETile[][].class);
        ter.renderFrame(myWorld);
        StdDraw.pause(1000);
        //switchOff(world.getApos(), tWorld);
        ter.renderFrame(tWorld);
        while(true) {
            char c = charInput();
            mouseInteraction();
            keyFrame();
        while (true) {

            if (c == 'W') {
                world.newA.moveHelper(0, 1, tWorld, myWorld, 'W');
                break;
            } else if (c == 'S') {
                world.newA.moveHelper(0, -1, tWorld, myWorld, 'S');
                break;
            } else if (c == 'D') {
                world.newA.moveHelper(1, 0, tWorld, myWorld, 'D');
                break;
            } else if (c == 'A') {
                world.newA.moveHelper(-1, 0, tWorld, myWorld, 'A');
                break;
            } else if (c == '0') {
                setPersistence();
                saveGame();
                System.exit(0);
            }
            //switchOff(world.getApos(), tWorld);
            switchOn(world.getApos(), myWorld);

            ter.renderFrame(tWorld);
            }
        }
    }

    private int[] checkdis(Room.Position pos){
        int[] Coor = new int[4];
        int xPos = pos.x;
        int yPos = pos.y;
        int xL = xPos - 2;
        if (xL <= 0) {
            xL = 1;

        }
        int xR = xPos + 2;
        if (xR >= WIDTH) {
            xR = WIDTH - 1;
        }
        int yU = yPos + 2;
        if (yU >= HEIGHT) {
            yU = HEIGHT - 1;
        }
        int yD = yPos - 2;
        if (yD <= 0) {
            yD = 1;
        }
        Coor[0] = xL;
        Coor[1] = xR;
        Coor[2] = yU;
        Coor[3] = yD;
        return Coor;
    }
    private void switchOn(Room.Position pos, TETile[][] myWorld) {
        int[] posC = checkdis(pos);
        int xL = posC[0];
        int xR = posC[1];
        int yU = posC[2];
        int yD = posC[3];
        for (int i = xL; i <= xR; i++) {
            for (int j = yD; j <= yU; j++) {
                tWorld[i][j] = myWorld[i][j];
            }
        }
    }

    private void switchOff(Room.Position pos, TETile[][] myWorld) {
        int[] posC = checkdis(pos);
        int xL = posC[0];
        int xR = posC[1];
        int yU = posC[2];
        int yD = posC[3];
        for (int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                tWorld[i][j] = Tileset.NOTHING;
            }
        }
        for (int i = xL; i< xR; i++) {
            for (int j = yD; j < yU; j++) {
                tWorld[i][j] = myWorld[i][j];
            }
        }
        /**for (int i = 0; i < WIDTH; i ++) {
            for (int j = yU; j < HEIGHT; j ++) {
                tWorld[i][j] = Tileset.NOTHING;
            }
        }
        for (int i = 0; i < WIDTH; i ++) {
            for (int j = 0; j < yD; j++) {
                tWorld[i][j] = Tileset.NOTHING;
            }
        }
        for (int i = 0; i < xL; i++) {
            for (int j = yD; j < yU; j++) {
                tWorld[i][j] = Tileset.NOTHING;
            }
        }
        for (int i = xR; i < WIDTH; i++) {
            for (int j = yD; j < yU; j++) {
                tWorld[i][j] = Tileset.NOTHING;
            }
        }*/
    }



    /**
     * reinit the enigne if the player enter the next floor
     * */
    private void newInit(String name) throws IOException {
        avatarName = name;
        this.world = new World(WIDTH, HEIGHT, RandomUtils.uniform(new Random(), 0, 99999999));
        this.tWorld = world.createRandomWorld(ter, name);
        Loading.writeObject(MAP, this.tWorld);
        interactWithKeyboard();

    }
    /**
     * set up mouse interaction
     * */
    private void mouseInteraction() {
        while (true) {
            TETile t = tWorld[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()];
            drawFrame(t.description(), 0.1, 0.9);
            drawFrame("", 0.1, 0.9);
            break;
        }
    }
}


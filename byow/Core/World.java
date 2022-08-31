package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;
public class World {
    protected Random random;
    protected int width;
    protected int height;
    protected HashMap<Integer, Room> roomMap;
    protected PriorityQueue<Room> roomQueue;
    protected Avatar newA;
    protected long seed;
    /**
     * Constructor of a world
     * @param width the width of the world
     * @param height the height of the world
     * @param seed randomly generated a long number
     * */
    public World(int width, int height, long seed) {
        this.random = new Random(seed);
        this.width = width;
        this.height = height;
        this.roomMap = new HashMap<>();
        this.roomQueue = new PriorityQueue<>();
        this.seed = seed;
    }

    /**
     * initialize the world
     * */
    private static TETile[][] init(int width, int height, TERenderer tera) {
        tera.initialize(width, height);
        return new TETile[width][height];

    }
    /**
     * draw NOTHING tile on the canvas
     * */
    private void initHall(TETile[][] tWorld) {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tWorld[x][y] = Tileset.NOTHING;
            }
        }
    }
    /**
     * generated a random world based on the seed
     * @param tera the render engine
     * @param name the avatar name given by player
     * */
    public TETile[][] createRandomWorld(TERenderer tera, String name) {
        TETile[][] tWorld = init(width, height, tera);
        initHall(tWorld);
        int howMany = randomNum(15, 20);
        worldConstructor(howMany, tWorld);
        while (roomQueue.size() > 1) {
            creteHallWay(tWorld);
        }
        setDoor(tWorld);
        newA = new Avatar(name, tWorld, random, roomMap);
        return tWorld;
    }
    /**
     * draw each room on the canvas
     *
     * */
    private void worldConstructor(int howMany, TETile[][] tWorld) {
        for (int n = 1; n <= howMany; n++) {
            Room newRoom = new Room(this.random);
            newRoom.recuHerlper(tWorld, newRoom);
            newRoom.createRoom(tWorld);
            roomMap.put(n, newRoom);
            roomQueue.add(newRoom);
        }
    }
    /**
     * draw each hallway between rooms
     * */
    public void creteHallWay(TETile[][] tWorld) {
        Room first = roomQueue.poll();
        Room second = roomQueue.poll();
        Hallway hallway = new Hallway(first, second, this.random);
        hallway.createHall(tWorld, hallway.getRoom1P());
        hallway.createHall(tWorld, hallway.getRoom2P());

        if (randomNum(0, 10000) % 2 == 0) {
            roomQueue.add(first);
        } else {
            roomQueue.add(second);
        }
    }
    /**
     * generate a random number
     * */
    private int randomNum(int min, int max) {
        return RandomUtils.uniform(randomHelper(), min, max);
    }
    /**
     * generate a random object
     * */
    public Random randomHelper() {
        return new Random(RandomUtils.uniform(random, 999999999));
    }
    /**
     * set up a door for the next level
     * ensure it does not build in the corner
     * */
    private void setDoor(TETile[][] tWorld) {
        int x = randomNum(1, width - 1);
        int y = randomNum(1, height - 1);
        if (notCorner(tWorld, x, y)) {
            tWorld[x][y] = Tileset.LOCKED_DOOR;
        } else {
            setDoor(tWorld);
        }
    }


    /**
     * helper to check if the position is an inaccessible corner
     * */
    private boolean notCorner(TETile[][] tWorld, int x, int y) {
        if (tWorld[x][y].equals(Tileset.STONE_WALL)) {
            for (int i = x - 1; i <= x + 1; i++) {
                if (!(tWorld[i][y].equals(Tileset.STONE_WALL) || tWorld[i][y].equals(Tileset.NOTHING))) {
                    return true;
                }
            }
            for (int j = y - 1; j <= y + 1; j++) {
                if (!(tWorld[x][j].equals(Tileset.STONE_WALL) || tWorld[x][j].equals(Tileset.NOTHING))) {
                    return true;
                }
            }
        }
        return false;
    }
}


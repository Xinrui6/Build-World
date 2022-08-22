package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;
import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;
public class Room implements Comparable {

    private Position p;
    private int kuan;
    private int chang;
    private TETile t;
    private Random random;
    private static final int height = HEIGHT - 5;
    @Override
    public int compareTo(Object o) {
        return 0;
    }

    /**
     * class for the position in the 2D tile world
     * */
    protected static class Position {
        protected int x;
        protected int y;
        /**
         * constructor for a position
         * x for horizontal
         * y for vertical
         * */
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * helper for change position based on an old one
         * */
        protected Position newP(int dx, int dy) {
            return new Position(x + dx, y + dy);
        }
    }

    /**
     * constructor for a room
     * @param random generated in the world
     * */
    public Room(Random random) {
        this.random = random;
        this.p = randomPos();
        this.kuan = randomSize(6, 12);
        this.chang = randomSize(6, 12);
        this.t = randomTile();
    }

    /**
     * get a room's position
     **/
    public Position getP() {
        return this.p;
    }

    /**
     * get width of a room
     * kuan is Chinese pinyin for width
     * */
    public int getKuan() {
        return this.kuan;
    }

    /**
     * get length of a room
     * chang is Chinese pinyin for length
     * */
    public int getChang() {
        return this.chang;
    }

    /**
     * if the start position of a room is too close to wall, cancel it
     * */
    private boolean noRoomCreate(Position p) {
        return (WIDTH - p.x < 2 || height - p.y < 2);
    }

    /**
     * if the width or length is too long, change the size based on the distance to the wall
     * */
    private static Position reSize(int xSize, int ySize, Position p) {
        if (WIDTH - xSize <= p.x) {
            xSize = WIDTH - p.x - 1;
        }
        if (height - ySize <= p.y) {
            ySize = height - p.y - 1;
        }
        return new Position(xSize, ySize);
    }

    /**
     * create a room in the 2D world
     * */
    public void createRoom(TETile[][] tWorld) {
        if (noRoomCreate(p)) {
            return;
        }
        Position max = reSize(kuan, chang, p);
        if ((max.x <= 2) || (max.y <= 2)) {
            return;
        }
        buildWall(p, max.x, max.y, tWorld);
        roomFloor(p, max.x, max.y, tWorld, randomTile());
    }

    /**
     * generate a random object
     * */
    private Random randomHelper() {
        return new Random(RandomUtils.uniform(random, 999999999));
    }
    /**
     * generate a random position in the 2D world
     * */
    private Position randomPos() {
        int xPos = RandomUtils.uniform(randomHelper(), WIDTH);
        int yPos = RandomUtils.uniform(randomHelper(), height);
        return new Position(xPos, yPos);
    }

    /**
     * recursion helper for overlaping checking
     * */
    public void recuHerlper(TETile[][] t, Room r) {
        if (overlapHelper(t, r)){
            p = randomPos();
            recuHerlper(t, r);
        }
    }

    /**
     * check if the position will overlap with pervious one
     * if it is the case, generate another position
     * */
    public boolean overlapHelper(TETile[][] tWorld,  Room r) {
        int x = r.p.x;
        int y = r.p.y;
        int xa = r.p.x + r.kuan ;
        int ya = r.p.y + r.chang ;
        if ((xa >= WIDTH) || (ya >= height)) {
            return true;
        }
        return !tWorld[x][y].equals(Tileset.NOTHING)
                || !tWorld[xa][ya].equals(Tileset.NOTHING)
                || !tWorld[x][ya].equals(Tileset.NOTHING)
                || !tWorld[xa][y].equals(Tileset.NOTHING);
    }

    /**
     * generate random sizes for room in the given range
     * */
    protected int randomSize(int min, int max) {
        return RandomUtils.uniform(randomHelper(), min, max);
    }

    /**
     * build room's walls
     * */
    protected static void buildWall(Position p, int xSize, int ySize, TETile[][] tWorld) {
        drawHeng(tWorld, p, xSize, Tileset.WALL);
        drawHeng(tWorld, p.newP(0, ySize), xSize, Tileset.WALL);
        drawShu(tWorld, p, ySize, Tileset.WALL);
        drawShu(tWorld, p.newP(xSize, 0), ySize, Tileset.WALL);
    }

    /**
     * check if the tile is rendered already
     * */
    private static void alreadyFloor(TETile[][] tWorld, int x, int y, TETile t) {
        if (tWorld[x][y].equals(Tileset.NOTHING) ) {
            tWorld[x][y] = t;
        }
    }

    /**
     * draw the room floor
     */
    protected static void roomFloor(Position p, int xSize, int ySize, TETile[][] tWorld, TETile t) {
        for (int y = 0; y < ySize - 1; y++) {
            for (int x = 0; x < xSize - 1; x++) {
                stopFloor(tWorld, p.x + 1 + x, p.y + 1 + y, t);
            }
        }
    }

    /**
     * ensure hallways floors does not override the rooms' floor
     * */
    private static void stopFloor(TETile[][]tWorld, int x, int y, TETile t) {
        if (t.equals(Tileset.FLOOR)) {
            if (tWorld[x][y].equals(Tileset.WALL) || tWorld[x][y].equals(Tileset.NOTHING)) {
                tWorld[x][y] = t;
            }
        } else {
            if (tWorld[x][y].equals(Tileset.WALL) || tWorld[x][y].equals(Tileset.NOTHING)
                    || tWorld[x][y].equals(Tileset.FLOOR)) {
                tWorld[x][y] = t;
            }
        }
    }

    /**
     * draw tile horizontally
     * Heng is Chinese pinyin for horizontal
     * */
    private static void drawHeng(TETile[][] tWorld, Position p, int length, TETile t) {
        for (int x = 0; x <= length; x++) {
            alreadyFloor(tWorld, p.x + x, p.y, t);
        }
    }

    /**
     * draw tile vertically
     * Shu is Chinese pinyin for vertical
     * */
    private static void drawShu(TETile[][] tWorld, Position p, int length, TETile t) {
        for (int y = 1; y < length; y++) {
            alreadyFloor(tWorld, p.x, p.y + y, t);
        }
    }

    /**
     * random choose a tile for room
     * */
    private TETile randomTile() {
        int tileNum = random.nextInt(6);
        return switch (tileNum) {
            case 0 -> Tileset.WATER;
            case 1 -> Tileset.GRASS;
            case 2 -> Tileset.MOUNTAIN;
            case 3 -> Tileset.SAND;
            case 4 -> Tileset.TREE;
            case 5 -> Tileset.FLOWER;
            default -> Tileset.NOTHING;
        };
    }
}


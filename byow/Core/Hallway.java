package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Core.Room.Position;
import java.util.Random;

public class Hallway {
    private Position room1P;
    private Position med;
    private Position room2P;
    private Random random;
    /**
     * constructor of a hallway
     * @param room1 first room the hallway starts
     * @param room2 second room the hallway ends
     * @param random random object generated from the world
     * */
    public Hallway(Room room1, Room room2, Random random) {
        this.random = random;
        room1P = room1.getP().newP(randomPos(1, room1.getKuan() - 2),
                randomPos(1, room1.getChang() - 2));
        room2P = room2.getP().newP(randomPos(1, room2.getKuan() - 2),
                randomPos(1, room2.getChang() - 2));
        changeP();
        med = chooseMed(room1P, room2P);
    }
    /**
     * prevent the hallway generated in the corners
     * */
    private void changeP() {
        if (room1P.x == 0) {
            room1P.newP(2, 0);
        }
        if (room1P.y == 0) {
            room1P.newP(0, 2);
        }
        if (room2P.x == 0) {
            room2P.newP(2, 0);
        }
        if (room2P.y == 0) {
            room2P.newP(0, 2);
        }
    }

    /**
     * return the first room's position
     * */
    public Position getRoom1P() {
        return room1P;
    }

    /**
     * return the second room's position
     * */
    public Position getRoom2P() {
        return room2P;
    }

    /**
     * randomly generate a position
     * */
    private int randomPos(int n, int m) {
        return RandomUtils.uniform(random, n, m);
    }

    /**
     * choose a point to make a turning
     * */
    private Position chooseMed( Position start1, Position start2) {
        int n = random.nextInt(2);
        return switch (n) {
            case 0 -> new Position(start1.x, start2.y);
            case 1 -> new Position(start2.x, start1.y);
            default -> null;
        };
    }

    /**
     * create a hallway, ensure the hallway construct normally
     * */
    protected void createHall(TETile[][] tWorld, Position p, TETile wall, TETile floor) {
        int K1or2 = RandomUtils.uniform(random, 2, 4);
        Position start = p.newP(-1, -1);
        int hallHeng = K1or2;
        int hallShu = Math.abs(p.y - med.y) + 2;
        if (med.x == p.x) {
            if (med.y < p.y) {
                start = med.newP(-1, -1);
            }
        } else if (med.y == p.y) {
            if (med.x < p.x) {
                start = med.newP(-1, -1);
            }
            hallHeng = Math.abs(p.x - med.x) + 2;
            hallShu = K1or2;
        }
        buildHall(start, hallHeng, hallShu, tWorld, wall, floor);
    }

    /**
     * draw the hallway in the 2D world
     *  */
    private void buildHall(Position p, int xSize, int ySize, TETile[][] tWorld,TETile wall,  TETile floor) {
        Room.buildWall(p, xSize, ySize, tWorld, wall);
        Room.roomFloor(p, xSize, ySize, tWorld, floor);
    }
}

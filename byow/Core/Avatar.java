package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class Avatar {
    private String name;
    private Room.Position aP;
    private TETile floor;
    private boolean hasKey;
    private char foot;
    /**
     * Constructor of an avatar
     * @param name entered by player
     * @param tWorld the tile world
     * @param r the world generated random object
     * @param m hashmap for all rooms to random position of avatar*/
    public Avatar(String name, TETile[][] tWorld, Random r, HashMap m) {
        this.name = name;
        this.aP = null;
        this.floor = null;
        this.foot = '@';
        posInit(tWorld, r, m);
        hasKey = false;
    }
    /**
     * get avatar's position
     * */
    public String getName() {
        return name;
    }
    public Room.Position getaP() {
        return aP;
    }
    public TETile getFloor() {
        return floor;
    }
    public Room.Position changeaP(int x, int y) {
        return aP.newP(x, y);
    }
    public boolean isHasKey() {
        return hasKey;
    }
    /**
     * set up avatar position
     * */
    public void setPos(Room.Position p, TETile[][] tWorld) {
        TETile t = tWorld[p.x][p.y];
        tWorld[aP.x][aP.y] = floor;
        aP = p;
        tWorld[aP.x][aP.y] = Tileset.AVATAR_DOWN_S;
        floor = t;
    }
    /**
     * random generate a positoin based on the room
     * */
    private void posInit(TETile[][] tWorld, Random r, HashMap m) {
        int roomIndex = RandomUtils.uniform(r, 1, m.size());
        Room aRoom = (Room) m.get(roomIndex);
        aP = posHelper(aRoom, r);
        floor = tWorld[aP.x][aP.y];
        tWorld[aP.x][aP.y] = Tileset.AVATAR_DOWN_S;
        foot = tWorld[aP.x][aP.y].character();

    }
    /**
     * helper to ensure avatar does not generated in wall
     * */
    private Room.Position posHelper(Room aRoom, Random r) {
        int x = aRoom.getP().x + 1;
        int y = aRoom.getP().y + 1;
        int xx = aRoom.getKuan() + x - 1;
        int yy = aRoom.getChang() + y - 1;
        int finalX = RandomUtils.uniform(r, x, xx);
        int finalY = RandomUtils.uniform(r, y, yy);
        return new Room.Position(finalX, finalY);
    }
    /**
     * helper ensure the wall can block avatar's movement
     * */
    public void moveHelper(Room.Position moveP, TETile[][] tWorld, TETile[][] myWorld, char dir) {
        foot = tWorld[aP.x][aP.y].character();
        myWorld[aP.x][aP.y] = floor;
        aP = moveP;
        floor = myWorld[moveP.x][moveP.y];
        avatarStep(myWorld, dir, moveP);
    }

    protected void pHelper(TETile[][] myWorld, Room.Position pos) {
        myWorld[aP.x][aP.y] = floor;
        floor = myWorld[pos.x][pos.y];
        aP = pos;
        myWorld[pos.x][pos.y] = Tileset.AVATAR_DOWN_S;
    }

    /**
     * put the key on the hand from the floor
     * */
    protected void getKey(TETile[][] tWorld, Room.Position moveP) {
        hasKey = true;
        tWorld[moveP.x][moveP.y] = floor;
    }
    /**
     * if player has the key, enter the next floor
     *
    protected void nextF() throws IOException {
        if (hasKey) {
        Engine e = new Engine(name);
        e.newInit(name);
        e.interactWithKeyboard();
        hasKey = false;
        }
    }
    */
    /**
     * animation for switch foor of avatar foot
     * */
    private void avatarStep(TETile[][] tWorld, char dir, Room.Position moveP) {
        switch (dir) {
            case 'W':
                if (foot == 'r') {
                    tWorld[moveP.x][moveP.y] = Tileset.AVATAR_UP_L;
                } else {
                    tWorld[moveP.x][moveP.y] = Tileset.AVATAR_UP_R;
                }
                break;
            case 'S':
                if (foot == 'r') {
                    tWorld[moveP.x][moveP.y] = Tileset.AVATAR_DOWN_L;
                } else {
                    tWorld[moveP.x][moveP.y] = Tileset.AVATAR_DOWN_R;
                }
                break;
            case 'A':
                if (foot == 'r') {
                    tWorld[moveP.x][moveP.y] = Tileset.AVATAR_LEFT_L;
                } else {
                    tWorld[moveP.x][moveP.y] = Tileset.AVATAR_LEFT_R;
                }
                break;
            case 'D':
                if (foot == 'r') {
                    tWorld[moveP.x][moveP.y] = Tileset.AVATAR_RIGHT_L;
                } else {
                    tWorld[moveP.x][moveP.y] = Tileset.AVATAR_RIGHT_R;
                }
        }

    }


}

package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

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
    public Room.Position getaP() {
        return aP;
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
    public void moveHelper(int x, int y, TETile[][] tWorld, TETile[][] myWorld, char dir) throws IOException {
        Room.Position moveP = aP.newP(x, y);
        foot = tWorld[aP.x][aP.y].character();
        myWorld[aP.x][aP.y] = floor;
        if (myWorld[moveP.x][moveP.y].character() =='w') {
            return;
        }
        if (myWorld[moveP.x][moveP.y].description().equals("locked door")) {
            Engine e = new Engine(name);
            hasKey = false;
        }
        if (myWorld[moveP.x][moveP.y].character() == 'k') {
            hasKey = true;
            tWorld[moveP.x][moveP.y] = floor;
            Engine.keyFrame(hasKey);
        } else {
        tWorld[aP.x][aP.y] = floor;
        aP = moveP;
        floor = tWorld[moveP.x][moveP.y];
        avatarStep(tWorld, dir, moveP);
        }
    }

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

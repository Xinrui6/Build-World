package byow.TileEngine;

import java.awt.Color;
import java.io.File;
import java.nio.file.Paths;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.STONE_FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final File CWD = new File(System.getProperty("user.dir"));

    // avatar
    public static final TETile AVATAR_UP_L = new TETile('l', Color.white, Color.black, "You", join(CWD, "/Assets/upL.png").getPath());
    public static final TETile AVATAR_UP_R = new TETile('r', Color.white, Color.black, "You", join(CWD, "/Assets/upR.png").getPath());
    public static final TETile AVATAR_DOWN_S = new TETile('@', Color.white, Color.black, "You", join(CWD, "/Assets/downS.png").getPath());
    public static final TETile AVATAR_DOWN_L = new TETile('l', Color.white, Color.black, "You", join(CWD, "/Assets/downL.png").getPath());
    public static final TETile AVATAR_DOWN_R = new TETile('r', Color.white, Color.black, "You", join(CWD, "/Assets/downR.png").getPath());
    public static final TETile AVATAR_LEFT_L = new TETile('l', Color.white, Color.black, "You", join(CWD, "/Assets/leftL.png").getPath());
    public static final TETile AVATAR_LEFT_R = new TETile('r', Color.white, Color.black, "You", join(CWD, "/Assets/leftR.png").getPath());
    public static final TETile AVATAR_RIGHT_L = new TETile('l', Color.white, Color.black, "You", join(CWD, "/Assets/rightL.png").getPath());
    public static final TETile AVATAR_RIGHT_R = new TETile('r', Color.white, Color.black, "You", join(CWD, "/Assets/rightR.png").getPath());




    // texture
    public static final TETile STONE_WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "A cold stone wall", join(CWD, "/Assets/stoneWall3.png").getPath());
    public static final TETile STONE_FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor", join(CWD, "/Assets/stoneFloor4.png").getPath());
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "Soft green grass", join(CWD, "/Assets/Grass2.png").getPath());
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "Clear water waves", join(CWD, "/Assets/Water2.png").getPath());
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand", join(CWD, "/Assets/Sand2.png").getPath());
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");

    // keys
    public static final TETile goldenKey = new TETile('k', Color.green, Color.black, "A shinning golden key", join(CWD, "/Assets/goldenKey.png").getPath());
    public static final TETile stoneKey = new TETile('k', Color.green, Color.black, "A heavy grey stone key", join(CWD, "/Assets/stoneKey.png").getPath());


    static File join(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }
}



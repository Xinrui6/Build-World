# Build Our Own Game
Hello!  :smirk:  
This is a simple interactive RPG-like game with implementation of a 2D tile-based world exploration engine.   
The developers: Me & Zhishuo Fang [(gimesmmilkt)](https://github.com/gimesmmilkt)
    
This is the project 3 of [Berkeley CS61B](https://sp21.datastructur.es/materials/proj/proj3/proj3). The world is pseudorandomly generated based on seed, including number of rooms and hallways, and position of these. The user will be able to explore by walking around and interacting with objects in that world.       
The functions in `Loading` class is provided by the course. The 2D tile-based world and interactive engine is based on the library `StdDraw.java` by Princeton:   
```
https://introcs.cs.princeton.edu/java/stdlib/StdDraw.java.html
```
##  Main Features :sparkles:
1. NEW/SAVE/LOAD/QUIT game. 
2. User can determine the name.  
3. Using keyboard to control avator to move around;    
4. A “Heads Up Display” (HUD): Text that describes the tile currently under the mouse pointer.    
5. To Be Continued...    

##  Basic Control System :video_game:   
**MENU**
```
N: New Game    
L: load the game (last time)
Q: quit
```
**IN GAME**
```
W: upwards    
A: leftwards    
S: downwards    
D: rightwards   
0: automatically save then quit the game.    
```



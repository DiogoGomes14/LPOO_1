package maze.logic.weapons;

import maze.logic.Maze;

public class Darts extends Maze{
    private boolean visible;

    public Darts(){
        this.row = 0;
        this.column = 0;
        this.visible = true;
    }

    public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

}

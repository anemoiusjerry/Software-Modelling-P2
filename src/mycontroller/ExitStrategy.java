package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

import java.util.HashMap;

public class ExitStrategy implements IGoalStrategy {

    @Override
    public Coordinate getGoal(MapSearch map, Coordinate currentPost) {
        /**
         * Gets next coord to move to exit.
         */
        HashMap<Coordinate, MapTile> exits = map.getExits();

        for (Coordinate c: exits.keySet()) {
            Coordinate potentialGoal = map.BFSSearch(currentPost, c);

            if (potentialGoal != null)
                return potentialGoal;
        }

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Coordinate getGoal(MapSearch map, Coordinate currentPost, WorldSpatial.Direction orientation) {
        return null;
    }
}
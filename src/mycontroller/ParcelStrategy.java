package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import static java.lang.Math.abs;

public class ParcelStrategy implements IGoalStrategy {

    @Override
    public Coordinate getGoal(MapSearch map, Coordinate currentPost) {
        HashMap<Coordinate, MapTile> parcels = map.getParcels();

        for (Coordinate c : parcels.keySet()) {
            Coordinate potentialGoal = map.BFSSearch(currentPost, c);

            if (potentialGoal != null)
                return potentialGoal;
        }
        return null;
    }

    @Override
    public Coordinate getGoal(MapSearch map, Coordinate currentPost, WorldSpatial.Direction orientation) {
        return null;
    }
}
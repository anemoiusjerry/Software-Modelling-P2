package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;

import java.util.HashMap;

public class ParcelStrategy implements IGoalStrategy{

    @Override
    public Coordinate getGoal(MapSearch map, Coordinate currentPost) {


        HashMap<Coordinate, MapTile> parcels = map.getParcels();


        for (Coordinate c: parcels.keySet()) {

            Coordinate potentialGoal = map.BFSSearch(currentPost, c);

            if (potentialGoal != null) {
                return potentialGoal;
            }

        }


        // TODO Auto-generated method stub
        return null;
    }

}
package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;
import world.WorldSpatial.*;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.abs;
import static java.lang.Math.copySign;
import static world.WorldSpatial.Direction.*;

public class ExploreStrategy implements IGoalStrategy {

    private boolean isFollowingWall = false;
    private Direction orientation;
    private Coordinate parent;

    @Override
    public Coordinate getGoal(MapSearch map, Coordinate currentPost) {
        /**
         * Inputs: MapSearch object that records all car has seen
         */

        HashMap<Coordinate, MapTile> seen = map.getMap();
        HashMap<Coordinate, MapTile> unexplored = new HashMap<Coordinate, MapTile>();
        ArrayList<Coordinate> immediateNeighbours;

        // Update map from car radar
        for (Coordinate coord : seen.keySet()) {
            if (!map.visited(coord) && !map.getWalls().containsKey(coord)) {
                unexplored.put(coord, seen.get(coord));
            }
        }

        Coordinate next = followWall(map, currentPost);

        // Move to next if haven't visited and not a wall
        if (!map.visited(next) && !map.getWalls().containsKey(next))
            return next;

        else {
            // Check if 2 squares on is visited
            Coordinate next2 = moveForward(next, orientation);
            if (!map.visited(next2) && !map.getWalls().containsKey(next2)) {
                return next2;
            }

            immediateNeighbours = getNeighbours(currentPost);

            for (Coordinate c : immediateNeighbours) {
                // Return coord if valid an unexplored
                if (unexplored.containsKey(c) && !map.getWalls().containsKey(c)) {
                    return c;
                }
            }

            // Otherwise return a valid square
            return followWall(map, currentPost);

        }
    }


//        if (unexplored.size() > 0) {
//            int min = Integer.MAX_VALUE;
//            Coordinate goal = null;
//
//            // Get the square closest to current location
//            for (Coordinate coord : unexplored.keySet()) {
//                if (distanceBetweenCoordinates(currentPost, coord) < min) {
//                    min = distanceBetweenCoordinates(currentPost, coord);
//                    goal = map.BFSSearch(currentPost, coord);
//                }
//            }
//            return goal;
//        }






    public Coordinate followWall(MapSearch map, Coordinate currentPost) {
        /**
         * Wall of left following method.
         */

        ArrayList<Coordinate> immediateNeighbours = getNeighbours(currentPost);

        Coordinate front = moveForward(currentPost, orientation);
        Coordinate left = moveForward(currentPost, turnLeft(orientation));
        Coordinate right = moveForward(currentPost, turnRight(orientation));

        // Continue forward until wall is met
        if (!map.getWalls().containsKey(front)) {
            return front;
        }
        else if (!map.getWalls().containsKey(right)) {
            return right;
        }
        else if (!map.getWalls().containsKey(left))
            return left;
        else {
            immediateNeighbours.remove(front);
            immediateNeighbours.remove(right);
            immediateNeighbours.remove(left);
            return immediateNeighbours.get(0);
        }
    }

//        if (isFollowingWall) {
//            // Wall left and wall front case - turn right
//            if (map.getWalls().containsKey(leftCoord) && map.getWalls().containsKey(frontCoord))
//                next = moveForward(currentPost, turnRight(orientation));
//            // Otherwise continue moving forward
//            else
//                next = moveForward(currentPost, orientation);
//
//        }
//
//        else {
//            // turn left if no wall on left
//            if (!map.getWalls().containsKey(leftCoord)) {
//                next = leftCoord;
//                isFollowingWall = true;
//            }

    public ArrayList<Coordinate> getNeighbours(Coordinate currentPost) {
        ArrayList<Coordinate> immediateNeighbours = new ArrayList<>();

        Coordinate up = new Coordinate(currentPost.x, currentPost.y + 1);
        Coordinate right = new Coordinate(currentPost.x + 1, currentPost.y);
        Coordinate left = new Coordinate(currentPost.x - 1, currentPost.y);
        Coordinate down = new Coordinate(currentPost.x, currentPost.y - 1);

        immediateNeighbours.add(up);
        immediateNeighbours.add(right);
        immediateNeighbours.add(left);
        immediateNeighbours.add(down);
        return immediateNeighbours;
    }

    public Coordinate moveForward(Coordinate currentPost, Direction orientation) {
        Coordinate next;
        switch (orientation) {
            case EAST:
                next = new Coordinate(currentPost.x + 1, currentPost.y);
                break;
            case NORTH:
                next = new Coordinate(currentPost.x, currentPost.y + 1);
                break;
            case SOUTH:
                next = new Coordinate(currentPost.x, currentPost.y - 1);
                break;
            case WEST:
                next = new Coordinate(currentPost.x - 1, currentPost.y);
                break;
            default:
                next = null;
                break;
        }
        return next;
    }

    public Direction turnRight(Direction orientation) {
        ArrayList<Direction> compass = new ArrayList<>();
        compass.add(NORTH);
        compass.add(EAST);
        compass.add(SOUTH);
        compass.add(WEST);

        int dir = compass.indexOf(orientation) + 1;
        // Wrap around
        if (dir > 3)
            return compass.get(0);

        return compass.get(dir);
    }

    public Direction turnLeft(Direction orientation) {
        ArrayList<Direction> compass = new ArrayList<>();
        compass.add(NORTH);
        compass.add(EAST);
        compass.add(SOUTH);
        compass.add(WEST);

        int dir = compass.indexOf(orientation) - 1;
        // Wrap around
        if (dir < 0)
            return compass.get(3);

        return compass.get(dir);
    }


    public int distanceBetweenCoordinates(Coordinate c1, Coordinate c2) {
        /**
         * Manhattan Distance
         */
        int xDiff = abs(c1.x - c2.x);
        int yDiff = abs(c1.y - c2.y);

        return xDiff + yDiff;
    }

    public void setOrientation(Direction new_direction) {
        this.orientation = new_direction;
    }

    public void setParent(Coordinate location) {
        this.parent = location;
    }
}
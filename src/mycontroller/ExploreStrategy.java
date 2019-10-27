package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import static world.WorldSpatial.Direction.*;

public class ExploreStrategy implements IGoalStrategy {

    @Override
    public Coordinate getGoal(MapSearch map, Coordinate currentPost) {
        return null;
    }

    @Override
    public Coordinate getGoal(MapSearch map, Coordinate currentPost, Direction orientation) {
        /**
         * Inputs: MapSearch object that records all car has seen
         */
        Stack<Coordinate> toBeExplored = new Stack<>();

        HashMap<Coordinate, MapTile> seen = map.getMap();
        HashMap<Coordinate, MapTile> unexplored = new HashMap<Coordinate, MapTile>();

        // Update map from car radar
        for (Coordinate coord : seen.keySet()) {
            if (!map.visited(coord) && !map.getWalls().containsKey(coord)) {
                unexplored.put(coord, seen.get(coord));
                // Stack to prioritise closest unexplored coord
                toBeExplored.push(coord);
            }
        }

        // Wall following strategy
        Coordinate next = followWall(map, currentPost, orientation);
        if (next != null)
            return next;

        ArrayList<Coordinate> neighbours = map.getNeighbours(currentPost);
        // Move to next unexplored coord
        for (Coordinate coord : unexplored.keySet()) {
            if (neighbours.contains(coord))
                return coord;
        }

        int leastVisitedWeight = Integer.MAX_VALUE;
        Coordinate leastVisited = null;


        // Run through the stack and visit the node that been visited least
        while (!toBeExplored.empty()) {
            next = map.BFSSearch(currentPost, toBeExplored.pop());

            if (next != null) {
                int visitCount = 0;
                // Pick node in least visited area
                ArrayList<Coordinate> newNeighbours = map.getNeighbours(next);

                // Count the number of visits to area around current coord
                for (Coordinate coord : newNeighbours) {
                    if (map.getVisited().containsKey(coord)) {
                        visitCount += map.getVisited().get(coord);
                    }
                }

                // Update least visited coord
                if (visitCount < leastVisitedWeight) {
                    leastVisited = next;
                    leastVisitedWeight = visitCount;
                }
            }
        }
        return leastVisited;
    }

    public Coordinate followWall(MapSearch map, Coordinate currentPost, Direction orientation) {
        /**
         * Always move forward, right turning strategy.
         */

        Coordinate front = moveForward(currentPost, orientation);
        Coordinate right = moveForward(currentPost, turnRight(orientation));

        // Continue forward until wall is met
        if (!map.getWalls().containsKey(front) && !map.visited(front)) {
            return front;
        }
        // Turn right if wall in front
        else if (!map.getWalls().containsKey(right) && !map.visited(right)) {
            return right;
        }
        // Return null and switch to random strategy
        return null;
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
}
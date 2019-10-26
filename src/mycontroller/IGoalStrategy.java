package mycontroller;

import utilities.Coordinate;
import world.WorldSpatial;

public interface IGoalStrategy {
    Coordinate getGoal(MapSearch map, Coordinate currentPost, WorldSpatial.Direction orientation);
}
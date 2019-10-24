package mycontroller;

import utilities.Coordinate;

public interface IGoalStrategy {
    Coordinate getGoal(MapSearch map, Coordinate currentPost);
}
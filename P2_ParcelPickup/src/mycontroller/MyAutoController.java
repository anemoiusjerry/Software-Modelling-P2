package mycontroller;

import controller.CarController;
import world.Car;
import world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;
import world.WorldSpatial;
import world.WorldSpatial.Direction;

public class MyAutoController extends CarController{		
		// How many minimum units the wall is away from the player.
		private int wallSensitivity = 1;
		
		private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
		
		// Car Speed to move at
		private final int CAR_MAX_SPEED = 1;
		
		HashMap<Coordinate,MapTile> worldMap = World.getMap();
		
		HashMap<Coordinate, Boolean> wallsFollowed = new HashMap<Coordinate, Boolean>();

		
		MapSearch theMap;

		
		public MyAutoController(Car car) {
			super(car);
			theMap = new MapSearch();


		}
		
		@Override
		public void update() {
			// Gets what the car can see
			HashMap<Coordinate, MapTile> currentView = getView();
			
			theMap.updateMap(currentView);
			
			theMap.mapToString(theMap.getParcels());
			
			theMap.mapToString(theMap.getExits());

			
			// checkStateChange();
			if(getSpeed() < CAR_MAX_SPEED){       // Need speed to turn and progress toward the exit
				applyForwardAcceleration();   // Tough luck if there's a wall in the way
			}
			if (isFollowingWall) {
				// If wall no longer on left, turn left
				if(!checkFollowingWall(getOrientation(), currentView)) {
					turnLeft();
				} else {
					// If wall on left and wall straight ahead, turn right
					if(checkWallAhead(getOrientation(), currentView)) {
						turnRight();
					}
				}
			} else {
				// Start wall-following (with wall on left) as soon as we see a wall straight ahead
				if(checkWallAhead(getOrientation(),currentView)) {
					turnRight();
					isFollowingWall = true;
				}
			}
		}
		
		

		private boolean isViableTrap(Coordinate goToTile) {
			
			Coordinate currentPosition = new Coordinate(getPosition());

			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * Check if you have a wall in front of you!
		 * @param orientation the orientation we are in based on WorldSpatial
		 * @param currentView what the car can currently see
		 * @return
		 */
		private boolean checkWallAhead(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView){
			switch(orientation){
			case EAST:
				return checkEast(currentView);
			case NORTH:
				return checkNorth(currentView);
			case SOUTH:
				return checkSouth(currentView);
			case WEST:
				return checkWest(currentView);
			default:
				return false;
			}
		}
		
		private void checkTrapAhead(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView, Coordinate goToTile){
			
			Coordinate currentPosition = new Coordinate(getPosition());

			if (orientation == Direction.NORTH && currentPosition.x < goToTile.x && currentPosition.y == goToTile.y) {
				
				for(int i = 0; i <= wallSensitivity; i++){
					MapTile tile = currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
					
					if(tile.isType(MapTile.Type.TRAP)){
						turnRight();
						isFollowingWall = false;

						return;
					}
					
					else if (tile.isType(MapTile.Type.WALL)) {
						return;
					}
				}
						

			}
			
			else if (orientation == Direction.NORTH && currentPosition.x > goToTile.x && currentPosition.y == goToTile.y) {
				
				for(int i = 0; i <= wallSensitivity; i++){
					MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
					
					if(tile.isType(MapTile.Type.TRAP)){
						turnLeft();
						isFollowingWall = false;

						return;
					}
					
					else if (tile.isType(MapTile.Type.WALL)) {
						return;
					}
				}
				
				

			}
			
			else if (orientation == Direction.SOUTH && currentPosition.x > goToTile.x && currentPosition.y == goToTile.y) {
				
				for(int i = 0; i <= wallSensitivity; i++){
					MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
					
					if(tile.isType(MapTile.Type.TRAP)){
						turnRight();
						isFollowingWall = false;

						return;
					}
					
					else if (tile.isType(MapTile.Type.WALL)) {
						return;
					}
				}
							

			}
			
			
			else if (orientation == Direction.SOUTH && currentPosition.x < goToTile.x && currentPosition.y == goToTile.y) {
				
				for(int i = 0; i <= wallSensitivity; i++){
					MapTile tile = currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
					
					if(tile.isType(MapTile.Type.TRAP)){
						turnLeft();
						isFollowingWall = false;

						return;
					}
					
					else if (tile.isType(MapTile.Type.WALL)) {
						return;
					}
				}
				


			}
			
			else if (orientation == Direction.EAST && currentPosition.x == goToTile.x && currentPosition.y > goToTile.y) {
				
				for(int i = 0; i <= wallSensitivity; i++){
					
					MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
					
					if(tile.isType(MapTile.Type.TRAP)){
						turnRight();
						isFollowingWall = false;

						return;
					}
					
					else if (tile.isType(MapTile.Type.WALL)) {
						return;
					}
				}
							

			}
			
			else if (orientation == Direction.EAST && currentPosition.x == goToTile.x && currentPosition.y < goToTile.y) {
				
				for(int i = 0; i <= wallSensitivity; i++){
					MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y+i));
					
					if(tile.isType(MapTile.Type.TRAP)){
						turnLeft();
						isFollowingWall = false;

						return;
					}
					
					else if (tile.isType(MapTile.Type.WALL)) {
						return;
					}
				}
				

			}
			
			else if (orientation == Direction.WEST && currentPosition.x == goToTile.x && currentPosition.y > goToTile.y) {
				
				for(int i = 0; i <= wallSensitivity; i++){
					
					MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y));
					
					if(tile.isType(MapTile.Type.TRAP)){
						turnLeft();
						isFollowingWall = false;

						return;
					}
					
					else if (tile.isType(MapTile.Type.WALL)) {
						return;
					}
				}
				
				
				isFollowingWall = false;

			}
			
			else if (orientation == Direction.WEST && currentPosition.x == goToTile.x && currentPosition.y < goToTile.y) {
				
				for(int i = 0; i <= wallSensitivity; i++){
					MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
					
					if(tile.isType(MapTile.Type.TRAP)){
						turnRight();
						isFollowingWall = false;

						return;
					}
					
					else if (tile.isType(MapTile.Type.WALL)) {
						return;
					}
				}

			}
			
//			switch(orientation){
//			case EAST:
//				return checkEast(currentView);
//			case NORTH:
//				return checkNorth(currentView);
//			case SOUTH:
//				return checkSouth(currentView);
//			case WEST:
//				return checkWest(currentView);
//			default:
//				return false;
//			}
		}		
		/**
		 * Check if the wall is on your left hand side given your orientation
		 * @param orientation
		 * @param currentView
		 * @return
		 */
		private boolean checkFollowingWall(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView) {
			
			switch(orientation){
			case EAST:
				return checkNorth(currentView);
			case NORTH:
				return checkWest(currentView);
			case SOUTH:
				return checkEast(currentView);
			case WEST:
				return checkSouth(currentView);
			default:
				return false;
			}	
		}
		
		/**
		 * Method below just iterates through the list and check in the correct coordinates.
		 * i.e. Given your current position is 10,10
		 * checkEast will check up to wallSensitivity amount of tiles to the right.
		 * checkWest will check up to wallSensitivity amount of tiles to the left.
		 * checkNorth will check up to wallSensitivity amount of tiles to the top.
		 * checkSouth will check up to wallSensitivity amount of tiles below.
		 */
		public boolean checkEast(HashMap<Coordinate, MapTile> currentView){
			// Check tiles to my right
			Coordinate currentPosition = new Coordinate(getPosition());
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
		public boolean checkWest(HashMap<Coordinate,MapTile> currentView){
			// Check tiles to my left
			Coordinate currentPosition = new Coordinate(getPosition());
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
		public boolean checkNorth(HashMap<Coordinate,MapTile> currentView){
			// Check tiles to towards the top
			Coordinate currentPosition = new Coordinate(getPosition());
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y+i));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
		public boolean checkSouth(HashMap<Coordinate,MapTile> currentView){
			// Check tiles towards the bottom
			Coordinate currentPosition = new Coordinate(getPosition());
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
	}

package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.utils.Queue;

import tiles.MapTile;
import tiles.MapTile.Type;
import tiles.TrapTile;
import utilities.Coordinate;
import world.World;


// Class responsbile for:

// 1. Tracking what we have seen so far \
// 2. Determining the shortest path between our current position and the goal position
public class MapSearch {
	public Coordinate parent;

	// Map of the walls we've seen
	private HashMap<Coordinate, MapTile> walls;

	// Map of tiles we've actually visited
	private HashMap<Coordinate, Integer> visited;

	// Map of the exits seen
	private HashMap<Coordinate, MapTile> exits;

	// Map of the exits seen
	private HashMap<Coordinate, MapTile> parcels;

	// Map of everything we've seen as of yet
	private HashMap<Coordinate, MapTile> map;



	// Initialize our primary map, and its submaps
	public MapSearch() {
		System.out.println("Creating the map!");

		this.visited = new HashMap<>();
		this.walls = new HashMap<>();
		this.map = new HashMap<>();
		this.exits = new HashMap<>();
		this.parcels = new HashMap<>();
	}

	public void visit(Coordinate c) {
		/* Record all visited coords. */
		if (visited.containsKey(c)) {
			Integer newVal = visited.get(c) + 1;
			visited.put(c, newVal);
		}
		else {
			// Create new entry
			visited.put(c, 0);
		}
	}

	public boolean visited(Coordinate c) {
		/* Checks whether a coordinate has been visited before. */
		for (Coordinate inspected: visited.keySet()) {
			if (inspected.equals(c))
				return true;
		}
		return false;
	}

	// Add the new information we've gained to our maps
	public void applyNewView(HashMap<Coordinate, MapTile> mapView) {

		for (Coordinate coord: mapView.keySet()) {
			MapTile tile = mapView.get(coord);

			// Checks if the coordinate is valid
			if (coord.x < World.MAP_WIDTH && coord.x >= 0 &&  coord.y >= 0 &&
					coord.y < World.MAP_HEIGHT) {

				// If wall, add to walls
				if (tile.isType(Type.WALL)) {
					this.walls.put(coord, tile);
					this.map.put(coord, tile);
				}

				// If Finish, add to exits
				else if (tile.isType(Type.FINISH)) {
					this.exits.put(coord, tile);
					this.map.put(coord, tile);
				}

				// If a parcel trap, add to parcels
				else if (tile.isType(Type.TRAP))   {
					TrapTile trapTile = (TrapTile)tile;

					if (trapTile.getTrap().contentEquals("parcel")){
						this.parcels.put(coord,tile);
						this.map.put(coord, tile);
					}
				}

				else if (tile.isType(Type.ROAD)) {
					this.map.put(coord, tile);
					this.parcels.remove(coord);
				}

				// if empty, remove from parcels to ensure we're no longer tracking parcels after they've been eaten
				// Bit of a bandaid fix, can make more precise solution later
				else if (tile.isType(Type.EMPTY)) {
					this.parcels.remove(coord);
				}
			}
		}
	}

	public HashMap<Coordinate, Integer> getVisited() {
		return visited;
	}

	public void setVisited(HashMap<Coordinate, Integer> visited) {
		this.visited = visited;
	}

	// Simple BFS Search, returning the next coordinate along the shortest valid (No walls on the path (can accomodate for traps later) path to our goal point
	public Coordinate BFSSearch(Coordinate start, Coordinate goal){
		Queue<Coordinate> queue = new Queue<Coordinate> ();

		HashMap<Coordinate, Boolean> visited = new HashMap();
		HashMap<Coordinate, Integer> distance = new HashMap();
		HashMap<Coordinate, Coordinate> parent = new HashMap();

		// Record everything we have seen so far
		for (Coordinate coord: map.keySet()) {
			visited.put(coord, false);
			distance.put(coord, 1000000);
			parent.put(coord, null);
		}

		visited.put(start, true);
		distance.put(start, 0);
		queue.addLast(start);

		while (queue.size != 0) {
			Coordinate u = queue.first();
			queue.removeFirst();

			ArrayList<Coordinate> adjacentPotential = getNeighbours(u);
			ArrayList<Coordinate> adjacent = new ArrayList<>();

			for (Coordinate c : adjacentPotential) {
				if (map.containsKey(c))
					adjacent.add(c);
			}

			for (Coordinate adj: adjacent) {
				if (visited.get(adj) == false) {

					visited.put(adj, true);
					distance.put(adj, distance.get(u)  + 1);
					parent.put(adj, u);
					queue.addLast(adj);

					if (adj.equals(goal)) {
						return parentToCoordinateRequired(parent, start, goal);
					}
				}
			}
		}
		System.out.println("NO GOAL FOUND :(");
		return null;
	}


	// Construct the path based on the hash map of coordinate's parent nodes
	private Coordinate parentToCoordinateRequired(HashMap<Coordinate, Coordinate> parent, Coordinate start, Coordinate goal) {

		Queue<Coordinate> path = new Queue<Coordinate> ();

		Coordinate crawl = goal;
		path.addLast(crawl);

		while(parent.get(crawl) != null) {
			path.addLast( parent.get(crawl) );
			crawl = parent.get(crawl);
		}

		path.get(path.size-1);

		System.out.println("printing first node to head to");
		System.out.println(path.get(path.size-2));

		return path.get(path.size -2);
	}

	public ArrayList<Coordinate> getNeighbours(Coordinate c) {
		ArrayList<Coordinate> adjacentPotential = new ArrayList<>();
		ArrayList<Coordinate> adjacent = new ArrayList<>();

		adjacentPotential.add(new Coordinate(c.x+1, c.y));
		adjacentPotential.add(new Coordinate(c.x-1, c.y));
		adjacentPotential.add(new Coordinate(c.x, c.y-1));
		adjacentPotential.add(new Coordinate(c.x, c.y+1));

		for (Coordinate adj : adjacentPotential) {
			if (!walls.containsKey(adj))
				adjacent.add(adj);
		}
		return adjacent;
	}

	public HashMap<Coordinate, MapTile> getWalls() {
		return walls;
	}

	public void setWalls(HashMap<Coordinate, MapTile> walls) {
		this.walls = walls;
	}

	public HashMap<Coordinate, MapTile> getExits() {
		return exits;
	}

	public void setExits(HashMap<Coordinate, MapTile> exits) {
		this.exits = exits;
	}

	public HashMap<Coordinate, MapTile> getParcels() {
		return parcels;
	}

	public void setParcels(HashMap<Coordinate, MapTile> parcels) {
		this.parcels = parcels;
	}

	public HashMap<Coordinate, MapTile> getMap() {
		return map;
	}

	public void setMap(HashMap<Coordinate, MapTile> map) {
		this.map = map;
	}
}
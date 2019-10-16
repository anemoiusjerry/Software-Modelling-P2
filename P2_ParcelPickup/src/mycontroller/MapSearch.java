package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tiles.MapTile;
import tiles.MapTile.Type;
import tiles.TrapTile;
import utilities.Coordinate;
import world.World;

public class MapSearch {
	
	
	// Map of the walls we've seen 
    private HashMap<Coordinate, MapTile> walls;
    
    // Map of the exits seen 
    private HashMap<Coordinate, MapTile> exits;
    
    // Map of the exits seen 
    private HashMap<Coordinate, MapTile> parcels;



    // Map of everything we've seen as of yet
    private HashMap<Coordinate, MapTile> map;

	
	public MapSearch() {
		
		System.out.println("Creating the map!");
		
		this.walls = new HashMap<Coordinate, MapTile>(); 
				
		this.map = new HashMap<Coordinate,MapTile>();
		
		this.exits = new HashMap<Coordinate,MapTile>();
		
		this.parcels = new HashMap<Coordinate,MapTile>();


		
       
	}
	
	public void updateMap(HashMap<Coordinate, MapTile> mapView) {
		      
        	
        	for (Coordinate coord: mapView.keySet()) {
        		
                
                MapTile tile = mapView.get(coord); 
                
//                System.out.println(tile.getType().toString() +  " at " + coord.toString());
                
//                 Checks if the coordinate is valid 
                if (coord.x < World.MAP_WIDTH && coord.x >= 0 &&  coord.y >= 0 && coord.y < World.MAP_HEIGHT ) {
                
                
                if (tile.isType(Type.WALL)) {
                	this.walls.put(coord, tile);
                }
                
                else if (tile.isType(Type.FINISH)) {
                	this.exits.put(coord, tile);
                	
                	
                	
                	
                }
                
                else if (   tile.isType(Type.TRAP)  )   {
                	
                	TrapTile trapTile = (TrapTile)tile;
                	
                	if (trapTile.getTrap().contentEquals("parcel")){
                	
                	this.parcels.put(coord,tile);
                	
                	
                	
                	}
                }
                
                this.map.put(coord, tile);
                
        		
        	}
  
        } 
        
        
        }
        
//		this.mapToString(mapView);
	
	public void mapToString(HashMap< Coordinate, MapTile> map) {
		
        System.out.println("------------------------------------------------------");

		
    	for (Coordinate coord: map.keySet()) {
    		
            
            MapTile tile = map.get(coord);   
            
        	
        	System.out.println(  tile.getType().toString() + "at " + coord.toString());
        	
        }
    	
        System.out.println("------------------------------------------------------");


		
		
	}
	
	public Coordinate BFSSearch(Coordinate start, Coordinate goal){
		
		
		
		
		
		return goal;
		
		
		
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

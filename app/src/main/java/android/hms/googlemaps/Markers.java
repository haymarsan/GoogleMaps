package android.hms.googlemaps;

import java.util.ArrayList;

public class Markers {

    private ArrayList<Location> agentLocation;
    private  ArrayList<Location> atmLocation;

    public ArrayList<Location> getAgentLocation() {
        return agentLocation;
    }

    public void setAgentLocation(ArrayList<Location> agentLocation) {
        this.agentLocation = agentLocation;
    }

    public ArrayList<Location> getAtmLocation() {
        return atmLocation;
    }

    public void setAtmLocation(ArrayList<Location> atmLocation) {
        this.atmLocation = atmLocation;
    }
}

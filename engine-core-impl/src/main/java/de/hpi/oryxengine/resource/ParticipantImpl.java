package de.hpi.oryxengine.resource;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.identity.Capability;
import de.hpi.oryxengine.identity.Participant;
import de.hpi.oryxengine.identity.Position;

/**
 * 
 * @author Gerardo Navarro Suarez
 */
public class ParticipantImpl extends ResourceImpl<Participant> implements Participant {

    private ArrayList<Position> myPositions;
    private ArrayList<Capability> myCapabilities;

    @Override
    public List<Position> getMyPositions() {

        if (myPositions == null) {
            myPositions = new ArrayList<Position>();
        }
        return myPositions;
    }

//    @Override
//    public Participant addMyPosition(Position position) {
//
//        myPositions.add(position);
//        return this;
//    }

    @Override
    public List<Capability> getMyCapabilities() {

        if (myCapabilities == null) {
            myCapabilities = new ArrayList<Capability>();
        }
        return myCapabilities;
    }

//    @Override
//    public Participant addMyCapability(Capability capability) {
//
//        myCapabilities.add(capability);
//        return this;
//    }
    

}

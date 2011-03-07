package de.hpi.oryxengine.resource;

/**
 * A Position refers to a unique job within an organization. Examples might include Positions like the CEO, bank
 * manager, secretary, etc. .
 * 
 * The purpose for this model is to define lines-of-reporting within the organization model in order to get a
 * Who-Is-My-Boss?-hierarchy.
 * 
 * @author Gerardo Navarro Suarez
 */
public interface Position extends Resource<Position> {
    
    // Vielleicht später mit Prioritäten, oder Wichitgkeit der Person

    /**
     * Gets the Participant that occupies this Position.
     * 
     * @return a Participant - Participant that occupies this Position
     */
    Participant getPositionHolder();

    /**
     * Gets the superior position.
     * 
     * @return the superior position
     */
    Position getSuperiorPosition();

    /**
     * Return the OrganizationUnit where this Position belongs to.
     * 
     * @return the OrganizationUnit where this Position belongs to
     */
    OrganizationUnit belongstoOrganization();
}

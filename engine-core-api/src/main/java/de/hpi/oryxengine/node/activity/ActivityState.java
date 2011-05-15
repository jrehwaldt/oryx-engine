package de.hpi.oryxengine.node.activity;

/**
 * This enum represents the execution states an activity may be in.
 */
public enum ActivityState {

    INIT,
    READY,
    ACTIVE,
    COMPLETED,
    SKIPPED,
    WAITING;
}

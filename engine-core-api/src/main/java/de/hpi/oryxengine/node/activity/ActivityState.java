package de.hpi.oryxengine.node.activity;

/**
 * This enum represents the execution states an activities may be in.
 */
public enum ActivityState {

    INIT,
    READY,
    ACTIVE,
    COMPLETED,
    SKIPPED,
    WAITING;
}

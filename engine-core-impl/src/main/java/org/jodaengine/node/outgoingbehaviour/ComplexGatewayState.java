package org.jodaengine.node.outgoingbehaviour;

/**
 * The Enum ComplexGatewayState represents the two states of a discriminator or more in general, a BPMN complex gateway.
 * WAITING_FOR_START means, that the gateway is enabled, but the activation Expression is not yet met.
 * WAITING_FOR_RESET means, that the gateway has fired, but was not yet reset.
 */
public enum ComplexGatewayState {
    WAITING_FOR_RESET, WAITING_FOR_START;
}

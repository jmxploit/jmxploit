package jmxattacks.type;

/**
 * Enumeration of availability status for JMX ports.
 * 
 * @author Dominique Righetto (dominique.righetto@gmail.com)
 * 
 */
public enum PortStatus {

	/** Port available without authentication */
	OPENED_WITHOUT_AUTHENTICATION,
	/** Port available with authentication */
	OPENED_WITH_AUTHENTICATION,
	/** Port not available */
	CLOSED;
}

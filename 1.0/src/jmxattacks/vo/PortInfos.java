package jmxattacks.vo;

import jmxattacks.type.PortStatus;

/**
 * Value object to store availability status infos for a JMX port.
 * 
 * @author Dominique Righetto (dominique.righetto@gmail.com)
 * 
 */
public class PortInfos {

	/** Port number */
	private int port = 0;

	/** Availability status */
	private PortStatus status;

	/**
	 * Constructor.
	 * 
	 */
	public PortInfos() {
		this.port = 1;
		this.status = PortStatus.CLOSED;
	}

	/**
	 * Constructor.
	 * 
	 * @param port Port number.
	 * @param status Availability status.
	 */
	public PortInfos(int port, PortStatus status) {
		super();
		if ((port < 1) || (port > 65535)) {
			throw new IllegalArgumentException("Port number must be between 1 and 65535 !");
		}
		if (status == null) {
			throw new IllegalArgumentException("Availability status cannot be null !");
		}
		this.port = port;
		this.status = status;
	}

	/**
	 * Getter
	 * 
	 * @return the port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Getter
	 * 
	 * @return the status
	 */
	public PortStatus getStatus() {
		return this.status;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PortInfos [port=" + this.port + ", " + (this.status != null ? "status=" + this.status.name() : "") + "]";
	}

}

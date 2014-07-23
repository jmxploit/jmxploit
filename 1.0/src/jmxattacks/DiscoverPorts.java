package jmxattacks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jmxattacks.type.PortStatus;
import jmxattacks.vo.PortInfos;
import jmxmenus.MainMenu;
import jmxutils.Connexion;
import jmxutils.EasyBasicParser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

/**
 * Module trying to discover all JMX ports available on target machine.<br>
 * Currently the module only supports RMI protocol.
 * 
 * @author Dominique Righetto (dominique.righetto@gmail.com)
 * 
 */
@SuppressWarnings({ "static-access", "static-method", "boxing" })
public class DiscoverPorts extends Attack {

	/** Module description */
	private String description = "Trying to discover all JMX ports available on target machine";

	/** Module name */
	private String name = "DiscoverPorts";

	/** Module banner */
	private String banner = "Please enter options for this module or back to return to the main menu";

	/** Module options example */
	private String example = "--host localhost --range 1-1024\n--host localhost --range 2562";

	/**
	 * Constructor
	 * 
	 */
	public DiscoverPorts() {
		super();
		this.setAttack_description(this.description);
		this.setAttack_name(this.name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see jmxattacks.Attack#prepareExploit()
	 */
	@SuppressWarnings("unused")
	@Override
	public void prepareExploit() {
		/* Display Menu to retrieve options */
		System.out.printf("%s selected%n", this.name);
		CommandLine line = attackMenu(this.setArgs(), this.banner, this.example);

		/* Apply processing */
		if (line != null) {
			// Get options
			String host = line.getOptionValue("host").trim();
			String rangeExpr = line.getOptionValue("range").trim();
			// Configure ports range to scan
			Set<Integer> range = this.parsePortsRange(rangeExpr);
			// Apply scan
			List<PortInfos> scanResult = scanHost(host, range);
			// Render result to user
			this.renderScanResult(scanResult);
		}

		/* Reset menu */
		new MainMenu();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see jmxattacks.Attack#prepareExploit(java.lang.String)
	 */
	@Override
	public void prepareExploit(String args) {
		/* Parse command line to extract working context parameters */
		Parser parser = new EasyBasicParser();
		CommandLine line = null;
		try {
			line = parser.parse(this.setArgs(), StringtoArrayList(args));
		}
		catch (ParseException e) {
			System.err.println("Parsing failed.  Reason: " + e.getMessage());
			System.exit(-1);
		}

		/* Apply processing */
		if (line != null) {
			// Get options
			String host = line.getOptionValue("host").trim();
			String rangeExpr = line.getOptionValue("range").trim();
			// Configure ports range to scan
			Set<Integer> range = this.parsePortsRange(rangeExpr);
			// Apply scan
			List<PortInfos> scanResult = scanHost(host, range);
			// Render result to user
			this.renderScanResult(scanResult);
		}
	}

	/**
	 * Method to apply ports scanning.
	 * 
	 * @param host Target machine to scan.
	 * @param ports Ports to probe.
	 * @return Scan result with opened port only.
	 */
	public List<PortInfos> scanHost(String host, Set<Integer> ports) {
		List<PortInfos> scanResult = null;
		PortInfos infos = null;

		/* Check input parameters */
		if ((host == null) || "".equalsIgnoreCase(host.trim())) {
			throw new IllegalArgumentException("Host cannot be null or empty !");
		}
		if ((ports == null) || ports.isEmpty()) {
			throw new IllegalArgumentException("Ports range cannot be null or empty !");
		}

		/* Apply scan */
		scanResult = new ArrayList<>();
		for (Integer port : ports) {
			try {
				infos = this.checkAvailability(host, port);
				if (infos.getStatus() != PortStatus.CLOSED) {
					scanResult.add(infos);
				}
			}
			catch (IOException e) {
				System.err.printf("Cannot close JMX connection for port %s (%s).%n", port, e.getMessage());
			}
		}

		return scanResult;
	}

	/**
	 * 
	 * Method to parse ports range and return a sorted set of integer.
	 * 
	 * @param rangeExpr Source ports range expression.
	 * @return Set of integer.
	 */
	public Set<Integer> parsePortsRange(String rangeExpr) {
		Set<Integer> range = null;

		/* Check input parameter */
		if ((rangeExpr == null) || "".equalsIgnoreCase(rangeExpr.trim())) {
			throw new IllegalArgumentException("Range cannot be null or empty !");
		}
		range = new TreeSet<>();

		/* Parse expression */
		if (rangeExpr.indexOf('-') == -1) {
			range.add(Integer.parseInt(rangeExpr.trim()));
		} else {
			String[] parts = rangeExpr.trim().split("-");
			int startBoundary = Integer.parseInt(parts[0].trim());
			int endBoundary = Integer.parseInt(parts[1].trim());
			if (startBoundary >= endBoundary) {
				throw new IllegalArgumentException("Starting boundary of the port range must be inferior to the ending boundary !");
			}
			for (int p = startBoundary; p <= endBoundary; p++) {
				range.add(p);
			}
		}

		return range;
	}

	/**
	 * Method to render the result of a scan to the user.
	 * 
	 * @param scanResult Scan result to use.
	 */
	private void renderScanResult(List<PortInfos> scanResult) {
		if (scanResult != null) {
			if (scanResult.isEmpty()) {
				System.out.println("No opened port found on target machine.");
			} else {
				System.out.printf("%s opened ports found on target machine:%n", scanResult.size());
				for (PortInfos info : scanResult) {
					System.out.printf("-> Port '%5d' is %s%n", info.getPort(), info.getStatus().name().replace("_", " ").toLowerCase());
				}
			}
		}
	}

	/**
	 * Define module options arguments.
	 * 
	 * @return Options configuration object.
	 */
	private Options setArgs() {
		Options options = new Options();
		options.addOption("h", "help", true, "prints the help content");
		options.addOption(OptionBuilder.withArgName("range").withLongOpt("range").hasArg().isRequired()
				.withDescription("Range of ports to scan using format '[PortStart]-[PortEnd]' or '[SinglePort]'").create());
		options.addOption(OptionBuilder.withArgName("host").withLongOpt("host").hasArg().isRequired().withDescription("Target machine to scan").create());

		return options;
	}

	/**
	 * Method to test availability of a port.
	 * 
	 * @param host Target marchine.
	 * @param port Port to probe.
	 * @return Object representing port availability information.
	 * @throws IOException
	 */
	private PortInfos checkAvailability(String host, int port) throws IOException {
		PortInfos infos = null;

		/* Check input parameters */
		if ((host == null) || "".equalsIgnoreCase(host.trim())) {
			throw new IllegalArgumentException("Host cannot be null or empty !");
		}
		if ((port < 1) || (port > 65535)) {
			throw new IllegalArgumentException("Port number must be between 1 and 65535 !");
		}

		/* Test connection using RMI protocol */
		Connexion jmxConn = null;
		try {
			jmxConn = new Connexion(host, Integer.toString(port), null, null, false);
			if (jmxConn.isState()) {
				infos = new PortInfos(port, PortStatus.OPENED_WITHOUT_AUTHENTICATION);
			} else if (jmxConn.isNeed_creds()) {
				infos = new PortInfos(port, PortStatus.OPENED_WITH_AUTHENTICATION);
			} else {
				infos = new PortInfos(port, PortStatus.CLOSED);
			}
		}
		finally {
			if ((jmxConn != null) && (jmxConn.getConnector() != null)) {
				jmxConn.getConnector().close();
			}
		}

		return infos;
	}

}

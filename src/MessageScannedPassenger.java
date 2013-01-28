/**
 * immutable message sending a passenger and if the passenger passed or not
 * passenger should also be immutable
 * @author Ian Salitrynski
 *
 */
public class MessageScannedPassenger {
	private final Passenger p;
	private final boolean passed;
	
	/**
	 * Makes a new immutable message stating a passenger was scanned
	 * @param passenger scanned passenger
	 * @param scanPassed if they are not going to jail (true = no jail)
	 */
	public MessageScannedPassenger(Passenger passenger, boolean scanPassed) {
		p = passenger;
		passed = scanPassed;
	}
	
	/**
	 * Gets the passenger of the message
	 * @return
	 */
	public Passenger getPassenger() {
		return p;
	}

	/**
	 * Did the passenger pass the scan?
	 * inverse of toJail()
	 * @return
	 */
	public boolean isPassed() {
		return passed;
	}
	
	/**
	 * Does this passenger go to jail?
	 * inverse of isPassed()
	 * @return
	 */
	public boolean toJail() {
		return !passed;
	}
}

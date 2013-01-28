/**
 * immutable message sending a passenger
 * passenger should also be immutable
 * @author Ian Salitrynski
 *
 */
public class MessageSendPassenger {
	
	private final Passenger p;
	
	/**
	 * Makes a new immutable message stating with a passenger
	 * @param passenger to send on
	 */
	public MessageSendPassenger(Passenger passenger) {
		p = passenger;
	}
	
	/**
	 * Gets the passenger sent by the teller
	 * @return
	 */
	public Passenger getPassenger() {
		return p;
	}
}

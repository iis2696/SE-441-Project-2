import akka.actor.ActorRef;

/**
 * immutable message sending a passenger
 * passenger should also be immutable
 * @author Ian Salitrynski
 *
 */
public class MessageSendPassengerRespond {
	
	private final Passenger p;
	private final ActorRef r;
	
	/**
	 * Makes a new immutable message stating with a passenger
	 * @param passenger to send on
	 */
	public MessageSendPassengerRespond(Passenger passenger, ActorRef response) {
		p = passenger;
		r = response;
	}
	
	/**
	 * Gets the passenger sent by the teller
	 * @return
	 */
	public Passenger getPassenger() {
		return p;
	}
	
	
	/**
	 * Gets the responding reference
	 * @return
	 */
	public ActorRef getResponseActor() {
		return r;
	}
}

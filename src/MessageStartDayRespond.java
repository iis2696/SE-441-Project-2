import akka.actor.ActorRef;

/**
 * immutable message for start of day
 * @author Ian Salitrynski
 *
 */
public class MessageStartDayRespond {
	
	private final ActorRef r;
	
	/**
	 * Makes a new immutable message stating the start of the day
	 * Reciever should begin to start up
	 */
	public MessageStartDayRespond(ActorRef respond) {
		r = respond;
	}
	
	public ActorRef getResponseActor() {
		return r;
	}
}


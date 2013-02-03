import akka.actor.*;

/**
 * System driver class; not main class, and should
 * be initialized by main, but starts day, receives
 * passengers and sends them through TSA screening.
 * 
 * At end of day, stops all actors.
 * 
 * @author Jacob Siegel
 */
public class Driver extends UntypedActor{
	private final ActorRef DocCheck;
	private final int CYCLES;
	private final MessageStartDay START;
	private final MessageEndDay END;
	
	public Driver(ActorRef dc, int n) {
		DocCheck = dc;
		CYCLES = n;
		
		START = new MessageStartDay();
		END = new MessageEndDay();
		
		startDay();
		cyclePassengers();
	}
	
	/*
	 * Outbound MSG
	 * - Start Day
	 * - Passenger Arrival (SendPassenger)
	 * - End Day (after N cycles)
	 * 
	 * Inbound MSG
	 * - End Day
	 */
	
	@Override
	public void onReceive(Object m) throws Exception {
		if ( m instanceof MessageEndDay ) { // System finished
			context().system().shutdown(); // I think?
		} else {
			// do nothing, not supposed to receive other msg
		}
	}

	/**
	 * Simply tells the DocumentCheck actor that the day has
	 * begun.
	 */
	private void startDay() {
		DocCheck.tell(START);
		System.out.println("message about sending start to doc check");
	}
	
	/**
	 * Loops number of times as passed to Driver constructor,
	 * creating a new passenger and passenger message to send
	 * to the DocumentCheck actor.
	 * 
	 * When loop is completed, sends end-of-day message.
	 */
	private void cyclePassengers() {
		for(int i = 0 ; i < CYCLES; i++) {
			final MessageSendPassenger passM = new MessageSendPassenger(new Passenger());
			DocCheck.tell(passM);
		}
		DocCheck.tell(END);
	}
}

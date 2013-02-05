import java.util.ArrayList;

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
public class Driver {
	private final static int NUMSTATIONS = 6;
	private static ActorRef DocCheck;
	private final static int CYCLES = 20;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ActorRef jail = Actors.actorOf(new UntypedActorFactory() {public UntypedActor create() { return new Jail(NUMSTATIONS); }}).start();
		final ArrayList<ActorRef> secQueues = new ArrayList<ActorRef>();
		for (int i = 0; i < NUMSTATIONS; i++) {
			final int n = i;
			final ActorRef sec = Actors.actorOf(new UntypedActorFactory() {public UntypedActor create() { return new Security(n, jail); }}).start();
			final ActorRef bodyScan = Actors.actorOf(new UntypedActorFactory() {public UntypedActor create() { return new BodyScan(sec, n); }}).start();
			final ActorRef bagScan = Actors.actorOf(new UntypedActorFactory() {public UntypedActor create() { return new BagScan(sec, n); }}).start();
			final ActorRef secQueue = Actors.actorOf(new UntypedActorFactory() {public UntypedActor create() { return new SecurityQueue(bodyScan, bagScan, n); }}).start();
			secQueues.add(secQueue);
		}
		DocCheck = Actors.actorOf(new UntypedActorFactory() {public UntypedActor create() { return new DocumentCheck(secQueues); }}).start();
		startDay();
		cyclePassengers();
	}

	/**
	 * Simply tells the DocumentCheck actor that the day has
	 * begun.
	 */
	private static void startDay() {
		DocCheck.tell(new MessageStartDay());
		System.out.println("Driver sending start day message to doc check");
	}
	
	/**
	 * Loops number of times as passed to Driver constructor,
	 * creating a new passenger and passenger message to send
	 * to the DocumentCheck actor.
	 * 
	 * When loop is completed, sends end-of-day message.
	 */
	private static void cyclePassengers() {
		for(int i = 0 ; i < CYCLES; i++) {
			final MessageSendPassenger passM = new MessageSendPassenger(new Passenger());
			DocCheck.tell(passM);
			System.out.println("System Driver sending passenger " +
				(passM).getPassenger().getID() + " to Document Check.");
		}
		
		DocCheck.tell(new MessageEndDay());
		System.out.println("System Driver sending end-of-day to Document Check.");
	}
}

import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * Handles the results of the body and bag scans. If a passenger
 * fails one of the scans, they are sent to the jail.
 * 
 * @author Mike Lopez
 *
 */
public class Security extends UntypedActor{
	/*
	 * The results of passenger's scans.
	 */
	private final HashMap<Passenger, Boolean> scanResults;
	/*
	 * The ID of the security station.
	 */
	private final int ID;
	/*
	 * The Jail actor to send passengers who fail scans.
	 */
	private final ActorRef jail;
	/*
	 * Whether or not the body scan and bag scans are
	 * shut down. Count will equal 2 at end of day.
	 */
	private int scannerOffCount;
	
	/**
	 * Security Station constructor.
	 * 
	 * @param ID - The ID of the security station.
	 * @param jail - The jail actor to send passengers
	 * who fail the body and bag scans.
	 */
	public Security(int ID, ActorRef jail) {
		this.ID = ID;
		this.jail = jail;
		this.scanResults = new HashMap<Passenger, Boolean>();
		scannerOffCount = 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
	 */
	@Override
	public void onReceive(Object m) throws Exception {
		if(m instanceof MessageScannedPassenger){
			MessageScannedPassenger msp = (MessageScannedPassenger) m;
			
			System.out.println("Security Station " + ID 
					+ " receives scan complete message with passenger "
					+ msp.getPassenger().getID() + ".");
			
			Boolean result = scanResults.get(msp.getClass());
			//Need to check if we have already received
			//the passenger's results from the other scan.
			if(result == null) {
				scanResults.put(msp.getPassenger(), msp.isPassed());
			} else {
				if(!result || !msp.isPassed()) {
					MessageSendPassenger mp =
						new MessageSendPassenger(msp.getPassenger());
					System.out.println("Security Station " + ID 
							+ " send passenger " + msp.getPassenger().getID()
							+ " to the Jail.");
					jail.tell(mp);
				} else {
					System.out.println("Passenger " + msp.getPassenger().getID() +
							" at Security Station " + ID + " has left the security area.");
				}
			}
		} else if(m instanceof MessageEndDay) {
			System.out.println("Security Station " + ID 
					+ " receives end of day message.");
			scannerOffCount++;
			if(scannerOffCount == 2) {
				System.out.println("Security Station " + ID +
						" is shutting down.");
				context().stop();
			}
		}
		
	}

}

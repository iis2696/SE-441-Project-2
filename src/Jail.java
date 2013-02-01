import java.util.ArrayList;

import akka.actor.UntypedActor;

/**
 * Receives passengers from security stations.
 * Transports passengers to detention center
 * at end of day.
 * 
 * @author Mike Lopez
 *
 */
public class Jail extends UntypedActor{
	/*
	 * The number of security stations in the system.
	 */
	private int numberOfSecurityStations;
	/*
	 * The list of passengers who have been jailed. 
	 */
	private ArrayList<Passenger> jailedPassengers;
	
	/**
	 * Jail constructor.
	 * 
	 * @param numberOfSecurityStations - The number of security
	 * stations in the system.
	 */
	public Jail(int numberOfSecurityStations) {
		this.numberOfSecurityStations = numberOfSecurityStations;
	}
	
	/*
	 * (non-Javadoc)
	 * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
	 */
	@Override
	public void onReceive(Object m) throws Exception {
		if(m instanceof MessageSendPassenger) {
			MessageSendPassenger mp = (MessageSendPassenger) m;
			
			System.out.println("Passenger " + mp.getPassenger()
					+ " has arrived at the Jail.");
			
			jailedPassengers.add(mp.getPassenger());
		} else if(m instanceof MessageEndDay) {
			System.out.println("Jail received end of day message.");
			
			numberOfSecurityStations--;
			
			if(numberOfSecurityStations == 0) {
				for(Passenger p : jailedPassengers) {
					System.out.println("Passenger " + p.getID()
							+ " was sent to detention center.");
				}
				System.out.println("The Jail is shutting down.");
				context().stop();
			}
		}
	}

}

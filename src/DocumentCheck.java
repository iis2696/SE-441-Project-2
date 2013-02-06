import akka.actor.UntypedActor;
import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.Random;

/**
 * Sends passnegers on to a security queue or denies them passage
 * @author Ian Salitrynski
 *
 */
public class DocumentCheck extends UntypedActor{
	
	/**
	 * Current queue to add to
	 */
	private int cur = 0;
	
	/**
	 * Array of Queues that passengers are sent to
	 */
	private ArrayList<ActorRef> queues = new ArrayList<ActorRef>();
	
	/**
	 * Randomly turns passengers away
	 */
	private Random r = new Random();
	
	public DocumentCheck(ArrayList<ActorRef> secQueues) {
		queues = secQueues;
	}
	
	@Override
	public void onReceive(Object m) throws Exception {
		
		if (m instanceof MessageStartDay) {
			
			for (int i = 0; i < queues.size(); i++) {
				queues.get(i).tell(m);	// Send start day message
			}
			
		} else if (m instanceof MessageEndDay) {
			
			for (int i = 0; i < queues.size(); i++) {
				queues.get(i).tell(m);	// Send end day message
			}
			
			context().stop();
			
		} else if (m instanceof MessageSendPassenger) {
			// Recieved passenger
			System.out.println("Document Check recieved passenger " + ((MessageSendPassenger) m).getPassenger().getID() + ".");
			
			if (r.nextDouble() > 0.2) {
				// Passenger can go on, so send them to next queue
				MessageSendPassenger msg = new MessageSendPassenger(((MessageSendPassenger) m).getPassenger());
				queues.get(cur).tell(msg);
				System.out.println("Document Check sending passenger " + ((MessageSendPassenger) m).getPassenger().getID() + " to Security Queue " + cur + ".");
				cur = ((cur + 1)  % queues.size());
				
			} else {
				// Passenger denied passage 
				System.out.println("Document Check sending passenger " + ((MessageSendPassenger) m).getPassenger().getID() + " away.");
			}
		}
		
	}

}

import akka.actor.UntypedActor;
import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.Random;

public class DocumentCheck extends UntypedActor{
	
	private int cur = 0;
	private ArrayList<ActorRef> queues = new ArrayList<ActorRef>();
	private Random r = new Random();
	
	@Override
	public void onReceive(Object m) throws Exception {
		
		if (m instanceof MessageStartDay) {
			// TODO: Initialize queues array list
			
			for (int i = 0; i < queues.size(); i++) {
				queues.get(i).tell(m);	// Send start day message
			}
		} else if (m instanceof MessageEndDay) {
			
			for (int i = 0; i < queues.size(); i++) {
				queues.get(i).tell(m);	// Send end day message
			}
		} else if (m instanceof MessageSendPassenger) {
			System.out.println("Document Check recieved passenger " + ((MessageSendPassenger) m).getPassenger().getID());
			if (r.nextDouble() > 0.2) {
				MessageSendPassenger msg = new MessageSendPassenger(((MessageSendPassenger) m).getPassenger());
				queues.get(cur).tell(msg);
				System.out.println("Document Check sending passenger" + ((MessageSendPassenger) m).getPassenger().getID() + " to Security Queue " + cur + ".");
				cur++;
			} else {
				System.out.println("Document Check sending passenger" + ((MessageSendPassenger) m).getPassenger().getID() + " away.");
			}
		}
		
	}

}

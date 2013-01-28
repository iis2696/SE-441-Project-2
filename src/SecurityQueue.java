import java.util.LinkedList;
import java.util.Queue;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class SecurityQueue extends UntypedActor{
	private final ActorRef bodyScan;
	private final ActorRef bagScan;	
	private Queue<Passenger> passengers = new LinkedList<Passenger>();
	
	@Override
	public void onReceive(Object m) throws Exception {
		if(m instanceof MessageStartDay) {
			bodyScan.tell(m);
			bagScan.tell(m);
		} else if (m instanceof MessageEndDay) {
			bodyScan.tell(m);
			bagScan.tell(m);
		} else if (m instanceof MessageSendPassenger) {
			passengers.add(((MessageSendPassenger) m).getPassenger());
			if (!passengers.isEmpty()) {
				Passenger p = passengers.poll();
				MessageSendPassenger msg = new MessageSendPassenger(p);
			}
		}
		
	}

}

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class BodyScan extends UntypedActor{

	private final ActorRef security;
	private final ActorRef securityQueue;	
	private final int ID;
	
	public BodyScan(ActorRef sec, ActorRef secQueue, int id) {
		security = sec;
		securityQueue = secQueue;
		ID = id;
	}
	
	@Override
	public void onReceive(Object arg0) throws Exception {

		// TODO Auto-generated method stub
		
	}

}

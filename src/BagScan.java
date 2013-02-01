import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class BagScan extends UntypedActor{
	private final ActorRef security;
	private final int ID;
	
	public BagScan(ActorRef sec, int id) {
		security = sec;
		ID = id;
	}
	
	@Override
	public void onReceive(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

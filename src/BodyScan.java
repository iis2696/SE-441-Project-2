import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class BodyScan extends UntypedActor{

	private final ActorRef security;
	private final ActorRef securityQueue;	
	private final int ID;
	private Random r;
	private final int failPercentage = 20;
	private final int failMax = 100;
	
	public BodyScan(ActorRef sec, ActorRef secQueue, int id) {
		security = sec;
		securityQueue = secQueue;
		ID = id;
		r = new Random();
	}
	
	@Override
	public void onReceive(Object m) throws Exception {
		if(m instanceof MessageStartDay){
			System.out.println("BodyScan " + ID + " received start day message.");
			securityQueue.tell(new MessageReady());
		} else if(m instanceof MessageEndDay){
			System.out.println("BodyScan " + ID + " received end day message.");
			System.out.println("BodyScan " + ID + " sending end day message to Security.");
			
			security.tell(m);
		} else if(m instanceof MessageSendPassenger){
			System.out.println("BodyScan " + ID + " receives passenger " + ((MessageSendPassenger) m).getPassenger().getID());
			if(r.nextInt(failMax)>=failPercentage){
				System.out.println("BodyScan " + ID + "fails the inspection of " + ((MessageSendPassenger) m).getPassenger());
				MessageScannedPassenger msp = new MessageScannedPassenger(((MessageSendPassenger) m).getPassenger(), false);
				System.out.println("BodyScan " + ID + "sending " + ((MessageSendPassenger) m).getPassenger() + " to Security");
				security.tell(msp);
			}else{
				System.out.println("BodyScan " + ID + "passes the inspection of " + ((MessageSendPassenger) m).getPassenger());
				MessageScannedPassenger msp = new MessageScannedPassenger(((MessageSendPassenger) m).getPassenger(), true);
				System.out.println("BodyScan " + ID + "sending " + ((MessageSendPassenger) m).getPassenger() + " to Security");
				security.tell(msp);
			}
			securityQueue.tell(new MessageReady());
		}
	}

}

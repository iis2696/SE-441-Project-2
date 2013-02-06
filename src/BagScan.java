import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class BagScan extends UntypedActor{
	private final ActorRef security;
	private final int ID;
	private Random r;
	private final int failPercentage = 20;
	private final int failMax = 100;
	
	public BagScan(ActorRef sec, int id) {
		security = sec;
		ID = id;
		r = new Random();
	}
	
	@Override
	public void onReceive(Object m) throws Exception {
		if(m instanceof MessageStartDay){
			System.out.println("BagScan " + ID + " received start day message.");
		} else if(m instanceof MessageEndDay){
			System.out.println("BagScan " + ID + " received end day message.");
			System.out.println("BagScan " + ID + " sending end day message to Security.");
			
			security.tell(m);
			context().stop();
			
		} else if(m instanceof MessageSendPassenger){
			System.out.println("BagScan " + ID + " receives passenger " + ((MessageSendPassenger) m).getPassenger().getID());
			if(r.nextInt(failMax)>=failPercentage){
				System.out.println("BagScan " + ID + " fails the inspection of " + ((MessageSendPassenger) m).getPassenger().getID());
				MessageScannedPassenger msp = new MessageScannedPassenger(((MessageSendPassenger) m).getPassenger(), false);
				System.out.println("BodyScan " + ID + " sending " + ((MessageSendPassenger) m).getPassenger().getID() + " to Security");
				security.tell(msp);
			}else{
				System.out.println("BagScan " + ID + " passes the inspection of " + ((MessageSendPassenger) m).getPassenger().getID());
				MessageScannedPassenger msp = new MessageScannedPassenger(((MessageSendPassenger) m).getPassenger(), true);
				System.out.println("BodyScan " + ID + " sending " + ((MessageSendPassenger) m).getPassenger().getID() + " to Security");
				security.tell(msp);
			}
		}
	}

}

import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class BodyScan extends UntypedActor{

	private final ActorRef security;
	private final int ID;
	private Random r;
	private final int failPercentage = 20;
	private final int failMax = 100;
	
	public BodyScan(ActorRef sec, int id) {
		security = sec;
		ID = id;
		r = new Random();
	}
	
	@Override
	public void onReceive(Object m) throws Exception {
		if(m instanceof MessageStartDayRespond){
			System.out.println("BodyScan " + ID + " received start day message.");
			((MessageStartDayRespond) m).getResponseActor().tell(new MessageReady());
		} else if(m instanceof MessageEndDay){
			System.out.println("BodyScan " + ID + " received end day message.");
			System.out.println("BodyScan " + ID + " sending end day message to Security.");
			
			security.tell(m);
			context().stop();
		} else if(m instanceof MessageSendPassengerRespond){
			System.out.println("BodyScan " + ID + " receives passenger " + ((MessageSendPassengerRespond) m).getPassenger().getID());
			if (r.nextInt(failMax)>=failPercentage) {
				System.out.println("BodyScan " + ID + " fails the inspection of " + ((MessageSendPassengerRespond) m).getPassenger().getID());
				MessageScannedPassenger msp = new MessageScannedPassenger(((MessageSendPassengerRespond) m).getPassenger(), false);
				System.out.println("BodyScan " + ID + " sending " + ((MessageSendPassengerRespond) m).getPassenger().getID() + " to Security");
				security.tell(msp);
			} else {
				System.out.println("BodyScan " + ID + " passes the inspection of " + ((MessageSendPassengerRespond) m).getPassenger().getID());
				MessageScannedPassenger msp = new MessageScannedPassenger(((MessageSendPassengerRespond) m).getPassenger(), true);
				System.out.println("BodyScan " + ID + " sending " + ((MessageSendPassengerRespond) m).getPassenger().getID() + " to Security");
				security.tell(msp);
			}
			((MessageSendPassengerRespond) m).getResponseActor().tell(new MessageReady());
		}
	}

}

import java.util.concurrent.*;

public class Lab7 {

	public Semaphore waitingRoom;
	public Semaphore haircutChair;
	private int numberCustomers = 25;
	
	public static void main( String[] args) {
		new Lab7();
	}
	
	public Lab7() {
		waitingRoom = new Semaphore( 10, true );
		haircutChair = new Semaphore( 1, true );
		
		ExecutorService e = Executors.newCachedThreadPool();
		e.execute( new Barber() );
		
		for (int i = 0; i < numberCustomers; i++ ) {
			e.execute( new Customer() );
		}
	}
}
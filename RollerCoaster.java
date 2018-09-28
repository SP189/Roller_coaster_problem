package myProj;
import java.util.*;
import java.util.concurrent.*;

class Passenger extends Thread
{
	int id;
	boolean notRode=true;
	
	public Passenger(int i)//every passenger has a different id
	{
		id=i;
	}
	
	public void run()
	{
		while(notRode)//as long as the passenger does not finish riding
		{
			try
			{
				
				RollerCoaster.accessSeats.acquire();//the passenger tries to get a seat
				if(RollerCoaster.numberOfFreeSeats > 0 && System.currentTimeMillis()- RollerCoaster.startTime<=RollerCoaster.warning)
				//if seats are available -> passenger is ready to ride the car
				{
					System.out.println("Passenger "+this.id+" is ready to take ride");
					RollerCoaster.numberOfFreeSeats--;//passenger occupies one seat
					
					RollerCoaster.passengers.release();//car is notified
					RollerCoaster.accessSeats.release();//unlock the booked seats
					try
					{
						RollerCoaster.car.acquire();//passenger can now take a ride
						notRode=false;
						this.ride();//passenger is riding the car
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
				}
				else//if seats are unavailable, the passenger goes off
				{
					System.out.println("No car is availabe for passenger "+this.id);
					System.out.println("Passenger "+this.id+" leaves");
					RollerCoaster.accessSeats.release();//unlock the seats
					notRode=false;//passrnger has left
				}
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	public void ride()
	{
		System.out.println("Passenger "+id +" is taking a ride");
		try {
			sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class Car extends Thread
{
	public void run()
	{
		while(true)//passengers can come anytime
		{
			if(System.currentTimeMillis()- RollerCoaster.startTime>RollerCoaster.stopTime)
			//if the park is about to close, dont run the car
			{
				System.out.println("Park is closed");
				System.exit(0);
			}
			else
			{
				try
				{
					RollerCoaster.passengers.acquire();//one passenger is acquired
					RollerCoaster.accessSeats.release();
					RollerCoaster.numberOfFreeSeats++;//increase the available seats by one as one passenger has gone for ride
					RollerCoaster.car.release();//car is ready to go!!
					RollerCoaster.accessSeats.release();//
					this.ride();//car went for ride
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
			}
		}
	}
	
	
	public void ride()
	{
		System.out.println("Car is riding");
		try {
			sleep(RollerCoaster.rideTime);//sleep until the car is riding -> ride's time
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

public class RollerCoaster extends Thread
{
	//since initially there are no passengers -> 0
	// car is also not in use -> 0
	//seats are available -> 1
	public static Semaphore passengers = new Semaphore(0);
    public static Semaphore car = new Semaphore(0);
    public static Semaphore accessSeats = new Semaphore(1);
    
    public static final int seats = 5;//maximum passengers waiting for ride
    public static int numPassengers;
    public static int numberOfFreeSeats = seats;
    
    public static long startTime = System.currentTimeMillis();
    public static long stopTime = 50000l;//park closing time
    public static long warning = 49000l;//warn when park is about to close
    public static final int rideTime = 5000;
    
    public static void main(String args[])
    {
    	System.out.println("Enter the expected number of passengers");
    	Scanner scn=new Scanner(System.in);    	
    	numPassengers=scn.nextInt();
    	
    	RollerCoaster park=new RollerCoaster();
    	park.start();
    	
    }
    
    public void run()
    {
    	Car c=new Car();//create a new car
    	c.start();
    	
    	for(int i=0;i<numPassengers;i++)
    	{
    		Passenger p=new Passenger(i+1);//new passenger comes
    		p.start();
    		try {
				sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    }
}

/*
OUTPUT:
CASE 1:
Enter the expected number of passengers
20
Passenger 1 is ready to take ride
Car is riding
Passenger 1 is taking a ride
Passenger 2 is ready to take ride
Passenger 3 is ready to take ride
Car is riding
Passenger 2 is taking a ride
Passenger 4 is ready to take ride
Passenger 5 is ready to take ride
Car is riding
Passenger 3 is taking a ride
Passenger 6 is ready to take ride
Passenger 7 is ready to take ride
Passenger 8 is ready to take ride
Car is riding
Passenger 4 is taking a ride
Passenger 9 is ready to take ride
No car is availabe for passenger 10
Passenger 10 leaves
Car is riding
Passenger 5 is taking a ride
Passenger 11 is ready to take ride
No car is availabe for passenger 12
Passenger 12 leaves
No car is availabe for passenger 13
Passenger 13 leaves
Car is riding
Passenger 6 is taking a ride
Passenger 14 is ready to take ride
No car is availabe for passenger 15
Passenger 15 leaves
Car is riding
Passenger 7 is taking a ride
Passenger 16 is ready to take ride
No car is availabe for passenger 17
Passenger 17 leaves
No car is availabe for passenger 18
Passenger 18 leaves
Car is riding
Passenger 8 is taking a ride
Passenger 19 is ready to take ride
No car is availabe for passenger 20
Passenger 20 leaves
Car is riding
Passenger 9 is taking a ride
Car is riding
Passenger 11 is taking a ride
Park is closed

CASE 2:
Enter the expected number of passengers
5
Passenger 1 is ready to take ride
Car is riding
Passenger 1 is taking a ride
Passenger 2 is ready to take ride
Passenger 3 is ready to take ride
Car is riding
Passenger 2 is taking a ride
Passenger 4 is ready to take ride
Passenger 5 is ready to take ride
Car is riding
Passenger 3 is taking a ride
Car is riding
Passenger 4 is taking a ride
Car is riding
Passenger 5 is taking a ride

*/
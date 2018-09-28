# Roller_coaster_problem
Operating System - Roller coaster problem using semaphore.
<br>
PROBLEM DEFINITION: 
Suppose there are n passenger threads and a car thread. The passengers repeatedly wait to take rides in the car, which can hold C passengers, where C < n. The car can go around the tracks only when it is full. 
Here are some additional details: 
• Passengers should notify before boarding and unboarding. 
• The car should invoke load, run and unload. 
• Passengers cannot board until the car has invoked load 
• The car cannot depart until C passengers have boarded. 
• Passengers cannot unboard until the car has invoked unload
<br>
PROBLEM DESCRIPTION :
We accept the expected number of passengers from the user. Also, we set the semaphore value of passengers and cars to 0 as initially there are no passengers present and the car is also not in use. But as the seats are available, we set the semaphore value to 1. We define the maximum number of passengers that can wait for a ride. Every passenger is given a unique id. Two classes of passengers and cars are created which extend the Thread class. The main() creates C threads of passenger and n threads of car.
When a passenger requests for a ride, he tries to acquire a seat by locking it. If the seats are available, he occupies one seat by decreasing the freeSeat count by one. Then the seats are unlocked. Now, passenger is ready to take a ride. So he will lock (acquire) the car and go for the ride. But if there are no free seats available, he goes away. The passenger is allowed to book a seat if the park is not about to close. If so, he goes back home.
Now, when a car is requested, it locks the passenger (passenger.acquire()) and then increases the freeSeat count by one. The car goes on the ride and hence it is unlocked (released). The car goes for the ride for a specific amount of time which is equal to the rideTime. The car stops running when the park is closing. Until then, passengers are allowed to request for a ride. 

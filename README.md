# Simulation of Human Population

Program that simulates the growth of a given population within a given time span.
&nbsp;
&nbsp;
&nbsp;  

## About  


One of the projects for our data structures class. The program uses the Gompertz-Makeham
distribution to determine an average lifespan for humans. As for mating, it uses a method that generates a time
following the exponential law. At the heart of it, the program consists of 2 priority queues (binary heaps).

One priority queue keeps track of the live population at the moment, with each node representing a living person.
It is a min heap ordered by the time of death. The next person to die in the simulation is at the root of the heap.

The second priority queue, also a min heap, keeps track of events in the simulation. There are 
3 types of events: BIRTH, MATING and DEATH. All known events are stored in the heap, with the soonest 
event to occur at the root. 

&nbsp;

Essentially, the program deals with events until there are no more of them. Since some events generate other events,
 the size of the event heap isn't necessarily decreasing. Each type of event can affect the data structures
of the simulation and the program handles that.

For example, dealing with a DEATH event implies 
deleting the root of the live population priority queue. Dealing with a MATING event implies
having to add a BIRTH event to the event heap and registering the mates as partners. And so on and so forth.
 (Some events have a p probability of occuring... etc).
 
 &nbsp;
 

The simulation keeps going until either the MaxTime is reached or until the population becomes extinct.  

The graph also shows the lineage coalascence of males and females. Starting from the live population at the
MaxTime, the program goes back in time until it reaches the beginning of the simulation. It then shows 
the number of coalescence lines for each gender. (In order to do this, 2 additional priority
queues are used).

&nbsp;
  

## Running the program  


1.Download a zipped folder of the project.  
2.Extract the folder and double click on the .jar file.

&nbsp;

## Demo

Click on the following image to be redirected to the youtube video.

[![Demo of the app. Click on this to be redirected](https://i.imgur.com/RRBGPC7.jpg)](https://www.youtube.com/watch?v=Ibk1qdeXhBY)
  
  
&nbsp;  
&nbsp;  
&nbsp;   

#### Authors



Mohamed Elayat  
Fatima Mostefai

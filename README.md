# ProjInda
--- 
Project for ProjInda24
### Project Description
---
This project is about creating a traffic simulation for an intersection. Here there are roads from 4 different directions with one lane in each direction where cars can come and go to each lane. You should be able to control variables about how many cars come from which direction and analyze how the traffic is affected. In addition, we implemented different algorithms for how the traffic lights connected to the intersection would work so that we could see how their logic could affect traffic under different conditions.

This project shows the simulation graphically using Java Swing. This helps with drawing cars and creating elements that you can interact with. To run the project, you need to make sure that Java Swing is working on your system. You can do this by following this guide, Option 1 (https://phoenixnap.com/kb/install-java-ubuntu), then you can run the project from the terminal in ubuntu. Option 2, run the project via vscode with the necessary Java Extensions downloaded.

To make the simulation run while you want to change things, we have used Java Runnable. This import allows us to execute different functions in parallel with each other. So it has helped to ensure that you can run the simulation and change variables at the same time.

### Trafic logic
At the control panel there are radio buttons for which type of logic the traffic lights should use. There are three options here and it is automatically set to the first one.
1. The first logic is a random choice of which traffic light will show green once at a time. This is very inefficient and can lead to many cars having to wait for a long time.
2. The second logic involves having two traffic lights in opposite directions on at the same time. Then it is alternated which two traffic lights should be on all the time. This works much more efficiently but can be worse if all traffic comes from one direction as queues can form.
3. The third logic is based on the size of the queues in each direction, from there the two traffic lights with opposite directions are set to start with cars at the same time. This ensures that large queues never form, but can cause individual cars in a lane to wait indefinitely.

### Trafic situations
Traffic situations are provided to demonstrate in which contexts the logic can work well. If no logic is activated, cars are sent in randomly from different directions.
1. The first situation is directly linked to logic 2 as it shows a situation where logic 1 had had major problems. Here cars are sent in from all directions at the same time.
2. The second situation is more connected to showing a situation where logic 3 is required instead of logic 2. Here cars mainly come from the east and west, this leads to the fact that in principle you only want it to be green at the east and west and not for the north and south because queues are created otherwise.

### Creators
---
This project was created by:      <br>
Adam Carlström                  <br>
Arvid Willhemlsson              <br>

### How the work has been divided
---
To make the work more efficient, the work has been divided between us. Here, Adam has mainly worked with the graphic components to ensure that the traffic situation itself is displayed correctly. Arvid has mainly worked with the traffic logic and how the traffic lights should work and in which cases cars are allowed to drive.

# Ghostbuster

An HMM Lab Exercise (Catching the Ghost): Problem Description


In these Corona days, an invisible ghost takes shelter in your room. You are also passing good
time with him/her during day time. Since the ghost is creating problems at night time by making
weird noises, you prefer to catch him/her and put it in a bottle for the whole night. However, it is
not easy to locate the ghost since it is invisible and the ghost is moving randomly in your room.
Assume that the ghost has a map of your room that can be considered as a grid of size 9x9.
He/She can randomly move one position in any of the four directions (up, down, left, right) with
equal probabilities (e.g., 0.24). He/She may also move in a random direction (diagonal one
position) or stay in place with small probabilities. You have a sensor that can sense how far the
ghost is from a given position of the sensor (using Manhattan distance). Based on the distance
of the sensor from the ghost, the sensor shows three colors: red if it is close by, orange if it is at
medium distance, and green if it is far away.


To model the above problem using HMM, you have to define initial probability distribution P(X1),
transition probability distribution, P(Xt|Xt-1) and emission probability distribution P(Rij|Xt). You
can consider P(X1) as uniform distribution, P(Xt|Xt-1) as the transition probability distribution
due to the mobility of the ghost, and P(Rij|Xt) as the probability distribution of sensing
red/orange/green of different cells given the host at a position of the grid.


A) Design a system that updates the probabilities of the ghost being in a grid cell as the
time elapses and a sensor measurement observed. To advance a time to the next time
stamp, you will press a button and see the change of probabilities in the grid. Then when
you press on a cell to get the sensor measurement it will display the color of the grid cell
based on the distance to the ghost, and update the probability distribution of all grid cells
accordingly. Finally when you are almost sure about the ghost location (the cell with the
highest probability, you can hit the cell to catch the ghost. Use the HMM filtering
technique to do the task.


B) Do the above task using Particle filtering

---
author:
- 'Carlos Bergillos, Roger Vilaseca, Adrià Cabeza'
title: |
    **Sharing cars to get to work:\
    A Local Search approach**\
    Artificial Intelligence
---


  
Introduction
============

The objective of this assignment is to learn different problem-solving
techniques based on Local Search using the AIMA library in Java. In
particular, the Hill Climbing and Simulated Annealing.

After implementing states and operators for the algorithms, we have to
do the second objective, which is comparing the different results
obtained with both algorithms. Making different experiments and
extracting results.

![Visual representation of a moment of the state where the car is
picking up and dropping off
people.[]{data-label="grid"}](images/carsMoving.png)

Description of the problem
==========================

For this assignment we are assuming a car sharing system where all the
users are sharing the car. We have $N$ people and $M$ drivers, which is
a subset of $N$.

Our city is a $10\times10\ km$ square and each street is disposed every
$100\ m$ (horizontally and vertically). This disposition creates a grid
of $100\times100\ blocs$ with each block of $100\times100\ m$.

![Grid representing the city map. Each point represents a pickup or drop
off location.[]{data-label="grid"}](images/grid.png)

For each person in the set N we will have two points of the grid:

1.  Point $i$: Is the origin place for each user (Home).

2.  Point $j$: Is the destination place for each user (Work).

The calculation of the distance between two points of the city will be
made using the Manhattan function:

$$d(i,j) = \mid i_x - j_x\mid+ \mid i_y - j_y\mid$$ where $i_x$ and
$i_y$ are the coordinates $x$ and $y$ of the $i$ point in the grid.

Each user of the service will leave home at 7am and has to arrive work
before 8am and the highest speed in the city is $30\ km/h$. With the
previous restrictions, supposing that the speed always is $30\ km/h$ and
supposing that picking up and dropping off people don’t have
penalization time, we can do the following reasoning.

$$dist = speed * time = 30\ km/h * 1\ h = 30 km$$ So each driver at most
can drive $30\ km$.

Everyone who drives a car will be the first to leave and the last ones
to arrive.

Also, in each car can be at most three people at the same time
(including the driver).

We will have two criteria in order to evaluate the quality of the
solution:

-   Minimize the distance traveled for each driver.

-   Minimize the distance traveled for each driver and minimize the
    number of drivers.

Representation of a problem state
=================================

We need a way to represent a state of the problem that is small in
memory, but at the same time easy and quick to work with. We want to
avoid storing useless or redundant information, unless we clearly think
that that information will help us reduce the computation time. Also, we
want to be able to represent all the possible states that could exist.

Our states contain the following information:

-   Which users act as drivers and are providing their cars.

-   For each driver, which users (passengers) the driver needs to pick
    up and drop off, if any.

-   The order in which a driver needs to pick up and drop off its
    assigned passengers.

-   The total distance each car will be traveling for its route.

All users (both drivers and non drivers) have a unique identifying
number (id), taken from the position they occupy in the users list. So
in our state representation we will be using these identifiers to refer
to users.

We consider that the order in which a car picks up and drops its
passengers is important. For example, if driver A is assigned passengers
B and C, there are four possible routes after leaving its home, and
before arriving at work:

-   Pick up B, pick up C, drop off B, drop off C.

-   Pick up B, drop off B, pick up C, drop off C.

-   Pick up C, pick up B, drop off C, drop off B.

-   Pick up C, drop off C, pick up B, drop off B.

These four options will result in different routes with potentially
different total distances.

For this reason, we decided to use an ordered list to describe which
passengers a driver needs to take care of, and the specific route they
will use in the process.

Specifically, for each driver, we use a Java ArrayList listing all the
sequential pick-ups and drop offs of users that have been assigned,
describing unambiguously a route that the driver needs to follow.

Continuing with the previous example, if driver A has id 1, and
passengers B and C have ids 2 and 3 respectively, the first route
proposed before will be stored like this: $$[1, 2, 3, 2, 3, 1]$$

In a different state, maybe the second proposed route is used, in that
case the list would look like this: $$[1, 2, 2, 3, 3, 1]$$

Note how each id in the list always needs to appear twice, implicitly
the first time it appears indicates that that user is being picked up,
and the second one indicates that the user is being dropped off.

Also note how the driver of the car is always the first and last element
of the list, to represent the fact that the route must start at the
driver’s home, and end at the driver’s workplace.

Because a state can describe the presence of many different cars (each
one with its own driver, passengers, and route) we use an ArrayList to
contain all the cars lists described before. Thus, we end up with the
following data structure:

    lstinlinelstinline
    ArrayList<ArrayList<Integer>> assignments;

Apart from this, we also decided to use an extra data structure that
contains the total route distances for each car:

    ArrayList<Integer> distances;

Where the distance for the route in *assignments\[i\]* is stored in
*distances\[i\]*.

Although not strictly necessary (because this could be computed at run
time from other existing data), this *distances* variable will help us
calculate the heuristic value of the state quicker. We just make sure to
update these values any time *assignments* is changed.

The creation of this problem, will generate a very big amount of states.
With some calculus we can arrive to this solution. Assuming that there
are N people in total and M drivers, we can know that the problem can
generate different states with two reasons: the number of possibilities
that has every person to be in a different car and the order of each
car. To compute the solution we calculate a lower bound supposing only
the probability for being in each car. $$\Omega = M^N$$ Afterwards to
compute an upper bound we can add the probability for being in each
place of the car. $$O = M^N*(2N)!$$ So if we consider our total number
of states X, we can confirm: $$M^N < X < M^N*(2N)!$$


Generating an initial solution
==============================

We have to make a representation of the initial state, which at the same
time is a solution state. We have implemented four different ways to
generate our initial state: (The following examples will be done with 4
people Pi (excluding drivers), 2 drivers Di and each List will represent
a car. The first time that a person appears in a List is when it is
picked up and the second one when it is dropped out)

1.  Assigning in a balanced way all the people that are not drivers in
    all cars, each car has the same number of people. Each person that
    isn’t a driver will be picked up and dropped of consecutively.
    $$[D1,P1,P1,P2,P2,D1]$$ $$[D2,P3,P3,P4,P4,D2]$$

2.  Assigning in a balanced way all the people that are not drivers in
    all cars, each car has the same number of people. Each car will pick
    up all the people and then will drop them in the inverse order.
    $$[D1,P1,P2,P2,P1,D1]$$ $$[D2,P3,P4,P4,P3,D2]$$ If the number of
    people that are not a driver is more than 2 times the number of
    drivers, this initial state will not be a goal state because the
    number of people in the same time in a car will be greater than 3.

3.  Taking all the people that are not drivers inside the first car’s
    list. Each person that isn’t a driver will be picked up and dropped
    of consecutively. $$[D1,P1,P1,P2,P2,P3,P3,P4,P4,D1]$$ $$[D2,D2]$$
    Because everybody that is not a driver is in the first car it is
    very probable that the distance traveled by the first car will be
    more than $30\ Km$ and that initial state won’t be a goal state.

4.  Taking all the people that are not drivers inside the first car’s
    list. Each car will pick up all the people and then will drop them
    in the inverse order. $$[D1,P1,P2,P3,P4,P4,P3,P2,P1,D1]$$
    $$[D2,D2]$$ If the number of people that are not a driver is more
    than 2, this initial state will not be a goal state because the
    number of people in the first car will be greater than 3. Although,
    because everybody that is not a driver is in the first car it is
    very probable that the distance traveled by the first car will be
    more than $30\ Km$ and also, that initial state won’t be a goal
    state.

Analysis of our operators
=========================

Once we have defined the structure we will work with, we have to decide
which operators will modify our structure to move from one state to
another. To do this, we must take into account several factors, so that
when executing our algorithm, the best possible solution is found in a
fairly reasonable execution time.\
Our operators indicate all the possible paths that can be taken given
any state. Then we will use all these possibilities with the objective
that this becomes a state with some favorable characteristics. This is
called a branch factor and it changes the way it is applied depending on
which algorithm we are using. In the **Hill Climbing**, we generate all
the successors and the heuristic decides if it is good enough to stay
with him, in the **Simulated Annealing** we generate a successor status
in a random way and the heuristic decides if it is good enough to stay
with him.\

It is really important to cover all the space of solutions with our
operators because if we are not doing it, there may exist solutions that
will be lost, which could prevent us from reaching an optimal solution.
Also we be cautious about creating repeated solutions because our
execution time would be affected.\
At the beginning of everything we made a brainstorming session with all
the operators we could think of, which we believed that could be useful
and serve for something: swap the order of the people inside a car, swap
outside people between cars, deleting cars, moving a person into another
car, etc...

Finally, we decided to implement these 3 operators, which we think they
would reach to all the possible solutions:

1.  **Swap inside**: this operator lets us swap the order of the people
    inside a car $i$, taking two indices from the route list of the car,
    and swapping them (independently of them being pick-ups or drop
    offs). The branching factor for this operator is $c \cdot r^2$, with
    $c$ being the number of cars and $r$ being the route list size for a
    particular car (2 times the number of passengers it is carrying).

2.  **Move**: this operator lets us move any person that is not a driver
    from the car $i$ to another car $j$, in a pickup place $k$ and a
    drop off place $k+1$. The branching factor for this operator is
    $c^2 \cdot r_1 \cdot r_2$, with $c$ being the number of cars and
    $r_1$ and $r_2$ being the route list size for two of the cars.

3.  **Delete car**: this operator lets us delete a car whenever the car
    is only occupied by the driver. When the deletion is made the driver
    is inserted into another car. The branching factor for this operator
    is $c_1 \cdot c_2 \cdot r_1$, with $c_1$ being the number of cars
    where the driver is the only occupant of the vehicle, $c_2$ being
    the number of cars with at least one passenger, and $r_1$ being the
    route list size of a particular car in $c_2$.

Generating successor states
===========================

The way we generate all the successor states really varies depending on
the algorithm:

-   **Hill Climbing**: we create a list with all the possible states
    that can be found from the given one. To do it, we create all the
    possible states that can be created using our operators, previously
    explained in section above. Then Hill Climbing will use our
    Heuristic Function to choose the best successor.

-   **Simulated Annealing**: we return a random state that is a
    successor of the given one. To do it, we choose a random operator
    with random values. Then we use it in order to change the initial
    state.

Goal State
==========

In our experiment we will have two conditions in order to determinate if
a state is a goal state:

1.  The distance of each car has to be at most $30\ Km$. In order to do
    this, we have a variable in state objects with his distance, and we
    obtain it directly.

2.  At the same time can be at most three people in each car. In this
    case, we have a function in the object state, which computes the
    exceed of simultaneous people in a car. The penalization will be
    worst as higher is the value.

Heuristics Function 
===================

Once the representation of the solution state has been defined, the
generation of the initial solution state and the operators on which we
are going to work, we proceed into analyzing the heuristic function.

In order to solve the two solution criteria given in the statement, we
must perform two different heuristics, because the final result that
must be returned has different priorities. The heuristic function that
will solve the first criterion will be called Heuristic Function 1, and
the one that resolves the second, Heuristic Function 2.

Heuristic Function 1
--------------------

The criterion that this function must follow is quite simple, the
objective is to minimize the sum of all the distances that each car has
to do and minimize the maximum number of people at the same time in each
car.

To follow this criterion, we will compute the heuristic value of each
state following this criteria.

1.  Compute the sum of distances that each car exceed. Each state has an
    ArrayList with the distance that every car drives, so iterating this
    array we will obtain the distance of all cars. We will only catch
    the ones that exceeds the limit of $30\ Km$, penalizing this
    situations.

2.  Compute the sum of people that each car carries in a certain moment
    of the travel. Here for each car with the list of pick up and drop
    out people we can find the maxim number of people in a certain
    moment. The values that exceeds the maximum of 3 people for car at
    the same time, are used in order to penalize this situations.

Heuristic Function 2
--------------------

For the second heuristic we have added another criterion in order to
minimize also the number of cars that are driving.

3.  Get the number of cars in each state. Adding the sum of previous
    criteria a penalty value of the number of cars, we obtain this
    second heuristic.


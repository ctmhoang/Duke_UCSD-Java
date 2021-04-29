# Communities within a Social Network

## Overview
This project focuses on communities in a social network (both on
Facebook and Twitter). 
The first part of the project will examine relationships among
the friends of a user. 
In the second part, the goal will be to find the minimum number
of people in the network to announce a message which will broadcast to the
whole community.

## Data
The provided UCSD Facebook/Twitter data.

## Questions

### Easier. 
For a given user, which of their friends does not befriend others?
We will use the answer to suggest them as potential friends. (Use Facebook
data)
### Harder. 
Figure out the minimum set cover (the smallest subset of people
in the network link to everyone in the network).
For example, if everyone in the given set posts news, all
members in the analyzing community can see it. (Use Twitter data)

## Algorithms and Data Structures:
### Primary Data Structure:
The social network presents as a classic graphic using an
adjacency list. Social network users in the graph are vertices.
The edge between vertices represents one-way relationships
(following).

The reciprocal relationship forms if two vertices have both
edges in and out.

### Easier Question. 

The algorithm will be a variant of a breath-first search. It
will just take a list of friends of a user and then check all pairs of those
friends to see if they are correlated.
### Harder Question. 

Finding an optimal set seems like it might be problematic. The minimum dominating set is an NP-complete problem.
I will try to find the exact solution by using DFS, Greedy
algorithm, and Dynamic Programming. But I think the runtime will be
unreasonable.
To improve the runtime, I will compromise the solution. I think
of using order-based randomized local search and the greedy algorithm. It is
the improved version of the greedy algorithm.

## Algorithm Analysis, Limitations, Risk:

### Easier Question:
We need to loop over all a user's friends (|V|) and determine if
they are connected. So, the worst-case runtime will be O(|V|^2) 

### Harder Question: 
This is tougher. I need to investigate it in more detail about
the DFS, Greedy, and Dynamic strategy.
About the order-based randomized local search and greedy
algorithm, we need to create a new permutation in the defined times (O|N|).
Each time, we construct a temporary minimum dominating set using the greedy
algorithm O(|V|).
The worst-case runtime will be O(|V|)  

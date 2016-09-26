# AIProject
Exploratory AI project aimed to create a basic environment in which a number of rudimentary organisms exist and evolve in an attempt to survive longer.
Each Organism has a handful of basic inputs based on what it can perceive which the World generates and passes to the Organism each tick
Each Organism then passes those inputs to its brain which then generates outputs based on the inputs and determine the actions of the Organism that tick
Organisms lose energy when they take various actions and gain it by coming into contact with food 
Organisms transfer energy between each other when they collide based on the velocity they were moving at
(the winner of a game of rock (red), paper (green), scissors (blue) based on the involved Organisms' color gets the energy and the loser loses energy)

Every time an Organism runs out of energy it dies
When an Organism dies, it is replaced by a randomly selected Organism from the living Population and the weights of the simple feed-forward neural network controlling its Brain are mutated slightly

The hope is to use this project as a framework to build a similar, but much more complex version involving Wizards casting complex Spells at each other and evolving to do this task better and survive longer among the mess of other Wizards trying to kill them.

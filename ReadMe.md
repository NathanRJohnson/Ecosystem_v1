# Ecosystem_v1

## How to run
1. Open the project folder and navigate to the file with your OS name
#### For Windows
1. Open either the application.windows32 or the application.windows64 folder and double click the executable file
2. Escape to exit the program

##### For Linux
1. I don't know for certain, but I think if you navigate to one of the application.linux folders from your command line and use the command $ ./Ecosystem_v1, it will run

## High Level Overview
### Intro
The program seeks to simulate a simple ecosystem consisting of two species of creature, Squidlies and Twidlies, as they attempt to outlast the other, each individual keeping their species alive by gathering more food and reproducing, until one of the species runs out of members, a.k.a goes extinct.

### The Creatures
Each creature is on a timer, where each tick their life points decrease. Once their lifepoints reach 0, the creature is considered dead and is removed from the simulation. Creatures can increase their lifepoints by eating food. Eating enough food fast enough will allow the creature to reproduce, adding another member of their species to the simulation, and thus improving that species odds of survival.

The heads of the creatures will change color depending on how many lifepoints they currently have. A healthly creature will have a dark head, whereas a creature on very low lifepoints will have a white head.

1. Squidlies: Characterized by their round heads and slow, methodical, powerful strokes. A lower frequency movement than their counterpart, but make up for it with a longer lifespan.
2. Twidlies: Star-shaped heads and quick short strokes. Reproduce more easily than their counterpart, but have a much lower lifespan.

### The Food
Food is scattered throughout the area and is represented by green circles. The larger the food, the greater the nutritional value, and the more lifepoints it restores to the creature that eats it. Creatures eat by passing over top of the food.
The amount of food present in the simulation at one time is not linear, but instead is a function of the total number of creatures present at that time in the simulation. The more creatures present, the less food there will be to eat. This is to simulate resource competition, as well as to keep the total number of creatures present at any time low so as to avoid performance problems.

## Things to add in future versions
1. Teamwork: Currently the creatures compete even with members of their own species, this not necessarily a problem, but it would be interesting to see how a species of creature that works as a team compete against eachother
2. Carnivores: Would be more interesting if a third party that at creatures were added
3. Larger Arena: Would allow for some cool stuff like terrain or biomes to affect the simulation
4. Weather Conditions
5. Day/Night Cycles

## References/Sources
https://natureofcode.com/book/preface/

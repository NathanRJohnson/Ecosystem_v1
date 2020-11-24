/*
* See ReadMe.md please
*/

import java.util.Iterator;

//System of Creatures
CreatureSystem cs; 
//System for food
FoodSystem fs;

//Limits for population and food
//Controlls the # of objects in the system at any given time
int populationLimit;
int foodLimit;

void setup() {
  size(1920, 1080);

  cs = new CreatureSystem();
  fs = new FoodSystem();
  populationLimit = 15;

  cs.InitializeSystem();
}

void draw() {
  background(#68F0E8);
  //Tell me how many squidlys there are


  fs.addFood();
  //Checkts to see if a creature is over top of food
  fs.intersects(cs.getArray());
  fs.run();

  cs.addCreature();
  cs.run();

  //Equation for the amout of food available on screen
  if (populationLimit > 0) { //Make sure we don't divide by Zero
    foodLimit = 400/populationLimit;
  } else {
    foodLimit = 50;
  }
  fill(#F2CF6E);
  rect( 15, 5, 230, 90);
  fill(0);
  textSize(25);
  text("Total pop: " +populationLimit, 20, 30);
  text("# of Squidlys: " +cs.numSquidlys(), 20, 55);
  text("# of Twidlys: " +cs.numTwidlys(), 20, 80);
}

//Add a squidly to the system
void mousePrecsed() {
  updateLimit(1);
}

//Give me the number of creatures in the system
int getLimit() {
  return populationLimit;
}

//Allow me to change the number of creatures in the system
void updateLimit(int n) {
  populationLimit = populationLimit + n;
}

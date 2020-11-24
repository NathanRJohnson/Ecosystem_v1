import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Iterator; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Ecosystem_v1 extends PApplet {

/*
* See ReadMe.md please
*/



//System of Creatures
CreatureSystem cs; 
//System for food
FoodSystem fs;

//Limits for population and food
//Controlls the # of objects in the system at any given time
int populationLimit;
int foodLimit;

public void setup() {
  

  cs = new CreatureSystem();
  fs = new FoodSystem();
  populationLimit = 15;

  cs.InitializeSystem();
}

public void draw() {
  background(0xff68F0E8);
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
  fill(0xffF2CF6E);
  rect( 15, 5, 230, 90);
  fill(0);
  textSize(25);
  text("Total pop: " +populationLimit, 20, 30);
  text("# of Squidlys: " +cs.numSquidlys(), 20, 55);
  text("# of Twidlys: " +cs.numTwidlys(), 20, 80);
}

//Add a squidly to the system
public void mousePrecsed() {
  updateLimit(1);
}

//Give me the number of creatures in the system
public int getLimit() {
  return populationLimit;
}

//Allow me to change the number of creatures in the system
public void updateLimit(int n) {
  populationLimit = populationLimit + n;
}
class Creature {

  PVector location;
  PVector velocity;
  PVector acceleration;
  float mass;
  boolean b;
  float factor;
  float lifespan;
  float nType; // 1 male, 0 female
  int c;
  //Legs
  Oscillator o, i;
  //Where its headed
  Target t;
  //Gender
  boolean isMale;
  //Species
  int nSpecies;

  float reproduceValue;
  float reproduceCount;
  float maxLife;
  float deathRate;

  Creature(float _m, PVector _location) {
    location = _location.copy();
    velocity = new PVector();
    acceleration = new PVector();
    //starting lifepoints
    lifespan = 350;
    //number over maximum life points at which the creature reproduces
    reproduceValue = 70;
    //How far over maximum life points creature currently is. Life points are increased when a creature eats food
    reproduceCount = 0;
    mass = _m;
    //maximum lifepoints
    maxLife = 500;
    //rate at which lifepoints are decreased until 0
    deathRate = 1;
    nType = random(1);
    nSpecies = 0;
    //legs
    o = new Oscillator(location.x, location.y);
    i = new Oscillator(location.x, location.y);
    //destination
    t = new Target();

    //Blue ball if male, pink if female
    if (nType < 0.50f ) {
      c = 0xff3094E3;
      isMale = true;
    } else {
      c = 0xffF068C7;
      isMale = false;
    }
  }


  public void update() {
    //Update speed and position, move legs 
    velocity.add(acceleration);
    location.add(velocity);
    acceleration.mult(0);
    velocity.limit(0.5f);
    i.oscillate(0.05f);
    o.oscillate(0.05f);
    //remove life    
    lifespan -= deathRate;
    //check if threshhold is reached to reproduce
    if (reproduceCount > reproduceValue) {
      reproduceCount = 0;
      createEgg();
    }
  }

  //Tells the food it has been eaten
  public void eatFood(Food f) {
    f.setEatenState(true);
  }

  //Give birth
  public void createEgg() {
    updateLimit(1);
  }

  //void intersects(ArrayList<Squidly> squids) {
  //  for (Squidly other : squids) {
  //    if (other != this) {
  //      float d = PVector.dist(other.location, location);
  //      if (d < mass*16 + other.mass*16) {
  //        //check if availabe to reproduce
  //        if (isMale && !other.isMale) {
  //          if (lifespan > 400 && other.lifespan > 400) {
  //            if (!isMale) {
  //              createEgg();
  //            }
  //          }
  //        }
  //      }
  //    }
  //  }
  //}


  public void display() {
    pushMatrix();
    translate(location.x, location.y);
    rotate(velocity.heading());
    factor = o.display(0, 0, c);
    stroke(0, 4); 
    fill(map(lifespan, 0, 500, 255, 0));
    ellipse(0, 0, mass*8, mass*8 );
    popMatrix();
  }

  public void run() {
    display();
    update();
    track();
  }

  public void applyForce(PVector force) {
    PVector f = PVector.div(force, mass);
    acceleration.add(f);
  }

  /*
  * For following the mouse
   */
  public void follow(PVector target) {
    factor = -(abs(map(factor, 0, 100, 0, 10)) - 10);
    //factor = abs(map(factor, 1, 100, 10, 1) - 10);
    PVector dir = PVector.sub(target, location);
    dir.normalize();
    dir.mult(factor);
    acceleration.add(dir);
  }

  /*
  * Follows the target
   */
  public void track() {
    factor = -(abs(map(factor, 0, 100, 0, 10)) - 10);
    PVector dir = PVector.sub(t.location, location);
    dir.normalize();
    dir.mult(factor);
    acceleration.add(dir);

    if (PVector.dist(location, t.location) < 10) {
      t.setLocation(new PVector(random(0, width), random(0, height)));
    }
  }

  //For adding health/ giving birth after eating
  public void addLife(float value) {
    if (lifespan + value < maxLife) {
      lifespan = lifespan + value;
    } else if (lifespan + value > maxLife) {
      reproduceCount += (lifespan + value) - maxLife;
    }
  }

  /*
* Says if the creature has died
   */
  public boolean isDead() {
    if (lifespan < 0.0f) {
      return true;
    }
    return false;
  }

  /*
* when creature reaches the edge of the screen, negate it's velocity to send it in the opposite direction
   */
  public void reflectEdges() {
    //[full] When it reaches one edge, set location to the other.
    if (location.x > width) {
      velocity.x *= -1;
      location.x = width;
    } else if (location.x < 0) {
      velocity.x *= -1;
      location.x = 0;
    }

    if (location.y > height) {
      velocity.y *= -1;
      location.y = height;
    } else if (location.y < 0) {
      velocity.y *= -1;
      location.y = 0;
    }
    //[end]
  }

  public void setPos(float x, float y) {
    location.x = x;
    location.y = y;
  }

  public int getSpecies() {
    return nSpecies;
  }
}
/*
* Maintains arraylist of creatures
*/
class CreatureSystem {

  ArrayList<Creature> creatures;

  CreatureSystem() {
    creatures = new ArrayList<Creature>();
  }

/*
* Called at program startup to randomly generate a population of Squidlies and Twidlies
*/
  public void InitializeSystem() {
    while (creatures.size() < getLimit()) {
      float r = random(1);
      if (r < 0.5f) {
        creatures.add(new Squidly(random(1, 3), new PVector(random(200, width-200), random(200, height-200))));
      } else {
        creatures.add(new Twidly(random(1, 3), new PVector(random(200, width-200), random(200, height-200))));
      }
    }
  }

/*
* add a Creature to the system. If there are two many creatures (shouldn't happen) the program exits to avoid using to much memory
*/
  public void addCreature() {

    if (creatures.size() < getLimit()) {
      if (getType() == 1) {
        creatures.add(new Squidly(random(1, 3), new PVector(random(200, width-200), random(200, height-200))));
      } else if (getType() == 2) {
        creatures.add(new Twidly(random(1, 3), new PVector(random(200, width-200), random(200, height-200))));
      }
    }
    if (getLimit() > 55) {
      exit();
    }
  }
  
/*
* Loops through the system and tells each creature to run. 
* Removes creatures from the system when they die
*/
  public void run() {
    Iterator<Creature> it = creatures.iterator();
    while (it.hasNext()) {
      Creature c = (Creature) it.next();
      c.run();
      if (c.isDead()) {
        updateLimit(-1);
        it.remove();
      }
    }
  }
  
/*
* Counts the number of Squidlies in the system
*/
  public int numSquidlys() {
    int sum = 0;
    for (Creature c : creatures) {
      if (c instanceof Squidly) {
        sum++;
      }
    }
    return sum;
  }
  
/*
* Counts the number of Twidlies in the system
*/
  public int numTwidlys() {
    int sum = 0;
    for (Creature c : creatures) {
      if (c instanceof Twidly) {
        sum++;
      }
    }
    return sum;
  }

  public void applyForce(PVector f) {
    for (Creature c : creatures) {
      c.applyForce(f);
    }
  }

  public void follow(PVector target) {
    for (Creature c : creatures) {
      c.follow(target);
    }
  }

  public void setPos(float x, float y) {
    for (Creature c : creatures) {
      c.setPos(x, y);
    }
  }

/*
* For debugging purposes
*/
  public int getType() {
    for (Creature c : creatures) {
      if (c instanceof Twidly) {
        println("Twid");
        return 2;
      } else if (c instanceof Squidly) {
        println("Squid");
        return 1;
      }
    }
    return 0;
  }

  public ArrayList<Creature> getArray() {
    return creatures;
  }

}
/*
* Class for the food which the creatures must eat to surivie and reproduce
*/
class Food {

  PVector location;
  //How much it will increase the lifespan
  float nutrientValue;
  float size;
  boolean isEaten;

  Food (PVector _location) {
    location = _location.copy();
    
    //Allocate a nutritional value
    nutrientValue = random(75, 135);
    //Size is based on nutrient value
    size = nutrientValue/20;
    isEaten = false;
  }
  
  //Draw the food
  public void display() {
    fill(0xff28DE35);
    noStroke();
    ellipse(location.x, location.y, size * 5, size * 5);
  }

  //Check to see if a creature has intersected some food
  public void intersects(ArrayList<Creature> creatures) {
    for (Creature c : creatures) {
      float d = PVector.dist(c.location, location);
      if (d < size*15 + c.mass) {
        //Tell the food it has been eaten
        isEaten = true;
        //Increase the lifepoints of that creature
        c.addLife(this.nutrientValue);
      }
    }
  }

  public boolean isEaten() {
    return isEaten;
  }

  public void setEatenState(boolean b) {
    isEaten = b;
  }
}
/*
* System class to control the arraylist of food
*/

class FoodSystem {

  ArrayList<Food> foods;

  FoodSystem() {
    foods = new ArrayList<Food>();
  }

/*
* adds food provided we are within the food limit
*/
  public void addFood() {
    if (foods.size() < foodLimit) {
      foods.add(new Food(new PVector(random(100, width - 100), random(100, height - 100))));
    }
  }

/*
* checks if the food is intersecting any creatures
* I don't think I use this method, and instead use the intersects method in the CreatureSystem
*/
  public void intersects(ArrayList<Creature> creatures) {
    for (Food f : foods) {
      f.intersects(creatures);
    }
  }

/*
* Tell food to display and remove any eaten food
*/
  public void run() {
    Iterator<Food> it = foods.iterator();
    while (it.hasNext()) {
      Food f = (Food) it.next();
      f.display();
      if (f.isEaten()) {
        it.remove();
      }
    }
  }
}
/*
* Acts as the propellor of the creature
* Not necissary, but when I designed the creature
* in step 3, I used and oscillator, so here it is.
*/
class Oscillator {

  float angle;
  PVector location;
  PVector velocity;
  PVector amplitude;
  
  Oscillator(float _x, float _y) {
    location = new PVector(_x, _y);
    angle = 0;
    amplitude = new PVector(0, 100);
  }

  public void oscillate(float increment) {
    angle += increment; 
  }
  
  //Draw Leg
  public float display(float dX, float dY, int c) {
    fill(175);
    stroke(0);
    //Equation for the twirl
    float y = sin(angle)*amplitude.y;
    
    constrain(y, 0, 100);
    line (dX - 25, dY - y/4, dX, dY);
    ellipse(dX - 25, dY - y/4, 8, 8);
    line (dX - 25, dY + y/4, dX, dY);
    fill(c);
    ellipse(dX - 25, dY + y/4, 8, 8);

  return y;
  }
}
/*
*Slower, longer strokes
*Cicle head
*/
class Squidly extends Creature {

  Squidly(float _m, PVector _location) {
    super(_m, _location);
  }


  public void display() {
    pushMatrix();
    translate(location.x, location.y);
    rotate(velocity.heading());
    factor = o.display(0, 0, c);
    i.display(0, 0, c);
    stroke(0, 4); 
    fill(map(lifespan, 0, 500, 255, 0));
    ellipse(0, 0, mass*8, mass*8 );
    popMatrix();
  }
}
/*
* Used within Creature class
* Sets an arbitrary within the screen for the creature to 
* navigate to
*/
class Target {

  PVector location;

  Target() {
    location = new PVector(random(25, width-25), random(25, height-25));
  }
  
  //For when location is reached
  public void setLocation(PVector _location) {    
    location = _location;
  }
  
  public PVector getLocation(){
    return location;
  }
}
/*
*Move with quicker short strokes, are faster than Squidlys, repoduce more easily, die quicker
*/
class Twidly extends Creature {

  Twidly(float _m, PVector _location) {
    super(_m, _location);
  }
  
  public void display() {
    pushMatrix();
    translate(location.x, location.y);
    rotate(velocity.heading());
    factor = o.display(0, 0, c);
    i.display(0, 0, c);
    stroke(0, 4); 
    fill(map(lifespan, 0, 500, 255, 0));
    star(0, 0, 3 * mass, 7 * mass, 5);
    popMatrix();
  }

  public void update() {
    velocity.add(acceleration);
    location.add(velocity);
    acceleration.mult(0);
    velocity.limit(2);
    o.oscillate(0.15f);
    i.oscillate(0.15f);
    lifespan -= 1.15f;

    if (reproduceCount > 50.0f) {
      reproduceCount = 0;
      createEgg();
    }
  }


  public void star(float x, float y, float radius1, float radius2, int npoints) {
    float angle = TWO_PI / npoints;
    float halfAngle = angle/2.0f;
    beginShape();
    for (float a = 0; a < TWO_PI; a += angle) {
      float sx = x + cos(a) * radius2;
      float sy = y + sin(a) * radius2;
      vertex(sx, sy);
      sx = x + cos(a+halfAngle) * radius1;
      sy = y + sin(a+halfAngle) * radius1;
      vertex(sx, sy);
    }
    endShape(CLOSE);
  }
}
  public void settings() {  size(1920, 1080); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "Ecosystem_v1" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

/**
* Base Class for Squidly and Twidly
*/
class Creature {

  PVector location;
  PVector velocity;
  PVector acceleration;
  float mass;
  boolean b;
  float factor;
  float lifespan;
  float nType; // 1 male, 0 female
  color c;
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
    if (nType < 0.50 ) {
      c = #3094E3;
      isMale = true;
    } else {
      c = #F068C7;
      isMale = false;
    }
  }


  void update() {
    //Update speed and position, move legs 
    velocity.add(acceleration);
    location.add(velocity);
    acceleration.mult(0);
    velocity.limit(0.5);
    i.oscillate(0.05);
    o.oscillate(0.05);
    //remove life    
    lifespan -= deathRate;
    //check if threshhold is reached to reproduce
    if (reproduceCount > reproduceValue) {
      reproduceCount = 0;
      createEgg();
    }
  }

  //Tells the food it has been eaten
  void eatFood(Food f) {
    f.setEatenState(true);
  }

  //Give birth
  void createEgg() {
    updateLimit(1);
  }

  void display() {
    pushMatrix();
    translate(location.x, location.y);
    rotate(velocity.heading());
    factor = o.display(0, 0, c);
    stroke(0, 4); 
    fill(map(lifespan, 0, 500, 255, 0));
    ellipse(0, 0, mass*8, mass*8 );
    popMatrix();
  }

  void run() {
    display();
    update();
    track();
  }

  void applyForce(PVector force) {
    PVector f = PVector.div(force, mass);
    acceleration.add(f);
  }

/**
* For following the mouse
*/
  void follow(PVector target) {
    factor = -(abs(map(factor, 0, 100, 0, 10)) - 10);
    //factor = abs(map(factor, 1, 100, 10, 1) - 10);
    PVector dir = PVector.sub(target, location);
    dir.normalize();
    dir.mult(factor);
    acceleration.add(dir);
  }

/**
* Follows the target
*/
  void track() {
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
  void addLife(float value) {
    if (lifespan + value < maxLife) {
      lifespan = lifespan + value;
    } else if (lifespan + value > maxLife) {
      reproduceCount += (lifespan + value) - maxLife;
    }
  }

/**
* Returns true if the creature has died
*/
  boolean isDead() {
    if (lifespan < 0.0) {
      return true;
    }
    return false;
  }

/**
* when creature reaches the edge of the screen, negate it's velocity to send it in the opposite direction
*/
  void reflectEdges() {
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

  void setPos(float x, float y) {
    location.x = x;
    location.y = y;
  }

  int getSpecies() {
    return nSpecies;
  }
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

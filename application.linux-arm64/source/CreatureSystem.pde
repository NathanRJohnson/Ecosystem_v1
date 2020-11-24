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
  void InitializeSystem() {
    while (creatures.size() < getLimit()) {
      float r = random(1);
      if (r < 0.5) {
        creatures.add(new Squidly(random(1, 3), new PVector(random(200, width-200), random(200, height-200))));
      } else {
        creatures.add(new Twidly(random(1, 3), new PVector(random(200, width-200), random(200, height-200))));
      }
    }
  }

/*
* add a Creature to the system. If there are two many creatures (shouldn't happen) the program exits to avoid using to much memory
*/
  void addCreature() {

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
  void run() {
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
  int numSquidlys() {
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
  int numTwidlys() {
    int sum = 0;
    for (Creature c : creatures) {
      if (c instanceof Twidly) {
        sum++;
      }
    }
    return sum;
  }

  void applyForce(PVector f) {
    for (Creature c : creatures) {
      c.applyForce(f);
    }
  }

  void follow(PVector target) {
    for (Creature c : creatures) {
      c.follow(target);
    }
  }

  void setPos(float x, float y) {
    for (Creature c : creatures) {
      c.setPos(x, y);
    }
  }

/*
* For debugging purposes
*/
  int getType() {
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

  ArrayList<Creature> getArray() {
    return creatures;
  }

}

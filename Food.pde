/**
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
  void display() {
    fill(#28DE35);
    noStroke();
    ellipse(location.x, location.y, size * 5, size * 5);
  }

  //Check to see if a creature has intersected some food
  void intersects(ArrayList<Creature> creatures) {
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

  boolean isEaten() {
    return isEaten;
  }

  void setEatenState(boolean b) {
    isEaten = b;
  }
}

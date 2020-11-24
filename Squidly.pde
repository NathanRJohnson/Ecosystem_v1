/**
* Slower, longer strokes
* Cicle head
*/
class Squidly extends Creature {

  Squidly(float _m, PVector _location) {
    super(_m, _location);
  }


  void display() {
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

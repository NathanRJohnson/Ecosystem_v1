/**
* Move with quicker short strokes, are faster than Squidlys, repoduce more easily, die quicker
*/
class Twidly extends Creature {

  Twidly(float _m, PVector _location) {
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
    star(0, 0, 3 * mass, 7 * mass, 5);
    popMatrix();
  }

  void update() {
    velocity.add(acceleration);
    location.add(velocity);
    acceleration.mult(0);
    velocity.limit(2);
    o.oscillate(0.15);
    i.oscillate(0.15);
    lifespan -= 1.15;

    if (reproduceCount > 50.0) {
      reproduceCount = 0;
      createEgg();
    }
  }

/**
* Draw the headshape
*/

  void star(float x, float y, float radius1, float radius2, int npoints) {
    float angle = TWO_PI / npoints;
    float halfAngle = angle/2.0;
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

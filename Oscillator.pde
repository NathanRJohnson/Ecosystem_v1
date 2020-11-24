/**
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

  void oscillate(float increment) {
    angle += increment; 
  }
  
  //Draw Leg
  float display(float dX, float dY, color c) {
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

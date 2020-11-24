/**
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
  void setLocation(PVector _location) {    
    location = _location;
  }
  
  PVector getLocation(){
    return location;
  }
}

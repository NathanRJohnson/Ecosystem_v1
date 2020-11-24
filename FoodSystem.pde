/**
* Maintains an ArrayList of Food
*/

class FoodSystem {

  ArrayList<Food> foods;

  FoodSystem() {
    foods = new ArrayList<Food>();
  }

/**
* adds food provided we are within the food limit
*/
  void addFood() {
    if (foods.size() < foodLimit) {
      foods.add(new Food(new PVector(random(100, width - 100), random(100, height - 100))));
    }
  }

/**
* checks if the food is intersecting any creatures
* I don't think I use this method, and instead use the intersects method in the CreatureSystem
*/
  void intersects(ArrayList<Creature> creatures) {
    for (Food f : foods) {
      f.intersects(creatures);
    }
  }

/**
* Tell food to display and remove any eaten food
*/
  void run() {
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

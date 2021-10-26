package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }

    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    @GetMapping("/restrpc/cheapest")
    Meal getCheapestMeal() {
        Optional<Meal> meal = mealsRepository.findCheapestMeal();

        return meal.orElseThrow(() -> new MealNotFoundException("Cheapest"));
    }

    @GetMapping("/restrpc/biggest")
    Meal getBiggestMeal() {
        Optional<Meal> meal = mealsRepository.findBiggestMeal();

        return meal.orElseThrow(() -> new MealNotFoundException("Biggest"));
    }

    @RequestMapping(path = "restrpc/meals/{id}")
    @ResponseBody
    void removeMeal(@PathVariable String id) {
        mealsRepository.delete(id);
    }

    @PostMapping(path = "restrpc/meals")
    void addMeal(@RequestBody Meal newMeal) {
        mealsRepository.add(newMeal);
    }

    @PutMapping(path = "restrpc/meals/{id}")
    void addMeal(@PathVariable String id, @RequestBody Meal newMeal) {
        mealsRepository.update(id, newMeal);
    }
}

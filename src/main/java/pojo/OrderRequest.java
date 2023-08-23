package pojo;

import java.util.List;
import java.util.Random;

public class OrderRequest {
    private List<String> ingredients;

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public OrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public static List<String> getRandomIngredients(List<String> ingredients) {
        if (ingredients != null) {
            return ingredients.subList(0, new Random().nextInt(ingredients.size()));
        } else {
            return null;
        }
    }
}
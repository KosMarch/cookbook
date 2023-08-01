package springboot.cookbook.service;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import springboot.cookbook.model.Recipe;

public interface RecipeService {
    Recipe createRecipe(Recipe recipe);

    List<Recipe> findAll(PageRequest pageRequest);

    List<Recipe> findAllByParentId(Long id, PageRequest pageRequest);

    List<Recipe> findAllById(Long id, PageRequest pageRequest);

    Recipe findById(Long id);

    Recipe update(Recipe recipe);

    void delete(Long id);
}

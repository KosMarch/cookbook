package springboot.cookbook.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import springboot.cookbook.model.Recipe;
import springboot.cookbook.repository.RecipeRepository;

@AllArgsConstructor
@Service
public class RecipeServiceImpl implements RecipeService {
    private RecipeRepository recipeRepository;

    @Override
    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public List<Recipe> findAll(PageRequest pageRequest) {
        return recipeRepository.findAll(pageRequest).toList();
    }

    @Override
    public List<Recipe> findAllByParentId(Long id, PageRequest pageRequest) {
        return recipeRepository.findAllByParentId(id, pageRequest);
    }

    @Override
    public List<Recipe> findAllById(Long id, PageRequest pageRequest) {
        List<Recipe> recipes = new ArrayList<>();
        Long currentId = recipeRepository.getReferenceById(id).getParentId();
        while (currentId != null) {
            recipes.add(recipeRepository.getReferenceById(currentId));
            currentId = recipeRepository.getReferenceById(currentId).getParentId();
        }
        recipes.sort(Comparator.comparing(Recipe::getDescription));

        int fromIndex = pageRequest.getPageNumber() * pageRequest.getPageSize();
        int toIndex = Math.min(fromIndex + pageRequest.getPageSize(), recipes.size());

        return recipes.subList(fromIndex, toIndex);
    }

    @Override
    public Recipe findById(Long id) {
        return recipeRepository.getReferenceById(id);
    }

    @Override
    public Recipe update(Recipe recipe) {
        recipeRepository.findById(recipe.getId()).orElseThrow(()
                -> new EntityNotFoundException("Can't find recipe with id " + recipe.getId()));
        return recipeRepository.save(recipe);
    }

    @Override
    public void delete(Long id) {
        smartDelete(id);
    }

    @Transactional
    public void smartDelete(Long id) {
        Long newId = recipeRepository.getReferenceById(id).getParentId();

        recipeRepository.findAllByParentId(id)
                .stream()
                .peek(r -> r.setParentId(newId))
                .forEach(this::update);

        recipeRepository.deleteById(id);
    }
}

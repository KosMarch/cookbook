package springboot.cookbook.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import springboot.cookbook.container.MySQLJpaContainerExtension;
import springboot.cookbook.model.Recipe;
import springboot.cookbook.repository.RecipeRepository;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith({MySQLJpaContainerExtension.class})
class RecipeServiceImplTest {
    private RecipeService recipeService;
    @MockBean
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @Test
    void createRecipe_Ok() {
        Recipe recipe = getDefaultRecipe();

        Mockito.when(recipeRepository.save(recipe)).thenReturn(recipe);

        Recipe savedRecipe = recipeService.createRecipe(recipe);

        Assertions.assertEquals(recipe.getDateCreated(), savedRecipe.getDateCreated());
        Assertions.assertEquals(recipe.getDescription(), savedRecipe.getDescription());
        Assertions.assertEquals(recipe.getParentId(), savedRecipe.getParentId());
    }

    @Test
    void findAllById_Ok() {
        Recipe parentRecipe = getDefaultRecipe();
        Recipe childRecipe1 = new Recipe(2L, LocalDateTime.of(2023,7,16,9,30), "Child Recipe 1", 1L);

        Recipe childRecipe2 = new Recipe(3L, LocalDateTime.of(2023,7,16,9,30), "Child Recipe 2", 2L);

        Mockito.when(recipeRepository.getReferenceById(1L)).thenReturn(parentRecipe);
        Mockito.when(recipeRepository.getReferenceById(2L)).thenReturn(childRecipe1);
        Mockito.when(recipeRepository.getReferenceById(3L)).thenReturn(childRecipe2);

        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("description"));
        List<Recipe> result = recipeService.findAllById(3L, pageRequest);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(childRecipe1, result.get(0));
        Assertions.assertEquals(parentRecipe, result.get(1));
    }

    @Test
    void updateRecipe_Ok() {
        Recipe existingRecipe = getDefaultRecipe();

        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(existingRecipe));
        Mockito.when(recipeRepository.save(existingRecipe)).thenReturn(existingRecipe);

        Recipe updatedRecipe = recipeService.update(existingRecipe);

        Assertions.assertNotNull(updatedRecipe);
        Assertions.assertEquals(existingRecipe, updatedRecipe);

        Mockito.verify(recipeRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(recipeRepository, Mockito.times(1)).save(existingRecipe);
    }

    @Test
    void updateRecipe_NotFound() {
        Recipe nonExistingRecipe = new Recipe(100L, LocalDateTime.now() , "Non-existing Recipe");

        Mockito.when(recipeRepository.findById(100L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> recipeService.update(nonExistingRecipe));

        Mockito.verify(recipeRepository, Mockito.times(1)).findById(100L);
        Mockito.verify(recipeRepository, Mockito.never()).save(Mockito.any());
    }

    private Recipe getDefaultRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setDateCreated(LocalDateTime.of(2023,7,16,9,30));
        recipe.setDescription("Test Description");
        return recipe;
    }
}

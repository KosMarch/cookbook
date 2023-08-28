package springboot.cookbook.dto.mapper;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import springboot.cookbook.dto.request.RecipeRequestDto;
import springboot.cookbook.dto.response.RecipeResponseDto;
import springboot.cookbook.model.Recipe;

@Component
public class RecipeMapper implements DtoMapper<Recipe, RecipeRequestDto, RecipeResponseDto> {
    @Override
    public Recipe toModel(RecipeRequestDto requestDto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(requestDto.getTitle());
        recipe.setDescription(requestDto.getDescription());
        recipe.setDateCreated(LocalDateTime.now());
        if (requestDto.getParentId() != null) {
            Recipe parentRecipe = new Recipe();
            parentRecipe.setId(requestDto.getParentId());
            recipe.setParent(parentRecipe);
        }
        return recipe;
    }

    @Override
    public RecipeResponseDto toDto(Recipe model) {
        RecipeResponseDto response = new RecipeResponseDto();
        response.setId(model.getId());
        response.setTitle(model.getTitle());
        response.setDescription(model.getDescription());
        response.setDateCreated(model.getDateCreated());
        if (model.getParent() != null) {
            response.setParentId(model.getParent().getId());
        }
        return response;
    }
}

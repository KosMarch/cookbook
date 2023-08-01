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
        recipe.setDescription(requestDto.getDescription());
        recipe.setDateCreated(LocalDateTime.now());
        recipe.setParentId(requestDto.getParentId());
        return recipe;
    }

    @Override
    public RecipeResponseDto toDto(Recipe model) {
        RecipeResponseDto response = new RecipeResponseDto();
        response.setDescription(model.getDescription());
        response.setDateCreated(model.getDateCreated());
        response.setParentId(model.getParentId());
        response.setId(model.getId());
        return response;
    }
}

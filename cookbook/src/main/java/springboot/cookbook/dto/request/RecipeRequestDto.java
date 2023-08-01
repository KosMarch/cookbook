package springboot.cookbook.dto.request;

import lombok.Data;

@Data
public class RecipeRequestDto {
    private String description;
    private Long parentId;
}

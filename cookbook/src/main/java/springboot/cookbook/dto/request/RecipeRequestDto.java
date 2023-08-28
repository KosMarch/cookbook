package springboot.cookbook.dto.request;

import lombok.Data;

@Data
public class RecipeRequestDto {
    private String title;
    private String description;
    private Long parentId;
}

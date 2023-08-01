package springboot.cookbook.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RecipeResponseDto {
    private Long id;
    private LocalDateTime dateCreated;
    private String description;
    private Long parentId;
}

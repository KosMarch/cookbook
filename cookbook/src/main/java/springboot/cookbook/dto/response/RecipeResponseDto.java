package springboot.cookbook.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RecipeResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dateCreated;
    private Long parentId;
}

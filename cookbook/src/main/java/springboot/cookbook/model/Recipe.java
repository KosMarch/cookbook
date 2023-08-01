package springboot.cookbook.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateCreated;
    private String description;
    private Long parentId;

    public Recipe() {
    }

    public Recipe(Long id, LocalDateTime dateCreated, String description) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.description = description;
    }

    public Recipe(Long id, LocalDateTime dateCreated, String description, Long parentId) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.description = description;
        this.parentId = parentId;
    }
}

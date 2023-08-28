package springboot.cookbook.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateCreated;
    private String title;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Recipe parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Recipe> children;

    public Recipe() {
    }

    public Recipe(Long id, LocalDateTime dateCreated, String title) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.title = title;
    }

    public Recipe(Long id, LocalDateTime dateCreated, String title, Recipe parent) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.title = title;
        this.parent = parent;
    }
}

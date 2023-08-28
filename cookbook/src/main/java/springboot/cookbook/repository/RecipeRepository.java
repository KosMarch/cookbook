package springboot.cookbook.repository;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.cookbook.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> getAllBy(Pageable pageable);

    List<Recipe> findAllByParentId(Long id, PageRequest pageRequest);
}

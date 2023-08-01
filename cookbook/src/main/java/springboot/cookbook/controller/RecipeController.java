package springboot.cookbook.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.cookbook.dto.mapper.DtoMapper;
import springboot.cookbook.dto.request.RecipeRequestDto;
import springboot.cookbook.dto.response.RecipeResponseDto;
import springboot.cookbook.model.Recipe;
import springboot.cookbook.service.RecipeService;

@RestController
@AllArgsConstructor
@RequestMapping("/recipes")
public class RecipeController {
    private RecipeService recipeService;
    private DtoMapper<Recipe, RecipeRequestDto, RecipeResponseDto> mapper;

    @PostMapping
    public RecipeResponseDto create(@RequestBody RecipeRequestDto requestDto) {
        return mapper.toDto(recipeService.createRecipe(mapper.toModel(requestDto)));
    }

    @GetMapping
    public List<RecipeResponseDto> findAll(
            @RequestParam(defaultValue = "20") Integer count,
            @RequestParam (defaultValue = "0") Integer page,
            @RequestParam (defaultValue = "description") String sortBy) {
        Sort sort = Sort.by(sortBy);
        PageRequest pageRequest = PageRequest.of(page, count, sort);
        return recipeService.findAll(pageRequest).stream()
                .map(mapper::toDto)
                .toList();
    }

    @PutMapping("/{id}")
    public RecipeResponseDto update(@PathVariable Long id,
                                    @RequestBody RecipeRequestDto requestDto) {
        Recipe recipe = mapper.toModel(requestDto);
        recipe.setId(id);
        return mapper.toDto(recipeService.update(recipe));
    }

    @GetMapping("/parents/{id}")
    public List<RecipeResponseDto> findAllById(
            @PathVariable Long id,
            @RequestParam (defaultValue = "10") Integer count,
            @RequestParam (defaultValue = "0") Integer page) {
        PageRequest pageRequest = PageRequest.of(page, count);
        return recipeService.findAllById(id, pageRequest)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/children/{id}")
    public List<RecipeResponseDto> findAllByParentId(
            @PathVariable Long id,
            @RequestParam (defaultValue = "10") Integer count,
            @RequestParam (defaultValue = "0") Integer page,
            @RequestParam (defaultValue = "description") String sortBy) {
        Sort sort = Sort.by(sortBy);
        PageRequest pageRequest = PageRequest.of(page, count, sort);
        return recipeService.findAllByParentId(id, pageRequest)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public RecipeResponseDto findById(@PathVariable Long id) {
        return mapper.toDto(recipeService.findById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        recipeService.delete(id);
    }
}

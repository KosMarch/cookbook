package springboot.cookbook.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import springboot.cookbook.container.MySQLJpaContainerExtension;
import springboot.cookbook.dto.mapper.RecipeMapper;
import springboot.cookbook.dto.request.RecipeRequestDto;
import springboot.cookbook.dto.response.RecipeResponseDto;
import springboot.cookbook.model.Recipe;
import springboot.cookbook.service.RecipeService;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith({MySQLJpaContainerExtension.class})
public class RecipeControllerTest {
    private MockMvc mockMvc;
    private RecipeController recipeController;
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeMapper mapper;

    @MockBean
    private RecipeService recipeService;

    @BeforeEach
    void setUpForEach() {
        objectMapper = new ObjectMapper();
        recipeController = new RecipeController(recipeService, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    void findAll_Ok() throws Exception {
        List<Recipe> mockRecipes = new ArrayList<>();
        mockRecipes.add(getDefaultRecipe());
        mockRecipes.add(new Recipe(2L, LocalDateTime.of(2003,7,16,9,30), "Description 2"));

        RecipeResponseDto mockResponse2 = new RecipeResponseDto();
        mockResponse2.setId(2L);
        mockResponse2.setDateCreated(LocalDateTime.of(2003,7,16,9,30));
        mockResponse2.setTitle("Description 2");

        Mockito.when(recipeService.findAll(Mockito.any())).thenReturn(mockRecipes);
        Mockito.when(mapper.toDto(mockRecipes.get(0))).thenReturn(getDefaultResponse());
        Mockito.when(mapper.toDto(mockRecipes.get(1))).thenReturn(mockResponse2);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateCreated").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dateCreated").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Description 2"));
    }

    @Test
    void findById_Ok() throws Exception {
        Long recipeId = 1L;
        Recipe mockRecipe = new Recipe(1L, LocalDateTime.of(2023,7,16,9,30), "Description 1");

        Mockito.when(recipeService.findById(recipeId)).thenReturn(mockRecipe);
        Mockito.when(mapper.toDto(Mockito.any())).thenReturn(getDefaultResponse());
        Mockito.when(mapper.toModel(Mockito.any())).thenReturn(mockRecipe);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/{id}", recipeId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(recipeId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreated").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Description 1"));
    }

    @Test
    void createRecipe_Ok() throws Exception {
        Mockito.when(recipeService.createRecipe(Mockito.any())).thenReturn(getDefaultRecipe());
        Mockito.when(mapper.toDto(Mockito.any())).thenReturn(getDefaultResponse());
        Mockito.when(mapper.toModel(Mockito.any())).thenReturn(getDefaultRecipe());

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                    .writer()
                                    .withDefaultPrettyPrinter()
                                    .writeValueAsString(getDefaultRequest()))
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreated").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    void updateRecipe_Ok() throws Exception {
        Mockito.when(recipeService.createRecipe(Mockito.any())).thenReturn(getDefaultRecipe());
        Mockito.when(mapper.toDto(Mockito.any())).thenReturn(getDefaultResponse());
        Mockito.when(mapper.toModel(Mockito.any())).thenReturn(getDefaultRecipe());

        mockMvc.perform(MockMvcRequestBuilders.put("/recipes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writer()
                                .withDefaultPrettyPrinter()
                                .writeValueAsString(getDefaultRequest()))
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateCreated").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    private RecipeRequestDto getDefaultRequest() {
        RecipeRequestDto mockRequest = new RecipeRequestDto();
        mockRequest.setTitle("Description 1");
        return mockRequest;
    }

    private Recipe getDefaultRecipe() {
        return new Recipe(1L, LocalDateTime.of(2023,7,16,9,30), "Description 1");
    }

    private RecipeResponseDto getDefaultResponse() {
        RecipeResponseDto mockResponse = new RecipeResponseDto();
        mockResponse.setId(1L);
        mockResponse.setDateCreated(LocalDateTime.of(2023,7,16,9,30));
        mockResponse.setTitle("Description 1");
        return mockResponse;
    }
}

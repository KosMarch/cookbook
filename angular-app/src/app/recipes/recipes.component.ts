import { Component } from '@angular/core';
import { Recipe } from "../recipe";
import { RecipeService } from "../recipe.service";

@Component({
  selector: 'app-recipes',
  templateUrl: './recipes.component.html',
  styleUrls: ['./recipes.component.css']
})
export class RecipesComponent {
  recipes: Recipe[] = [];
  count: number = 10;
  page: number = 0;

  constructor(
    private recipeService: RecipeService) {
  }

  ngOnInit(): void {
    this.getRecipes();
  }

  getRecipes(): void {
    this.recipeService.getRecipes(this.count, this.page)
      .subscribe(recipes => this.recipes = recipes);
  }

  add(description: string): void {
    description = description.trim();
    if (!description) { return; }
    this.recipeService.addRecipe({ description } as Recipe)
      .subscribe(recipe => {
        this.recipes.push(recipe)
    });
  }

  delete(recipe: Recipe): void {
    this.recipes = this.recipes.filter(r => r !== recipe);
    this.recipeService.deleteRecipe(recipe.id).subscribe();
  }

  nextPage(): void {
      this.page++;
      this.getRecipes();
  }

  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.getRecipes();
    }
  }
}

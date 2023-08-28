import { Component } from '@angular/core';
import { Recipe } from "../recipe";
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { RecipeService } from "../recipe.service";
import { Router } from '@angular/router';
import { switchMap } from "rxjs";
import * as moment from "moment";

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrls: ['./recipe-detail.component.css']
})
export class RecipeDetailComponent {
  recipe: Recipe | undefined;
  recipesChildren: Recipe[] = [];
  recipesParents: Recipe[] = [];
  count: number = 10;
  pageParents: number = 0;
  pageChildren: number = 0;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private recipeService: RecipeService,
    private location: Location
  ) {
  }

  ngOnInit(): void {
    this.getRecipe();
    this.getParentsRecipes();
    this.getChildrenRecipes();

    this.route.params.pipe(
      switchMap(params => this.recipeService.getRecipe(params['id']))
    ).subscribe(recipe => {
      this.recipe = recipe;
      this.pageParents = 0;
      this.pageChildren = 0;
    });
  }

  getRecipe(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.recipeService.getRecipe(id)
      .subscribe(recipe => this.recipe = recipe);
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.recipe) {
      this.recipeService.updateRecipe(this.recipe)
        .subscribe(() => this.goBack());
    }
  }

  saveFork(title: string, parentId: number, recipeDescription: string): void {
    title = title.trim();
    recipeDescription = recipeDescription.trim();
    if (!title) { return; }
    this.recipeService.addRecipe({ title: title, parentId, description: recipeDescription} as Recipe)
      .subscribe(() => this.goBack());
  }

  getParentsRecipesById(id: number): void {
    this.recipeService.getParentsRecipesById(id, this.count, this.pageParents)
      .subscribe(recipes => this.recipesParents = recipes);
  }

  getChildrenByParentId(id: number): void {
    this.recipeService.geByParentId(id, this.count, this.pageChildren)
      .subscribe(recipes => this.recipesChildren = recipes);
  }

  nextChildrenPage(): void {
    this.pageChildren++;
    this.getChildrenRecipes();
  }

  prevChildrenPage(): void {
    if (this.pageChildren > 0) {
      this.pageChildren--;
      this.getChildrenRecipes();
    }
  }

  private getChildrenRecipes(): void {
    this.route.paramMap.subscribe(params => {
      const recipeId = +params.get('id')!;
      this.getChildrenByParentId(recipeId);
    });
  }

  nextParentsPage(): void {
    this.pageParents++;
    this.getParentsRecipes();
  }

  prevParentsPage(): void {
    if (this.pageParents > 0) {
      this.pageParents--;
      this.getParentsRecipes();
    }
  }

  private getParentsRecipes(): void {
    this.route.paramMap.subscribe(params => {
      const recipeId = +params.get('id')!;
      this.getParentsRecipesById(recipeId);
    });
  }

  protected readonly moment = moment;
}

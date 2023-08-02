import { Injectable } from '@angular/core';
import { Recipe } from "./recipe";
import { Observable, of } from "rxjs";
import { catchError } from 'rxjs/operators';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  private recipeUrl = 'http://localhost:8080/recipes';

  constructor(
    private http: HttpClient
  ) { }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  getRecipes(count: number, page: number): Observable<Recipe[]> {
    let params = new HttpParams();
    params = params.append('count', count.toString());
    params = params.append('page', page.toString());

    return this.http.get<Recipe[]>(this.recipeUrl, { params })
      .pipe(
        catchError(this.handleError<Recipe[]>('getRecipes', []))
      );
  }

  getRecipe(id: number): Observable<Recipe> {
    const url = `${this.recipeUrl}/${id}`;
    return this.http.get<Recipe>(url)
      .pipe(
        catchError(this.handleError<Recipe>(`getRecipes id=${id}`))
      );

  }

  getParentsRecipesById(id: number, count: number, page: number): Observable<Recipe[]> {
    const url = `${this.recipeUrl}/parents/${id}`;
    return this.getWithParams(url, count, page);
  }

  geByParentId(id: number, count: number, page: number): Observable<Recipe[]> {
    const url = `${this.recipeUrl}/children/${id}`;
    return this.getWithParams(url, count, page);
  }

  updateRecipe(recipe: Recipe): Observable<any> {
    const url = `${this.recipeUrl}/${recipe.id}`;
    return this.http.put(url, recipe, this.httpOptions)
      .pipe(
        catchError(this.handleError<any>(`getRecipes id=${recipe.id}`))
      );
  }

  addRecipe(recipe: Recipe): Observable<Recipe> {
    return this.http.post<Recipe>(this.recipeUrl, recipe, this.httpOptions)
      .pipe(
        catchError(this.handleError<any>(`addRecipe`))
      );
  }

  deleteRecipe(id: number): Observable<Recipe> {
    const url = `${this.recipeUrl}/${id}`;
    return this.http.delete<Recipe>(url, this.httpOptions).pipe(
      catchError(this.handleError<Recipe>('deleteRecipe'))
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      console.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  private getWithParams(url: string, count: number, page: number): Observable<Recipe[]> {
    let params = new HttpParams();
    params = params.append('count', count.toString());
    params = params.append('page', page.toString());
    return this.http.get<Recipe[]>(url, { params })
      .pipe(
        catchError(this.handleError<Recipe[]>(`getRecipes`, []))
      );
  }
}

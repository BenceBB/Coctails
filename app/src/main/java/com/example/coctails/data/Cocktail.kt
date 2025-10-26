package com.example.coctails.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** Data model describing a single cocktail. */
data class Cocktail(
    val id: String,
    val name: String,
    val tagline: String,
    val imageName: String,
    val glass: String,
    val ingredients: List<Ingredient>,
    val preparation: String,
    val garnish: String
)

/** Represents a single ingredient entry for the cocktail. */
data class Ingredient(
    val amount: String,
    val item: String
)

/** Loads cocktail information from the bundled JSON asset. */
object CocktailRepository {
    private const val ASSET_NAME = "cocktails.json"

    fun load(context: Context): List<Cocktail> {
        val json = context.assets.open(ASSET_NAME).bufferedReader().use { it.readText() }
        val root = JSONArray(json)
        return buildList(root.length()) {
            for (index in 0 until root.length()) {
                val item = root.getJSONObject(index)
                add(item.toCocktail())
            }
        }
    }

    private fun JSONObject.toCocktail(): Cocktail {
        val ingredientsArray = getJSONArray("ingredients")
        val ingredients = buildList(ingredientsArray.length()) {
            for (index in 0 until ingredientsArray.length()) {
                val ingredient = ingredientsArray.getJSONObject(index)
                add(
                    Ingredient(
                        amount = ingredient.getString("amount"),
                        item = ingredient.getString("item")
                    )
                )
            }
        }
        return Cocktail(
            id = getString("id"),
            name = getString("name"),
            tagline = getString("tagline"),
            imageName = getString("imageName"),
            glass = getString("glass"),
            ingredients = ingredients,
            preparation = getString("preparation"),
            garnish = getString("garnish")
        )
    }
}

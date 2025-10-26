package com.example.coctails.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.coctails.data.Cocktail
import com.example.coctails.data.CocktailRepository
import com.example.coctails.data.Ingredient
import kotlinx.coroutines.delay

@Composable
fun CocktailApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var cocktails by remember { mutableStateOf<List<Cocktail>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedCocktail by remember { mutableStateOf<Cocktail?>(null) }

    LaunchedEffect(Unit) {
        cocktails = CocktailRepository.load(context)
        isLoading = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isLoading) {
            Text(
                text = "Betöltés...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            CocktailGrid(
                cocktails = cocktails,
                onCocktailSelected = { selectedCocktail = it },
                modifier = Modifier.fillMaxSize()
            )
        }

        selectedCocktail?.let { cocktail ->
            CocktailDetailDialog(
                cocktail = cocktail,
                onDismiss = { selectedCocktail = null }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CocktailGrid(
    cocktails: List<Cocktail>,
    onCocktailSelected: (Cocktail) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(cocktails, key = { it.id }) { cocktail ->
            CocktailCard(
                cocktail = cocktail,
                onClick = { onCocktailSelected(cocktail) }
            )
        }
    }
}

@Composable
private fun CocktailCard(
    cocktail: Cocktail,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageResId = rememberCocktailDrawableId(cocktail.imageName)

    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3f / 4f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = cocktail.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = cocktail.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = cocktail.tagline,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun CocktailDetailDialog(
    cocktail: Cocktail,
    onDismiss: () -> Unit
) {
    val imageResId = rememberCocktailDrawableId(cocktail.imageName)
    Dialog(onDismissRequest = onDismiss) {
        LaunchedEffect(cocktail) {
            delay(20_000)
            onDismiss()
        }

        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = cocktail.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                    )
                }
                Text(
                    text = cocktail.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = cocktail.tagline,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Pohár: ${cocktail.glass}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Hozzávalók",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    cocktail.ingredients.forEach { ingredient ->
                        Text(
                            text = "${ingredient.amount} — ${ingredient.item}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Elkészítés",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = cocktail.preparation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "Díszítés: ${cocktail.garnish}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
private fun CocktailCardPreview() {
    val sample = Cocktail(
        id = "sample",
        name = "Bison Mojito",
        tagline = "Mentás, lime-os long drink buborékos ásványvízzel.",
        imageName = "bison-mojito",
        glass = "Highball",
        ingredients = listOf(
            Ingredient("50 ml", "Żubrówka Bison Grass vodka"),
            Ingredient("1/2 db", "Lime"),
            Ingredient("2 kanál", "Nádcukor")
        ),
        preparation = "Keverd össze a hozzávalókat és töltsd fel ásványvízzel.",
        garnish = "Mentalevél"
    )
    com.example.coctails.ui.theme.CoctailsTheme {
        CocktailCard(cocktail = sample, onClick = {})
    }
}

@Composable
private fun rememberCocktailDrawableId(imageName: String): Int {
    val context = LocalContext.current
    val normalizedName = remember(imageName) { imageName.replace('-', '_') }
    return remember(normalizedName, context) {
        context.resources.getIdentifier(normalizedName, "drawable", context.packageName)
    }
}

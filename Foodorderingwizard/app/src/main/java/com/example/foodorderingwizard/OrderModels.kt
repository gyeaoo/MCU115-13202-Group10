package com.example.foodorderingwizard


data class Order(
    val mainMeal: String? = null,
    val sideDishes: List<String> = emptyList(),
    val drink: String? = null
) {
    fun isComplete(): Boolean =
        mainMeal != null && sideDishes.isNotEmpty() && drink != null
}
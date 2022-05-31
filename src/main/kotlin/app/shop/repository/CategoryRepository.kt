package app.shop.repository

import app.shop.entity.Category

interface CategoryRepository {

    fun getCategory(id: Long?): Category?
    val categories: List<Category?>?
    fun saveCategory(category: Category?): Category?
    fun deleteCategory(id: Long?)
}
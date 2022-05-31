package app.shop.service

import app.shop.entity.Category

interface CategoryService {

    fun getCategory(id: Long): Category?
    fun getAllCategories() : List<Category?>?
    fun saveCategory(category: Category)
    fun updateCategory(category: Category)
    fun deleteCategory(id: Long)
}
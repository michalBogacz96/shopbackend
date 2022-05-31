package app.shop.repository

import app.shop.entity.Product


interface ProductRepository {

    fun getProductById(id: Long?): Product?
    val getAllProducts: List<Product?>?
    fun saveProduct(product: Product?): Product?
    fun deleteProductById(id: Long?)
}
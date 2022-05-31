package app.shop.service

import app.shop.entity.Product


interface ProductService {

    fun getProductById(id: Long) : Product?

    fun getAllProducts() : List<Product?>?

    fun addProduct(product: Product)

    fun updateProduct(product: Product)

    fun deleteProductById(id : Long)

    fun getAllProductsByCategoryId(categoryId : Long) : List<Product?>?
}
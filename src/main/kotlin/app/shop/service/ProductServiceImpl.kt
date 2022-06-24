package app.shop.service


import app.shop.entity.Product
import app.shop.repository.ProductRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ProductServiceImpl : ProductService {

    @Autowired
    lateinit var productRepository: ProductRepository

    val logger: Logger = LoggerFactory.getLogger("UserService")

    @Transactional
    override fun getProductById(id: Long): Product? {
        logger.info("getProductMethod: get product with id: $id")
        return productRepository.getProductById(id)
    }

    @Transactional
    override fun getAllProducts(): List<Product?>? {
        logger.info("getAllProductsMethod: get all products")
        return productRepository.getAllProducts
    }

    @Transactional
    override fun addProduct(product: Product) {
        logger.info("product is added")
        productRepository.saveProduct(product)
    }

    @Transactional
    override fun updateProduct(product: Product) {
        logger.info("user is updated")
        productRepository.saveProduct(product)
    }

    @Transactional
    override fun deleteProductById(id: Long) {
        logger.info("product is deleted with id: $id")
        productRepository.deleteProductById(id)
    }

    @Transactional
    override fun getAllProductsByCategoryId(categoryId: Long): List<Product?>? {
        val allProducts: List<Product?>? = productRepository.getAllProducts
        return allProducts?.filter { product -> product?.categoryEntity!!.id!! == categoryId }?.toList()
    }
}
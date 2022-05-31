package app.shop.controller


import app.shop.entity.Product
import app.shop.service.ProductService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/product")
class ProductController {

    @Autowired
    lateinit var productService: ProductService

    val logger: Logger = LoggerFactory.getLogger("ProductController")

    @GetMapping
    fun getAllProducts(): List<Product?>? {
        logger.info("getAllProducts method")
        return productService.getAllProducts()
    }


    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
    @GetMapping("/category/{categoryId}")
    fun getAllProductsByCategoryId(@PathVariable categoryId : Long): List<Product?>? {
        logger.info("getAllProductsByCategoryId method")
        val myVar =  productService.getAllProductsByCategoryId(categoryId)

        return myVar
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): Product? {
        logger.info("getAllProducts method")
        return productService.getProductById(id)!!
    }

    @PostMapping
    fun addProduct(@RequestBody product: Product) {
        logger.info("addProduct method")
        productService.addProduct(product)
    }


    @PutMapping
    fun updateProduct(@RequestBody product: Product) {
        logger.info("updateProduct method")
        productService.updateProduct(product)
    }

    @DeleteMapping("/{id}")
    fun deleteProductById(@PathVariable id : Long) {
        logger.info("deleteProduct method")
        productService.deleteProductById(id)
    }
}
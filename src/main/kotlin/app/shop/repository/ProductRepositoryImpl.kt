package app.shop.repository

import app.shop.entity.Product
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl : ProductRepository {

    @Autowired
    private val sessionFactory: SessionFactory? = null

    override fun getProductById(id: Long?): Product? {
        val session = sessionFactory!!.currentSession!!
        return session.get(Product::class.java, id)!!
    }

    override val getAllProducts: List<Product>
        get() {
            val session = sessionFactory!!.currentSession
            return session.createQuery("from Product ", Product::class.java).list()
        }

    override fun saveProduct(product: Product?): Product? {
        val session = sessionFactory!!.currentSession
        session.saveOrUpdate(product)
        return product
    }

    override fun deleteProductById(id: Long?) {
        val session = sessionFactory!!.currentSession
        val carEntity = session.get(Product::class.java, id)
        session.delete(carEntity)
    }

    override fun getProductPrice(product: Long): Long {
        val product = getProductById(product)!!
        return product.price!!.toLong()
    }
}
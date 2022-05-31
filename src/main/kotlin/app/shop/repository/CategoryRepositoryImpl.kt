package app.shop.repository

import app.shop.entity.Category
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class CategoryRepositoryImpl : CategoryRepository {


    @Autowired
    private val sessionFactory: SessionFactory? = null
    override fun getCategory(id: Long?): Category? {
        val session = sessionFactory!!.currentSession
        val categoryList =
            session.createQuery("from Category", Category::class.java).list()
        return categoryList.stream().filter { category: Category? -> category!!.id == id }.findFirst().get()
    }

    override val categories: List<Category>
        get() {
            val session = sessionFactory!!.currentSession
            return session.createQuery("from Category", Category::class.java).list()
        }

    override fun saveCategory(category: Category?): Category? {
        val session = sessionFactory!!.currentSession
        session.saveOrUpdate(category)
        return category
    }

    override fun deleteCategory(id: Long?) {
        val session = sessionFactory!!.currentSession
        val carEntity = session.get(Category::class.java, id)
        session.delete(carEntity)
    }
}
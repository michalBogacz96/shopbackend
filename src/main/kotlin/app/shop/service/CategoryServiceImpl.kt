package app.shop.service

import app.shop.entity.Category
import app.shop.repository.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CategoryServiceImpl : CategoryService {

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Transactional
    override fun getCategory(id: Long): Category? {
        return categoryRepository.getCategory(id)
    }

    @Transactional
    override fun getAllCategories(): List<Category?>? {
        return categoryRepository.categories
    }

    @Transactional
    override fun saveCategory(category: Category) {
        categoryRepository.saveCategory(category)
    }

    @Transactional
    override fun updateCategory(category: Category) {
        categoryRepository.saveCategory(category)
    }

    @Transactional
    override fun deleteCategory(id: Long) {
        categoryRepository.deleteCategory(id)
    }
}
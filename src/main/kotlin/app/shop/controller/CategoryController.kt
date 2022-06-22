package app.shop.controller


import app.shop.entity.Category
import app.shop.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/category")
class CategoryController {


    @Autowired
    private val categoryService: CategoryService? = null

    @CrossOrigin(origins = ["http://localhost:3000"], allowedHeaders = ["Authorization"])
    @GetMapping("/{id}")
    fun getCategory(@PathVariable id: Long?): Category? {
        return categoryService!!.getCategory(id!!)
    }

    @CrossOrigin(origins = ["http://localhost:3000"], allowedHeaders = ["Authorization"])
    @GetMapping
    fun getAllCategories(): List<Category?>? {
        return categoryService!!.getAllCategories()
    }


    @PostMapping
    fun addCategory(@RequestBody category: Category?) {
        return categoryService!!.saveCategory(category!!)
    }

    @PutMapping("{id}")
    fun updateCategory(@RequestBody category: Category) {
        categoryService!!.saveCategory(category)
    }

    @DeleteMapping("{id}")
    fun deleteCategory(@PathVariable id: Long) {
        categoryService!!.deleteCategory(id)
    }
}
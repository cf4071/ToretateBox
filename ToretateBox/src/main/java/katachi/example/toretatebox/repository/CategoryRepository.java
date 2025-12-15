package katachi.example.toretatebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import katachi.example.toretatebox.domain.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
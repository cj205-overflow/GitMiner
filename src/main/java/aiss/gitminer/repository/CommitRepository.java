package aiss.gitminer.repository;

import aiss.gitminer.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitRepository extends JpaRepository<Commit, String> {
    // Este repositorio hereda de JpaRepository, lo que le proporciona métodos CRUD básicos como findAll(), findById(), save(), deleteById(), etc.
    // Aquí puedes definir métodos personalizados para consultas específicas si es necesario
    // Por ejemplo, puedes buscar commits por autor, fecha, etc.
    // public List<Commit> findByAuthorName(String authorName);
}

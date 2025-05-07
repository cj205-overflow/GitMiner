package aiss.gitminer.repository;

import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, String> {

    @Query(value = "SELECT * FROM comment WHERE issue_id = :issueId",
            countQuery = "SELECT COUNT(*) FROM comment WHERE issue_id = :issueId",
            nativeQuery = true)
    Page<Comment> findCommentsByIssueId(@Param("issueId") String issueId, Pageable pageable);

    //Definimos los métodos para el filtrado de las issues por estado y autorId
    Page<Issue> findByState(String state, Pageable pageable);
    Page<Issue> findByAuthorId(String authorId, Pageable pageable);
    Page<Issue> findByStateAndAuthorId(String state, String authorId, Pageable pageable);
}

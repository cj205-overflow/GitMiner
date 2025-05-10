package aiss.gitminer.repository;

import aiss.gitminer.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    @Query(value = "SELECT * FROM comment WHERE issue_Id = :issueId",
            nativeQuery = true)
    Page<Comment> findCommentsByIssueId(@Param("issueId") String issueId, Pageable pageable);

}


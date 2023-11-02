package com.hrproject.repository;

import com.hrproject.repository.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCompanyId(Long companyId);
    List<Comment> findAllByUserId(String userId);

}

package com.file.uploader.repository;

import com.file.uploader.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    // Jpa for basic CRUD with Files
    Optional<File> findById (Long id);
    List<File> findAllByAuthorId(Long id);
}
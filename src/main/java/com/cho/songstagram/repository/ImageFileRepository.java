package com.cho.songstagram.repository;

import com.cho.songstagram.domain.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile,String> {
    Optional<ImageFile> findById(String id);
}

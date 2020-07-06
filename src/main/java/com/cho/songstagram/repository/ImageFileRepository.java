package com.cho.songstagram.repository;

import com.cho.songstagram.domain.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository extends JpaRepository<ImageFile,Long> {
}

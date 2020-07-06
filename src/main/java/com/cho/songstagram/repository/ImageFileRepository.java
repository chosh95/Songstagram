package com.cho.songstagram.repository;

import com.cho.songstagram.domain.ImageFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile,String> {

}

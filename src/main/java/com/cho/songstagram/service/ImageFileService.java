package com.cho.songstagram.service;

import com.cho.songstagram.domain.ImageFile;
import com.cho.songstagram.repository.ImageFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    public void save(ImageFile file){
        imageFileRepository.save(file);
    }

    public ImageFile getFile(String fileId) throws FileNotFoundException {
        return imageFileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("file not found"));
    }
}

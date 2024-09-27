package com.syvodid.webservice;

import com.syvodid.webservice.models.Image;
import com.syvodid.webservice.repo.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public void saveImage(Image image) {
        imageRepository.save(image);
    }
}

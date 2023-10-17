package com.ndmkcn.springbootmongodb.service;

import com.ndmkcn.springbootmongodb.collection.Photo;
import com.ndmkcn.springbootmongodb.dto.PhotoDTO;
import com.ndmkcn.springbootmongodb.mapper.PhotoMapper;
import com.ndmkcn.springbootmongodb.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {
    private final Logger logger= LoggerFactory.getLogger(PhotoService.class);
    private final PhotoRepository photoRepository;

    public PhotoDTO save(String originalFilename, MultipartFile image) throws IOException {
        Photo photo=new Photo();
        photo.setTitle(originalFilename);
        photo.setPhoto(new Binary(BsonBinarySubType.BINARY,image.getBytes()));
        return PhotoMapper.toDTO(this.photoRepository.save(photo));
    }

    public Photo getPhoto(String id) {
        return this.photoRepository.findById(id).get();
    }
}

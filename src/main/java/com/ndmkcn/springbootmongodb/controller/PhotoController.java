package com.ndmkcn.springbootmongodb.controller;

import com.ndmkcn.springbootmongodb.collection.Photo;
import com.ndmkcn.springbootmongodb.dto.PhotoDTO;
import com.ndmkcn.springbootmongodb.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/photo")
@RequiredArgsConstructor
public class PhotoController {
    private final Logger logger= LoggerFactory.getLogger(PhotoController.class);
    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<PhotoDTO> savePhoto(@RequestParam(name = "image") MultipartFile image) {
        try {
            PhotoDTO photoDTO1=this.photoService.save(image.getOriginalFilename(),image);
            logger.debug("REST request to save Photo : {}", photoDTO1);
            return new ResponseEntity<>(photoDTO1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable(name = "id") String id) {
        Photo photo=this.photoService.getPhoto(id);
        Resource resource=new ByteArrayResource(photo.getPhoto().getData());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+photo.getTitle()+"\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}

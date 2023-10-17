package com.ndmkcn.springbootmongodb.mapper;

import com.ndmkcn.springbootmongodb.collection.Photo;
import com.ndmkcn.springbootmongodb.dto.PhotoDTO;

public class PhotoMapper {
    public static PhotoDTO toDTO(Photo photo) {
        PhotoDTO photoDTO= new PhotoDTO();
        photoDTO.setId(photo.getId());
        photoDTO.setTitle(photo.getTitle());
        photoDTO.setPhoto(photo.getPhoto());
        return photoDTO;
    }
}

package com.star.service.impl;

import com.star.domain.Picture;
import com.star.mapper.PictureMapper;
import com.star.service.IPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureService implements IPictureService {

    @Autowired
    PictureMapper pictureMapper;

    @Override
    public List<Picture> listPicture() {
        return pictureMapper.listPicture();
    }

    @Override
    public int savePicture(Picture picture) {
        return pictureMapper.savePicture(picture);
    }

    @Override
    public Picture getPicture(Long id) {
        return pictureMapper.getPicture(id);
    }

    @Override
    public int updatePicture(Picture picture) {
        return pictureMapper.updatePicture(picture);
    }

    @Override
    public void deletePicture(Long id) {
        pictureMapper.deletePicture(id);
    }
}

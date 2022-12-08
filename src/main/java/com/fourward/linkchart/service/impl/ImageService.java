package com.fourward.linkchart.service.impl;

import com.fourward.linkchart.dto.ImageDTO;
import com.fourward.linkchart.persistence.mapper.ImageMapper;
import com.fourward.linkchart.service.IImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ImageService")
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageMapper imageMapper;

    @Override
    public ImageDTO getImageByImageName(String name) {
        return imageMapper.getImageDetail(name + ".png");
    }

}
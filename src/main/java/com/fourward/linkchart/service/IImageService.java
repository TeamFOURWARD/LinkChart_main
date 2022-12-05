package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.ImageDto;

public interface IImageService {
    ImageDto getImageByImageName(String name);
}

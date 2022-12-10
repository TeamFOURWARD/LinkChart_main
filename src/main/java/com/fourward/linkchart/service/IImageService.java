package com.fourward.linkchart.service;

import com.fourward.linkchart.dto.ImageDTO;

public interface IImageService {
    ImageDTO getImageByImageName(String name);
}

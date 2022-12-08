package com.fourward.linkchart.persistence.mapper;

import com.fourward.linkchart.dto.ImageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImageMapper {
    ImageDTO getImageDetail(@Param("imgSaveName") String imgName);
}
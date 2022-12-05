package com.fourward.linkchart.persistence.mapper;

import com.fourward.linkchart.dto.ImageDto;
import com.fourward.linkchart.dto.StockDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageMapper {
    ImageDto getImageDetail(@Param("imgSaveName") String imgName);
}
package com.fourward.linkchart.persistence.mapper;

import com.fourward.linkchart.dto.NewsDTO;
<<<<<<< HEAD
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface INewsMapper {

    //웹페이지에서 뉴스 정보 가져오기
    //Mapper 호출
   int InsertNewsInfo(NewsDTO pDTO) throws Exception;
=======

public interface INewsMapper {

    int InsertNewsInfo(NewsDTO pDTO) throws Exception;
>>>>>>> origin/wvbm_school
}

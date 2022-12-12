package com.fourward.linkchart.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public class WordcloudDTO {
    private ArrayList<HashMap<String,Object>> newsList;
    private String word;
}

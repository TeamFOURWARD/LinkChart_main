function getNews_click() {
    const date = document.getElementById("news_date").value;
    const keyword = document.getElementById("news_keyword").value
    if (keyword !== "") {
        return getNewsData(keyword, date, true);
    }
    alert("키워드를 입력해주세요.");
}

/**
 * @param keyword String. 검색할 뉴스 이름.
 * @param date String. 검색할 날짜. null 일시 서버에서 현재 날짜로 처리.
 * @param condition boolean. alert 표시 여부. 페이지 로딩시 처리되는 함수만 false.
 */
function getNewsData(keyword, date, condition) {
    $.ajax({
        url: "/news/getNewsData",
        data: {
            "keyword": keyword,
            "date": date
        },
        type: "GET",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        async: true,
        statusCode: {
            500: () => alert("서버에 문제가 생겼습니다. 잠시후 다시 시도해주세요.")
        },
        success: function (data) {
            if (data.length !== 0) {
                if (data.image) {
                    // document.getElementById('imgTest').src = data.image.img_save_path + data.image.img_save_name;
                }
                wordcloud(data.wordcloud)
                return loadNews(data.newsList);
            } else if (condition) {
                alert("표시할 뉴스가 없습니다.");
            }
        }
    });
}

// 데이터 구조 : List<Map<String,Object( List<NewsRelatedDTO> or NewsDTO )>>
function loadNews(data) {
    let table = document.createElement('table');
    let tbody = document.createElement('tbody');
    $('#newsMain').empty();
    table.appendChild(tbody);

    data.forEach(newsGroup_list => {
            let tr = document.createElement('tr');
            tbody.appendChild(tr);
            {
                const divList = document.createElement('div');
                tr.appendChild(divList);
                divList.setAttribute('class', 'newsMain_div');// 뉴스 리스트가 각각 삽입됨.
                {
                    // 기사 썸네일 (null 가능)
                    if (newsGroup_list.news.thumb != null) {
                        const tdThumb = document.createElement('td');
                        divList.appendChild(tdThumb);
                        {
                            const divThumb = document.createElement('div');
                            tdThumb.appendChild(divThumb);
                            {
                                const aThumb = document.createElement('a');
                                divThumb.appendChild(aThumb);
                                {
                                    aThumb.setAttribute('href', newsGroup_list.news.link.toString());
                                }
                                const imgThumb = document.createElement('img');
                                aThumb.appendChild(imgThumb);
                                {
                                    imgThumb.setAttribute('width', '100');
                                    imgThumb.setAttribute('height', '90');
                                    imgThumb.setAttribute('src', newsGroup_list.news.thumb);
                                    imgThumb.setAttribute('width', '100');
                                    imgThumb.setAttribute('alt', '이미지 없음');
                                }
                            }
                        }
                        // 메인 기사
                        const tdContents = document.createElement('td');
                        divList.appendChild(tdContents);
                        {
                            const ulContents = document.createElement('ul');
                            tdContents.appendChild(ulContents);
                            {
                                // 기사 헤드&링크
                                const liHead = document.createElement('li');
                                ulContents.appendChild(liHead);
                                {
                                    const divHead = document.createElement('div');
                                    liHead.appendChild(divHead);
                                    {
                                        const aHead = document.createElement('a');
                                        divHead.appendChild(aHead);
                                        {
                                            aHead.setAttribute('href', newsGroup_list.news.link);
                                        }

                                        const strongHead = document.createElement('strong');
                                        aHead.appendChild(strongHead);
                                        {
                                            strongHead.innerText = newsGroup_list.news.head;
                                        }
                                    }
                                }
                                // 기사 본문 & 날짜 & 언론사
                                const liBody = document.createElement('li');
                                ulContents.appendChild(liBody);
                                {
                                    const divBody = document.createElement('div');
                                    liBody.appendChild(divBody);
                                    {
                                        divBody.innerHTML = (
                                            newsGroup_list.news.summary + '<br>' + newsGroup_list.news.date + '<br>' +
                                            newsGroup_list.news.publisher);
                                    }
                                }

                            }
                        }
                        /*if(newsGroup_list.newsGroup_related_list.length!==0){
                            const accordion_list_btn = document.createElement('button');
                            accordion_list_btn.setAttribute("class", "accordion");
                            accordion_list_btn.innerText = "연관기사";
                            divList.appendChild(accordion_list_btn);

                            const accordion_list = document.createElement('div');
                            accordion_list.setAttribute("class", "panel");
                            divList.appendChild(accordion_list);
                            {
                                // 연관기사 리스트
                                const tdRelated_list = document.createElement('td');
                                accordion_list.appendChild(tdRelated_list);
                                {
                                    const olRelated_list = document.createElement('ol');
                                    tdRelated_list.appendChild(olRelated_list);
                                    {
                                        newsGroup_list.newsGroup_related_list.forEach(newsRelated_list => {
                                            const liRelatedBody = document.createElement('li');
                                            olRelated_list.appendChild(liRelatedBody);
                                            {
                                                // 연관기사 본문 & 링크 & 언론사명 & 날짜
                                                const divRelatedBody = document.createElement('div');
                                                liRelatedBody.appendChild(divRelatedBody);
                                                {
                                                    divRelatedBody.innerHTML = (
                                                        '<a href=' + newsRelated_list.link_related + '>' +
                                                        newsRelated_list.head_related +
                                                        '</a>' +
                                                        '<br/><hr/>' +
                                                        newsRelated_list.publisher_related +
                                                        '<br/><hr/>' +
                                                        newsRelated_list.date_related +
                                                        '<br/><hr/>'
                                                    );
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }*/
                    }
                }
            }
        }
    );
    document.getElementById('newsMain').appendChild(table);
}

function wordcloud(data) {
    /*console.log(data)
    const obj = JSON.parse(JSON.stringify(data)); // JSON 문자열 javascript 객체로 변환
    console.log(obj);*/
    var a = []
    data.forEach(b => {
        const c = []
        c.push(String(b[0]))
        c.push(parseInt(b[1]))
        a.push(c)
    })
    $("#wordcloud_container").empty();
    var chart = anychart.tagCloud(a);
    chart.title("기사 한눈에 보기");
    chart.container("wordcloud_container");

    chart.hovered().fill("#8711c3");
    chart.mode("spiral"); // 모양

    // 글자 돌아가는거 막기
    chart.angles([0]);

    chart.draw();
}
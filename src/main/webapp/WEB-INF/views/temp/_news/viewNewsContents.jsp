<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.fourward.linkchart.dto.NewsDTO" %>
<%@ page import="com.fourward.linkchart.dto.NewsRelatedDTO" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%--deprecated--%>
<%
    List<HashMap<String, Object>> newsGroup_list = (List<HashMap<String, Object>>) request.getAttribute("newsData");
%>
<script type="text/javascript">
    console.log('<%=newsGroup_list%>');
</script>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>뉴스 보기</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <!-- jQuery library -->
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
    <!-- Popper JS -->
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
</head>
<body>
<div>
    <%-- 데이터 확인용 코드    <%=request.getAttribute("newsData")%>--%>
    <%--    키워드와 날짜 표기--%>
        <ul>
            <li>
                <p>
                    검색 키워드 : <%=request.getAttribute("keyword")%>
                </p>
            </li>
            <li>
                <p>
                    <%
                        String s =request.getAttribute("date").toString();
                        String y = s.substring(0,4);
                        String m = s.substring(4,6);
                        String d = s.substring(6);
                    %>
                    <%=y%> 년 <%=m%> 월 <%=d%> 일
                </p>
            </li>
        </ul>

        <table class="table">
            <tbody>
                <%
//                        바깥 반복문. tr(기사목록) 을 반복 생성
                for (HashMap<String,Object> newsGroup_map:newsGroup_list) {
                    NewsDTO rNewsDTO = (NewsDTO) newsGroup_map.get("_news");
                %>

            <tr>
                <td>
                    <%-- "#" 내용은 가이드라인 --%>
                    <div>
                        <%
                            if (rNewsDTO.getThumb() != null) {
                        %>
                        <a href="<%=rNewsDTO.getLink()%>"><img width="100" height="100" src="<%=rNewsDTO.getThumb()%>" alt="썸네일 없음"></a>
                        <%}%>
                    </div>
                </td>
                <td class="n_article">
                    <ul>
                        <li class="linc1">
                            <div>
                                <a href="<%=rNewsDTO.getLink()%>"> <strong><%=rNewsDTO.getHead()%>
                                </strong>
                                </a>
                            </div>
                        </li>
                        <li class="linc2">
                            <ul>
                                <li class="gs">
                                    <%=rNewsDTO.getSummary()%>
                                </li>
                                <li class="gd">
                                    <%=rNewsDTO.getDate()%>
                                </li>
                                <li class="gp">
                                    <%=rNewsDTO.getPublisher()%>
                                </li>
                                <%--<%=rNewsDTO.getSummary()%>
                                <br/>
                                <hr/>
                                <%=rNewsDTO.getDate()%>
                                <br/>
                                <hr/>
                                <%=rNewsDTO.getPublisher()%>--%>
                            </ul>
                        </li>
                    </ul>
                </td>
                <td class="ol_wrap">
                    <ol>
                        <%
                            // 안쪽 반복문. 연관기사 목록(li)을 생성
                            ArrayList<NewsRelatedDTO> rNewsRelated_list = (ArrayList<NewsRelatedDTO>) newsGroup_map.get("newsGroup_related_list");
                            for (NewsRelatedDTO rNewsRelatedDTO : rNewsRelated_list) {
                        %>
                        <li>
                            <ul>
                                <li>
                                    <a href="<%=rNewsRelatedDTO.getLink_related()%>"><%=rNewsRelatedDTO.getHead_related()%></a>
                                </li>
                                <li>
                                    <%=rNewsRelatedDTO.getPublisher_related()%>
                                </li>
                                <li>
                                    <%=rNewsRelatedDTO.getDate_related()%>
                                </li>
                                <%--<a href="<%=rNewsRelatedDTO.getLink_related()%>"><%=rNewsRelatedDTO.getHead_related()%>
                                </a>
                                <br/>
                                <hr/>
                                <%=rNewsRelatedDTO.getPublisher_related()%>
                                <br/>
                                <hr/>
                                <%=rNewsRelatedDTO.getDate_related()%>
                                <br/>
                                <hr/>--%>
                            </ul>
                        </li>
                        <%
                            }
                        %>
                    </ol>
                </td>
                <td>
                    <a href="#">
                        <i class="fa-solid fa-share"></i>
                    </a>
                </td>
                <td>
                    <a href="#">
                        <i class="fa-regular fa-heart"></i>
                    </a>
                </td>
            </tr>
                <%
                }
                %>
        </table>
</body>
</html>
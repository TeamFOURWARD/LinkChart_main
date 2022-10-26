<%@ page contentType="text/html; charset=utf-8" %>
<script type="text/javascript">
    console.log(<%=request.getAttribute("newsData")%>)
</script>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>뉴스 보기</title>
    <link rel="stylesheet" href="/css/reset.css">
    <link rel="stylesheet" href="/css/all.min.css">
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
<div style="text-align: center">
    <h1>뉴스 크롤링부분</h1>
        <%=request.getAttribute("newsData")%>

    <div class="main_section">
        <div class="container">
            <div class="container table_container">
                <table class="table">
                    <tbody>
                    <tr>
                        <td>썸네일이미지 & 링크</td>
                        <td>기사 내용
                            <ul>
                                <li>
                                    <div>
                                        기사 헤드 & 링크
                                    </div>
                                </li>
                                <li>
                                    <div>
                                        기사 본문 텍스트 & 날짜 & 작성 언론사명
                                    </div>
                                </li>
                            </ul>
                        </td>
                        <td>
                            연관기사 목록 들
                            <ol>
                                <li>
                                    연관기사 j
                                    <div>
                                        연관기사 텍스트 & 링크 & 언론사명 & 날짜ㅓ
                                    </div>
                                </li>
                                <li>
                                    연관기사 j
                                    <div>
                                    </div>
                                </li>
                                ...
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
                </table>
            </div>
        </div>
    </div>
</body>
</html>
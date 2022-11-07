<%@ page language="java" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta
      name="viewport"
      content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
    />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <link rel="stylesheet" href="/css/reset.css" />
    <link rel="stylesheet" href="/css/all.min.css" />
    <title>종목 데이터 가져오기</title>
  </head>
  <body>
    <h1>가져올 종목 이름, 시작 날짜 입력</h1>
    <form
      name="f"
      method="get"
      action="${pageContext.request.contextPath}/chart/insertStockData"
    >
      종목명(정확히입력) : <input name="name" type="text" width="20" />
      <br />
      시작날짜 :
      <input name="start_date" type="text" placeholder="예 : 20200101 형식" />
      <br />
      <input type="submit" value="전송" />
      <input type="reset" value="초기화" />
    </form>
  </body>
</html>

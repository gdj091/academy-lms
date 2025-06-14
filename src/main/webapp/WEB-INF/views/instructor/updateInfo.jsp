<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<meta charset="UTF-8">
 <link rel="stylesheet" href="/css/styles.css">
<title>Insert title here</title>
</head>
<body>
  <div class="top-bar">
    <div class="logo">MyLMS</div>
    <div class="user-name">홍길동님</div>
  </div>

  <div class="layout">
    <div class="side-bar">
      <ul>
        <li><a href="#">메인페이지</a></li>
        <li><a href="/instructor/myPage">내 개인정보</a></li>
        <li><a href="/instructor/updateInfo">개인정보 수정</a></li>
        <li><a href="/instructor/updatePw">비밀번호 변경</a></li>
        <li><a href="/logOut">로그아웃</a></li>
      </ul>
    </div>
    
    
<main>
   	<h2>내 개인정보 수정</h2>
	<form action="/instructor/updateInfo" method="post">
		  <table>
		    <tr>
		      <td><label for="name">이름</label></td>
		      <td><input type="text" id="name" name="name" value="${myPage.name}" readonly></td>
		    </tr>
		    <tr>
		      <td><label for="email">이메일</label></td>
		      <td><input type="email" id="email" name="email" value="${myPage.email}" required></td>
		    </tr>
		    <tr>
		      <td><label for="phone">휴대폰 번호</label></td>
		      <td><input type="text" id="phone" name="phone" value="${myPage.phone}" required></td>
		    </tr>
		    <tr>
		      <td><label for="birth">생년월일</label></td>
		      <td><input type="date" id="birth" name="birth" value="${myPage.birth}" readonly></td>
		    </tr>
		    <tr>
		      <td colspan="2">
		        <button type="submit">수정 완료</button>
		      </td>
		    </tr>
		  </table>
	</form>
    	
    	
</main>
    
</body>
</html>
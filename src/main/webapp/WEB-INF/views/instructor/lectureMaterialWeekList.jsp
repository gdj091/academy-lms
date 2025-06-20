<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="../css/lmsStyle.css">
<meta charset="UTF-8">
<title>AcademyLMS</title>
</head>
<body>
<div>
	<jsp:include page ="../nav/sideNav.jsp">
		<jsp:param name="lectureId" value="${lectureId}" />
	</jsp:include>
	
</div>
<main>
  	<span class="quiz-list-title">강의자료 주차별 리스트</span>
   <ul style="list-style-type: none; padding-left: 0; margin-top: 20px;">
        <c:forEach var="week" items="${weekList}">
            <li style="margin-bottom: 10px;">
                <a href="/lectureMaterialList?weekId=${week.weekId}" 
                   style="font-size: 16px; font-weight: 500; color: var(--primary);">
                    📘 ${week.week}주차 강의자료
                </a>
            </li>
        </c:forEach>
    </ul>

    <!-- 뒤로가기 링크 -->
    <div style="margin-top: 20px;">
        <a href="/instructor/lectureOne?lectureId=${lectureId}" 
           style="font-weight: bold; color: #333;">
            ← 강의정보로 돌아가기
        </a>
    </div>
</main>
</body>
</html>
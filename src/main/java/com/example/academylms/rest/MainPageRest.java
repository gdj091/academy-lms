package com.example.academylms.rest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.academylms.service.MainPageService;

import jakarta.servlet.http.HttpSession;

@RestController
public class MainPageRest {

    @Autowired
    private MainPageService mainPageService;

    @GetMapping("/api/mainLectureList")
    public Map<String, List<Map<String, Object>>> getLectureListForMainRest(HttpSession session) {
        String role = (String) session.getAttribute("role");
        Integer userId = (Integer) session.getAttribute("user_id");

        List<Map<String, Object>> lectureList = new ArrayList<>();

        if ("admin".equals(role)) {
            lectureList = mainPageService.getAllLectures();
        } else if ("instructor".equals(role)) {
            lectureList = mainPageService.getLecturesByInstructor(userId);
        } else if ("student".equals(role)) {
            lectureList = mainPageService.getLecturesByStudent(userId);
        }

        // 상태별 분류
        List<Map<String, Object>> ongoingLectures = new ArrayList<>();
        List<Map<String, Object>> upcomingLectures = new ArrayList<>();
        List<Map<String, Object>> endedLectures = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Map<String, Object> lecture : lectureList) {
            LocalDateTime startedAt = ((Timestamp)lecture.get("started_at")).toLocalDateTime();
            LocalDateTime endedAt = ((Timestamp)lecture.get("ended_at")).toLocalDateTime();

            if (now.isBefore(startedAt)) {
                upcomingLectures.add(lecture);
            } else if (now.isAfter(endedAt)) {
                endedLectures.add(lecture);
            } else {
                ongoingLectures.add(lecture);
            }
        }

        Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();
        resultMap.put("ongoing", ongoingLectures);
        resultMap.put("upcoming", upcomingLectures);
        resultMap.put("ended", endedLectures);

        return resultMap;
    }
}
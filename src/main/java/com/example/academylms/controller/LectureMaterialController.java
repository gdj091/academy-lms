package com.example.academylms.controller;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.academylms.dto.LectureMaterial;
import com.example.academylms.dto.LectureWeek;
import com.example.academylms.dto.User;
import com.example.academylms.mapper.LectureMaterialMapper;
import com.example.academylms.mapper.QnaMapper;
import com.example.academylms.service.LectureMaterialService;
import com.example.academylms.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LectureMaterialController {
    @Autowired
    private LectureMaterialService lectureMaterialService;
    
    @Autowired
    private LectureMaterialMapper lectureMaterialMapper;
    
    @Autowired
    private QnaMapper qnaMapper;
    
    @Autowired
    private LoginService loginService;

// 강의자료리스트    
    @GetMapping("/lectureMaterialList")
    public String lectureMaterialList(@RequestParam int weekId, Model model, HttpSession session, HttpServletRequest request) {
    	// 세션정보
    	Object userIdObj = session.getAttribute("loginUserId");
    	if (userIdObj == null) {
    		return "redirect:/login";
    	}

    	int userId = (int) userIdObj;
    	User user = loginService.findById(userId);
    	String role = user.getRole();
        
        List<LectureMaterial> materialList = lectureMaterialService.getLectureMaterialsByWeek(weekId);
        model.addAttribute("materialList", materialList);
        model.addAttribute("weekId", weekId);

        // 주차 정보에서 강의 ID, 주차 번호 추출 & 리스트 상단 강의정보표시
        LectureWeek weekInfo = lectureMaterialMapper.getLectureWeekById(weekId); 
        if (weekInfo != null) {
            model.addAttribute("week", weekInfo.getWeek());  // JSP에서 사용할 ${week}
            model.addAttribute("lectureId", weekInfo.getLectureId()); 
            int lectureId = weekInfo.getLectureId();
            Map<String, Object> lectureInfoMap = qnaMapper.getLectureInfoByLectureId(lectureId);
            request.setAttribute("lectureTitle", lectureInfoMap.get("title"));
            request.setAttribute("lectureDay", lectureInfoMap.get("day"));
            request.setAttribute("lectureTime", lectureInfoMap.get("time"));
        }
        
        System.out.println("📌 weekInfo.getLectureId() = " + weekInfo.getLectureId());
        if ("instructor".equals(role)) {
            return "instructor/lectureMaterialList";
        } else if ("student".equals(role)) {
            return "student/lectureMaterialList";
        } else if ("admin".equals(role)) {
            return "admin/lectureMaterialList";
        } else {
            return "lectureOne";
        }
    }

// 강의자료상세정보
    @GetMapping("/lectureMaterialOne")
    public String lectureMaterialDetail(@RequestParam int materialId,
                                        HttpServletRequest request,
                                        Model model) {
    	// 세션정보
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("loginUserId");
        User user = loginService.findById(userId);
        String role = user.getRole();
        
        LectureMaterial material = lectureMaterialService.getLectureMaterialById(materialId);
        model.addAttribute("material", material);
        request.setAttribute("loginRole", role);

        // 여기서 weekId를 material에서 꺼내서 이후 redirect 등에 써야 함
        int weekId = material.getWeekId();
        model.addAttribute("weekId", weekId);
        
        // 주차 정보에서 강의 ID, 주차 번호 추출 & 리스트 상단 강의정보표시
        LectureWeek weekInfo = lectureMaterialMapper.getLectureWeekById(weekId); 
        if (weekInfo != null) {
            model.addAttribute("week", weekInfo.getWeek());  // JSP에서 사용할 ${week}
            model.addAttribute("lectureId", weekInfo.getLectureId()); 
            int lectureId = weekInfo.getLectureId();
            Map<String, Object> lectureInfoMap = qnaMapper.getLectureInfoByLectureId(lectureId);
            request.setAttribute("lectureTitle", lectureInfoMap.get("title"));
            request.setAttribute("lectureDay", lectureInfoMap.get("day"));
            request.setAttribute("lectureTime", lectureInfoMap.get("time"));
        }
        

        if ("instructor".equals(role)) {
            return "instructor/lectureMaterialOne";
        } else if ("student".equals(role)) {
            return "student/lectureMaterialOne";
        } else if ("admin".equals(role)) {
            return "admin/lectureMaterialOne";
        }	else {
            return "lectureMaterialList";
        }
    }
    
// 강의자료추가
    @GetMapping("/addLectureMaterial")
    public String lectureMaterialAddForm(@RequestParam int weekId, Model model, HttpServletRequest request, HttpSession session) {
    	// 세션정보
    	Object userIdObj = session.getAttribute("loginUserId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = (int) userIdObj;
        User user = loginService.findById(userId);
        String role = user.getRole();
        
        // 주차 정보에서 강의 ID, 주차 번호 추출 & 리스트 상단 강의정보표시
        LectureWeek weekInfo = lectureMaterialMapper.getLectureWeekById(weekId); 
        if (weekInfo != null) {
            model.addAttribute("week", weekInfo.getWeek());  // JSP에서 사용할 ${week}
            model.addAttribute("lectureId", weekInfo.getLectureId()); 
            int lectureId = weekInfo.getLectureId();
            Map<String, Object> lectureInfoMap = qnaMapper.getLectureInfoByLectureId(lectureId);
            request.setAttribute("lectureTitle", lectureInfoMap.get("title"));
            request.setAttribute("lectureDay", lectureInfoMap.get("day"));
            request.setAttribute("lectureTime", lectureInfoMap.get("time"));
        }
        

        // 학생은 사용불가
        if (!"instructor".equals(role) && !"admin".equals(role)) {
            return "redirect:/lectureMaterialList?weekId=" + weekId;
        }

        model.addAttribute("weekId", weekId);
        return "/instructor/addLectureMaterial";
    }

// 강의자료추가    
    @PostMapping("/addLectureMaterial")
    public String lectureMaterialAdd(@RequestParam int weekId,
                                     @RequestParam("titles") List<String> titles,
                                     @RequestParam("files") MultipartFile[] files,
                                     HttpSession session) throws IOException {
    	//유효성 체크
        if (titles == null || titles.isEmpty()) return "redirect:/lectureMaterialWeekList";
        if (files == null || files.length == 0) return "redirect:/lectureMaterialWeekList";

        // 세션정보
        Object userIdObj = session.getAttribute("loginUserId");
        if (userIdObj == null) {
        	return "redirect:/login";
        }

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String title = titles.get(i);

            if (file != null && !file.isEmpty() && title != null && !title.trim().isEmpty()) {
                String uploadDir = "/home/ubuntu/upload";
                String originalFilename = file.getOriginalFilename();
                String savedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
                File targetFile = new File(uploadDir, savedFilename);
                file.transferTo(targetFile);

                LectureMaterial material = new LectureMaterial();
                material.setWeekId(weekId);
                material.setTitle(title);
                material.setFileUrl("/semi/" + savedFilename);

                lectureMaterialService.addLectureMaterial(material);
            }
        }

        return "redirect:/lectureMaterialList?weekId=" + weekId;
    }

// 강의자료수정
    @GetMapping("/updateLectureMaterial")
    public String lectureMaterialEditForm(@RequestParam int materialId,
    									HttpServletRequest request, Model model, HttpSession session) {
    	// 세션정보
    	Object userIdObj = session.getAttribute("loginUserId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = (int) userIdObj;
        User user = loginService.findById(userId);
        String role = user.getRole();
        
        // 학생 사용불가
        if (!"instructor".equals(role) && !"admin".equals(role)) {
            LectureMaterial material = lectureMaterialService.getLectureMaterialById(materialId);
            return "redirect:/lectureMaterialOne?materialId=" + materialId;
        }

        LectureMaterial material = lectureMaterialService.getLectureMaterialById(materialId);
        model.addAttribute("material", material);
        return "instructor/updateLectureMaterial";
    }

// 강의자료수정
    @PostMapping("/updateLectureMaterial")
    public String lectureMaterialEdit(@RequestParam int materialId,
                                      @RequestParam String title,
                                      @RequestParam MultipartFile file,
                                      HttpSession session) throws IOException {
        if (title == null || title.trim().isEmpty()) return "redirect:/lectureMaterialOne?materialId=" + materialId;
        // 세션정보
        Object userIdObj = session.getAttribute("loginUserId");
        if (userIdObj == null) {
            return "redirect:/login";
        }

        LectureMaterial material = lectureMaterialService.getLectureMaterialById(materialId);
        if (material == null) return "redirect:/lectureMaterialWeekList";

        material.setTitle(title);

        if (file != null && !file.isEmpty()) {
            String uploadDir = "/home/ubuntu/upload";
            String originalFilename = file.getOriginalFilename();
            String savedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            File targetFile = new File(uploadDir, savedFilename);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            file.transferTo(targetFile);
            material.setFileUrl("/semi/" + savedFilename);
        }

        lectureMaterialService.updateLectureMaterial(material);

        return "redirect:/lectureMaterialOne?materialId=" + materialId;
    }

    
// 강의자료삭제    
    @GetMapping("/deleteLectureMaterial")
    public String deleteLectureMaterial(@RequestParam int materialId,HttpServletRequest request, HttpSession session, Model model) {
    	// 세션정보
    	Object userIdObj = session.getAttribute("loginUserId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = (int) userIdObj;
        User user = loginService.findById(userId);
        String role = user.getRole();
        
        if (!"instructor".equals(role) && !"admin".equals(role)) {
            return "redirect:/lectureMaterialOne?materialId=" + materialId;
        }
        
        // 1️.삭제 전에 먼저 weekId 조회
        LectureMaterial material = lectureMaterialService.getLectureMaterialById(materialId);
        int weekId = material != null ? material.getWeekId() : 0;
        
        // 주차 정보에서 강의 ID, 주차 번호 추출 & 리스트 상단 강의정보표시
        LectureWeek weekInfo = lectureMaterialMapper.getLectureWeekById(weekId); 
        if (weekInfo != null) {
            model.addAttribute("week", weekInfo.getWeek());  // JSP에서 사용할 ${week}
            model.addAttribute("lectureId", weekInfo.getLectureId()); 
            int lectureId = weekInfo.getLectureId();
            Map<String, Object> lectureInfoMap = qnaMapper.getLectureInfoByLectureId(lectureId);
            request.setAttribute("lectureTitle", lectureInfoMap.get("title"));
            request.setAttribute("lectureDay", lectureInfoMap.get("day"));
            request.setAttribute("lectureTime", lectureInfoMap.get("time"));
        }
        // 2️.삭제 수행
        lectureMaterialService.deleteLectureMaterial(materialId);

        return "redirect:/lectureMaterialList?weekId=" + weekId;
    }
    
// 주차별 게시판
    @GetMapping("/lectureMaterialWeekList")
    public String lectureWeekList(@RequestParam int lectureId, Model model,HttpServletRequest request, HttpSession session) {
    	// 세션정보
        Object userIdObj = session.getAttribute("loginUserId");
        if (userIdObj == null) {
            return "redirect:/login";
        }
        int userId = (int) userIdObj;
        User user = loginService.findById(userId);
        String role = user.getRole();
        
        // 전체 주차 리스트 불러오기 (주차에 자료가 있는 주차만)
        List<LectureWeek> weekList = lectureMaterialService.getWeeksByLectureId(lectureId);
        model.addAttribute("weekList", weekList);
        model.addAttribute("lectureId", lectureId);
        model.addAttribute("loginRole", role);
        
        if ("student".equals(role)) {
            return "student/lectureMaterialWeekList";
        } else if("instructor".equals(role)) {
        	return "instructor/lectureMaterialWeekList";
        } else if("admin".equals(role)){
        	return "admin/lectureMaterialWeekList";
        }
        	return "redirect:/login";
    }
}

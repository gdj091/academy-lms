package com.example.academylms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.academylms.dto.InstructorInfo;
import com.example.academylms.dto.Lecture;
import com.example.academylms.dto.LectureWeekMaterial;
import com.example.academylms.dto.Notice;
import com.example.academylms.dto.QnaList;
import com.example.academylms.dto.QuizWeekList;
import com.example.academylms.dto.StudyGroup;
import com.example.academylms.dto.StudyPost;
import com.example.academylms.dto.StudyPostList;

@Mapper
public interface LectureMapper {
	List<Lecture> findLecturesByInstructor(int instructorId);
	Lecture selectLectureById(int lectureId);
	List<InstructorInfo> findInstructorInfo(); // 강사정보 조회 
	String findInstructorInfoByinfoId(int instructorId); // 전공정보 조회
	int createLecture(Lecture lecture); // 강의 개설
	int createLectureWeek(@Param("lectureId")int lectureId, @Param("week") int week); // 강의 주차 개설
	Lecture lectureOneBylectureId(int lectureId); // lectureId 로 강의 정보 조회
	int deleteLectureWeek(int lectureId); // lectureId로 강의 정보 삭제
	int updateLecture(Lecture lecture); // 강의 업데이트 
	int deleteLecture(int lectureId); // 강의 삭제
	List<LectureWeekMaterial> lectureOneWeekList(int lectureId); // 5주차 강의까지 출력
	List<Notice> lectureOneNoticeList(int lectureId); // 공지 리스트 5개 출력
	List<QuizWeekList> lectureOneQuizList(int lectureId); // 5주차 퀴즈리스트 까지 출력
	List<QnaList> lectureOneQnaList(int lectureId); // 5개의 최근 Qna  불러오기
	List<StudyPostList> lectureOneStduyGroupList(int lectureId); // 최근 5개의 스터디 그룹 리스트
	int isScheduleConflict(Lecture lecture); // 강사 강의 스케줄 중복여부 체크
	

}

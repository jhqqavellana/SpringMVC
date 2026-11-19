package com.spring.spring_rest.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.spring_rest.dto.StudentDto;
import com.spring.spring_rest.entity.Student;
import com.spring.spring_rest.exception.StudentNotFoundException;
import com.spring.spring_rest.mapper.StudentMapper;
import com.spring.spring_rest.repository.StudentRepository;

@Service
public class StudentServiceImplement implements StudentService {

  private StudentRepository studentRepo;

  public StudentServiceImplement(StudentRepository studentRepo) {
    this.studentRepo = studentRepo;
  }

  @Override
  @Transactional
  public StudentDto createStudent(StudentDto theStudentDto) {
    Student student = StudentMapper.maptoStudent(theStudentDto);
    Student savedStudent = studentRepo.save(student);
    return StudentMapper.mapToStudentDto(savedStudent);
  }

  @Override
  public StudentDto getStudentById(Long theId) {
    Student student = studentRepo.findById(theId)
        .orElseThrow(() ->
            new StudentNotFoundException("Student id: " + theId + " not found!"));
    return StudentMapper.mapToStudentDto(student);
  }

  @Override
  public List<StudentDto> getAllStudents() {
    List<Student> students = studentRepo.findAll();
    return students.stream()
        .map(StudentMapper::mapToStudentDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public StudentDto updateStudent(Long theId, StudentDto theStudentDto) {
    Student student = studentRepo.findById(theId)
        .orElseThrow(() -> new StudentNotFoundException(
            "Student id " + theId + " not found!"));

    student.setFirstName(theStudentDto.getFirstName());
    student.setLastName(theStudentDto.getLastName());
    student.setEmail(theStudentDto.getEmail());

    Student saved = studentRepo.save(student);
    return StudentMapper.mapToStudentDto(saved);
  }

  @Override
  @Transactional
  public StudentDto patchStudent(Long theId, StudentDto theStudentDto) {
    Student student = studentRepo.findById(theId)
        .orElseThrow(() -> new StudentNotFoundException(
            "Student id " + theId + " not found!"));

    if (theStudentDto.getFirstName() != null) {
      student.setFirstName(theStudentDto.getFirstName());
    }
    if (theStudentDto.getLastName() != null) {
      student.setLastName(theStudentDto.getLastName());
    }
    if (theStudentDto.getEmail() != null) {
      student.setEmail(theStudentDto.getEmail());
    }

    Student saved = studentRepo.save(student);
    return StudentMapper.mapToStudentDto(saved);
  }


@Override
  @Transactional
  public void deleteStudentById(Long theId) {
    studentRepo.findById(theId)
        .orElseThrow(() -> new StudentNotFoundException(
            "Student id: " + theId + " not found."));
    studentRepo.deleteById(theId);
  }


}
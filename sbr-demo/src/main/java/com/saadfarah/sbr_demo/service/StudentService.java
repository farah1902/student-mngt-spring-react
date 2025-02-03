package com.saadfarah.sbr_demo.service;

import com.saadfarah.sbr_demo.exception.StudentNotFoundException;
import com.saadfarah.sbr_demo.exception.studentAlreadyExistsException;
import com.saadfarah.sbr_demo.model.Student;
import com.saadfarah.sbr_demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.rmi.StubNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor

public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student addStudent(Student student) {

        if (studentAlreadyExists(student.getEmail())) {
            throw new studentAlreadyExistsException(student.getEmail() + "Email already exists");
        }
        return studentRepository.save(student);
    }


    @Override
    public Student updateStudent(Student student, Long id) {
        return studentRepository.findById(id).map(st -> {
            st.setFirstName(student.getFirstName());
            st.setLastName(student.getLastName());
            st.setProfilePicture(student.getProfilePicture());
            st.setCIN(student.getCIN());
            st.setCNE(student.getCNE());
            st.setGender(student.getGender());
            st.setBirth(student.getBirth());
            st.setEmail(student.getEmail());
            st.setPhone(student.getPhone());
            st.setAddress(student.getAddress());

            return studentRepository.save(st);

        }).orElseThrow(() -> new StudentNotFoundException("Sorry, this student could not be found"));
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Sorry, no student found with this id : " +id));
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Sorry, student not found");
        }
    }

    private boolean studentAlreadyExists(String email) {
        return studentRepository.findByEmail(email).isPresent(); //if student is present
    }
}

package h.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import h.demo.model.Student;
import h.demo.repository.StudentRepository;


@Service
@Transactional
public class StudentServiceImplementation implements StudentService {

	
	@Autowired
	StudentRepository studentRepository;
	
	
	@Override
	public List<Student> getAllStudents() {
		List<Student> studentList = (List<Student>) studentRepository.findAll();
		return studentList;
	}

	@Override
	public Student getStudentById(int id) {
		Student student = studentRepository.findById(id).get();
		return student;
	}

	@Override
	public void addStudent(Student student) {
		studentRepository.save(student);
	}

	@Override
	public void deleteStudent(int id) {
		studentRepository.deleteById(id);
	}

	
	 
	
}
package h.demo.repository;

import org.springframework.data.repository.CrudRepository;

import h.demo.model.Student;



public interface StudentRepository extends CrudRepository<Student, Integer> {

}

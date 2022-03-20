package h.demo.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import h.demo.model.Student;
import h.demo.services.StudentService;


@Controller
@RequestMapping(value="/student")
public class StudentController {

	@Autowired
	StudentService studentService;
	
	
	 @RequestMapping(value="/list", method=RequestMethod.GET)
	 public ModelAndView list() {
		 
	  ModelAndView model = new ModelAndView("student_list");
	  List<Student> studentList = studentService.getAllStudents();
	  model.addObject("studentList", studentList);
	
	  return model;
	 }
	 
	 
	 
	 @RequestMapping(value="/addStudent/", method=RequestMethod.GET)
	 public ModelAndView addStudent() {
		 
	  ModelAndView model = new ModelAndView();
	  Student student = new Student();
	  model.addObject("studentForm", student);
	  model.setViewName("student_form");
	  
	  return model;
	 }
	 
	 
	 
	 @RequestMapping(value="/editStudent/{id}", method=RequestMethod.GET)
	 public ModelAndView editStudent(@PathVariable int id) {
	  ModelAndView model = new ModelAndView();
	  
	  Student student = studentService.getStudentById(id);
	  model.addObject("studentForm", student);
	  model.setViewName("student_form");
	  
	  return model;
	 }
	 
	 
	 @RequestMapping(value="/addStudent", method=RequestMethod.POST)
	 public ModelAndView add(@ModelAttribute("studentForm") Student student) {
		 
	  studentService.addStudent(student);
	  return new ModelAndView("redirect:/student/list");
	  
	 }
	 
	 @RequestMapping(value="/deleteStudent/{id}", method=RequestMethod.GET)
	 public ModelAndView delete(@PathVariable("id") int id) {
		 
	  studentService.deleteStudent(id);
	  return new ModelAndView("redirect:/student/list");
	  
	 }
	
}
package h.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;
import org.springframework.vault.support.VaultMount;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import h.demo.model.Student;
import h.demo.services.StudentService;

@Controller
@RequestMapping(value = "/student")
public class StudentController {

	@Autowired
	StudentService studentService;

	String KEY_NAME = "foo-key";

	@Autowired
	private VaultTemplate vaultTemplate;
	VaultTransitOperations transitOperations;
	

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		System.out.println("hello world, I have just started up");
		transitOperations = vaultTemplate.opsForTransit();
		VaultSysOperations sysOperations = vaultTemplate.opsForSys();
		if (!sysOperations.getMounts().containsKey("transit/")) {
			sysOperations.mount("transit", VaultMount.create("transit"));
			transitOperations.createKey(KEY_NAME);
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView model = new ModelAndView("student_list");
		List<Student> studentList = studentService.getAllStudents();
		for (Student student : studentList) {
			String lastNameDecrypted = student.getLastname();
			lastNameDecrypted =  transitOperations.decrypt(KEY_NAME, student.getLastname());
			student.setLastname(lastNameDecrypted);		    
		}

		model.addObject("studentList", studentList);

		return model;
	}

	@RequestMapping(value = "/addStudent/", method = RequestMethod.GET)
	public ModelAndView addStudent() {

		ModelAndView model = new ModelAndView();
		Student student = new Student();
		model.addObject("studentForm", student);
		model.setViewName("student_form");

		return model;
	}

	@RequestMapping(value = "/editStudent/{id}", method = RequestMethod.GET)
	public ModelAndView editStudent(@PathVariable int id) {
		ModelAndView model = new ModelAndView();

		Student student = studentService.getStudentById(id);
		String lastNameDecrypted =  transitOperations.decrypt(KEY_NAME, student.getLastname());
		student.setLastname(lastNameDecrypted);

		model.addObject("studentForm", student);
		model.setViewName("student_form");		
		return model;
	}

	@RequestMapping(value = "/addStudent", method = RequestMethod.POST)
	public ModelAndView add(@ModelAttribute("studentForm") Student student) {
		String lastName = student.getLastname();
		String lastNameEncrypted = transitOperations.encrypt(KEY_NAME, lastName);
		student.setLastname(lastNameEncrypted);

		studentService.addStudent(student);
		return new ModelAndView("redirect:/student/list");

	}

	@RequestMapping(value = "/deleteStudent/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") int id) {

		studentService.deleteStudent(id);
		return new ModelAndView("redirect:/student/list");

	}	

}
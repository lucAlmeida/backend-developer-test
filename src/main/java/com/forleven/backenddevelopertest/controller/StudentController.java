package com.forleven.backenddevelopertest.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.forleven.backenddevelopertest.dto.StudentDTO;
import com.forleven.backenddevelopertest.exception.RequirementViolationException;
import com.forleven.backenddevelopertest.service.IStudentService;
import com.forleven.backenddevelopertest.validation.CreateValidate;
import com.forleven.backenddevelopertest.validation.DeleteValidate;
import com.forleven.backenddevelopertest.validation.UpdateSpecValidate;
import com.forleven.backenddevelopertest.validation.UpdateValidate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@Api(tags="Students")
@CrossOrigin(origins="*")
@Validated
public class StudentController {
	private IStudentService studentService;
	
	@Autowired
	public StudentController(IStudentService studentService) {
		this.studentService = studentService;
	}
	
	@GetMapping("/students")
	@ApiOperation(value="Retorna lista de estudantes cadastrados")
	@ApiResponses(value= {@ApiResponse(code=HttpServletResponse.SC_OK, message="OK - Retorna lista de estudantes cadastrados")})
	public List<StudentDTO> findAll() {
		return studentService.findAll().stream().map(StudentDTO::from).collect(Collectors.toList());
	}
	
	@GetMapping("/students/{enrollmentId}")
	@ApiOperation(value="Retorna um determinado estudante com base em seu número de matrícula atual")
	@ApiResponses(value={@ApiResponse(code=HttpServletResponse.SC_OK, message="OK - Retorna estudante já cadastrado"), @ApiResponse(code=HttpServletResponse.SC_NOT_FOUND, message="Not found"), @ApiResponse(code=HttpServletResponse.SC_BAD_REQUEST, message="Invalid request")})
	public StudentDTO getStudent(@PathVariable @ApiParam(name="enrollmentId", value="Student Enrollment ID") @Valid @Pattern(regexp="^[0-9]+$", message="The enrollment ID must be composed of numerical digits only") @Size(min=3, message="The enrollment ID must have a size of at least 3 characters") String enrollmentId) throws RequirementViolationException {
		return StudentDTO.from(studentService.find(enrollmentId));
	}
	
	@PostMapping("/students")
	@ApiOperation(value="Salva um determinado estudante")
	@ApiResponses(value={@ApiResponse(code=HttpServletResponse.SC_OK, message="OK - Retorna estudante que foi registrado"), @ApiResponse(code=HttpServletResponse.SC_CONFLICT, message="Already exists"), @ApiResponse(code=HttpServletResponse.SC_BAD_REQUEST, message="Invalid request")})
	public StudentDTO addStudent(@RequestBody @ApiParam(name="student", value="Student") @Validated(CreateValidate.class) StudentDTO dto) throws RequirementViolationException {
		return StudentDTO.from(studentService.save(dto.toStudent()));
	}
	
	@PutMapping("/students/{enrollmentId}")
	@ApiOperation(value="Atualiza um determinado estudante com base em seu número de matrícula atual")
	@ApiResponses(value={@ApiResponse(code=HttpServletResponse.SC_OK, message="OK - Retorna estudante que teve seu cadastro alterado"), @ApiResponse(code=HttpServletResponse.SC_NOT_FOUND, message="Not found"), @ApiResponse(code=HttpServletResponse.SC_BAD_REQUEST, message="Invalid request")})
	public StudentDTO updateStudent(@RequestBody @ApiParam(name="student", value="Student") @Validated(UpdateSpecValidate.class) StudentDTO dto,
			                        @PathVariable @ApiParam(name="enrollmentId", value="Current Student Enrollment ID") @Valid @Pattern(regexp="^[0-9]+$", message="The enrollment ID must be composed of numerical digits only") @Size(min=3, message="The enrollment ID must have a size of at least 3 characters") String enrollmentId) throws RequirementViolationException {
		return StudentDTO.from(studentService.update(dto.toStudent(), enrollmentId));
	}
	
	@PutMapping("/students")
	@ApiOperation(value="Atualiza um determinado estudante")
	@ApiResponses(value={@ApiResponse(code=HttpServletResponse.SC_OK, message="OK - Retorna estudante que teve seu cadastro alterado"), @ApiResponse(code=HttpServletResponse.SC_NOT_FOUND, message="Not found"), @ApiResponse(code=HttpServletResponse.SC_BAD_REQUEST, message="Invalid request")})
	public StudentDTO updateStudent(@RequestBody @ApiParam(name="student", value="Student") @Validated(UpdateValidate.class) StudentDTO dto) throws RequirementViolationException {
		return StudentDTO.from(studentService.update(dto.toStudent()));
	}

	@DeleteMapping("/students/{enrollmentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="Remove um determinado estudante com base em seu número de matrícula atual")
	@ApiResponses(value={@ApiResponse(code=HttpServletResponse.SC_NO_CONTENT, message="No Content - Estudante foi deletado"), @ApiResponse(code=HttpServletResponse.SC_NOT_FOUND, message="Not found"), @ApiResponse(code=HttpServletResponse.SC_BAD_REQUEST, message="Invalid request")})
	public ResponseEntity<Void> deleteStudent(@PathVariable @ApiParam(name="enrollmentId", value="Student Enrollment ID") @Valid @Pattern(regexp="^[0-9]+$", message="The enrollment ID must be composed of numerical digits only") @Size(min=3, message="The enrollment ID must have a size of at least 3 characters") String enrollmentId) throws RequirementViolationException {
		studentService.delete(enrollmentId);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/students")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="Remove um determinado estudante")
	@ApiResponses(value={@ApiResponse(code=HttpServletResponse.SC_NO_CONTENT, message="No Content - Estudante foi deletado"), @ApiResponse(code=HttpServletResponse.SC_NOT_FOUND, message="Not found"), @ApiResponse(code=HttpServletResponse.SC_BAD_REQUEST, message="Invalid request")})
	public ResponseEntity<Void> deleteStudent(@RequestBody @ApiParam(name="student", value="Student") @Validated(DeleteValidate.class) StudentDTO dto) throws RequirementViolationException {
		studentService.delete(dto.toStudent());
		return ResponseEntity.noContent().build();
	}
}

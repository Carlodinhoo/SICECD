package com.cocay.sicecd.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cocay.sicecd.model.Curso;
import com.cocay.sicecd.model.Grupo;
import com.cocay.sicecd.model.Profesor;
import com.cocay.sicecd.repo.CursoRep;
import com.cocay.sicecd.repo.GrupoRep;
import com.cocay.sicecd.repo.InscripcionRep;
import com.cocay.sicecd.repo.ProfesorRep;
import com.cocay.sicecd.service.SendMailService;


@Controller
public class BatchController {
	
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier("jobCurso")
	Job jobCurso;
	
	@Autowired
	@Qualifier("jobGrupo")
	Job jobGrupo;
	
	@Autowired
	@Qualifier("jobProfesor")
	Job jobProfesor;
	
	@Autowired
	@Qualifier("jobInscripcion")
	Job jobInscripcion;
	
	@Autowired
	@Qualifier("cursoExcelJob")
	Job cursoExcelJob;
	
	@Autowired
	CursoRep curso;
	
	@Autowired
	GrupoRep grupo;
	
	@Autowired
	ProfesorRep profesor;
	
	@Autowired
	InscripcionRep inscripcion;
	
	@Autowired
	@Qualifier("grupoExcelJob")
	Job grupoExcelJob;
	
	@Autowired
	@Qualifier("inscripcionExcelJob")
	Job inscripcionExcelJob;
	
	@Autowired
	@Qualifier("profesorExcelJob")
	Job profesorExcelJob;
	
	@Autowired
	SendMailService _email;
	
	String carpeta = System.getProperty("user.dir");
	
	private String UPLOADED_FOLDER = carpeta + "/folderTemp/";
	
	@PostMapping("/consultarCursos") // //new annotation since 4.3
    public ModelAndView singleFileUpload(@RequestParam("file") MultipartFile file) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {


        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path ruta = Paths.get(UPLOADED_FOLDER);
            if(!Files.exists(ruta)) {
            	Files.createDirectory(ruta);
            	
            }
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            File fileToImport = new File(UPLOADED_FOLDER + file.getOriginalFilename());
          //Launch the Batch Job
            JobExecution jobExecution = jobLauncher.run(jobCurso, new JobParametersBuilder()
            		.addLong("time", System.currentTimeMillis())
                    .addString("fullPathFileName", fileToImport.getAbsolutePath())
                    .toJobParameters()); 
            
            fileToImport.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return new ModelAndView("BatchTemplate/consultarCursos");

    }
	
	@PostMapping("/consultarGrupo") // //new annotation since 4.3
    public ModelAndView singleFilegrupo(@RequestParam("file") MultipartFile file) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {


        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path ruta = Paths.get(UPLOADED_FOLDER);
            if(!Files.exists(ruta)) {
            	Files.createDirectory(ruta);
            	
            }
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            File fileToImport = new File(UPLOADED_FOLDER + file.getOriginalFilename());
          //Launch the Batch Job
            JobExecution jobExecution = jobLauncher.run(jobGrupo, new JobParametersBuilder()
            		.addLong("time", System.currentTimeMillis())
                    .addString("fullPathFileName", fileToImport.getAbsolutePath())
                    .toJobParameters()); 

            fileToImport.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

		return new ModelAndView("BatchTemplate/consultarGrupo");
    }
	
	@PostMapping("/consultarProfesor") // //new annotation since 4.3
    public ModelAndView singleFileprofesor(@RequestParam("file") MultipartFile file) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {


        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path ruta = Paths.get(UPLOADED_FOLDER);
            if(!Files.exists(ruta)) {
            	Files.createDirectory(ruta);
            	
            }
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            File fileToImport = new File(UPLOADED_FOLDER + file.getOriginalFilename());
          //Launch the Batch Job
            JobExecution jobExecution = jobLauncher.run(jobProfesor, new JobParametersBuilder()
            		.addLong("time", System.currentTimeMillis())
                    .addString("fullPathFileName", fileToImport.getAbsolutePath())
                    .toJobParameters()); 

            fileToImport.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ModelAndView("BatchTemplate/consultarProfesor");
    }
	
	@PostMapping("/consultarInscripcionBatch") // //new annotation since 4.3
    public ModelAndView singleFileinscripcion(@RequestParam("file") MultipartFile file) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {


        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path ruta = Paths.get(UPLOADED_FOLDER);
            if(!Files.exists(ruta)) {
            	Files.createDirectory(ruta);
            	
            }
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            File fileToImport = new File(UPLOADED_FOLDER + file.getOriginalFilename());
          //Launch the Batch Job
            JobExecution jobExecution = jobLauncher.run(jobInscripcion, new JobParametersBuilder()
            		.addLong("time", System.currentTimeMillis())
                    .addString("fullPathFileName", fileToImport.getAbsolutePath())
                    .toJobParameters()); 

            fileToImport.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ModelAndView("BatchTemplate/consultarInscripcionBatch");
    }
	
	
	@RequestMapping(value = "/consultarCursos", method = RequestMethod.GET)
	public ModelAndView consultarCursos(ModelMap model, HttpServletRequest request) {
		
		List<Curso> list_p = curso.loadAllCursos();
		if(!list_p.isEmpty()) {
			model.put("cursos", list_p);
			return new ModelAndView("/BatchTemplate/consultarCursos", model);
			
		} else {
			return new ModelAndView("/BatchTemplate/consultarCursos");
		}
	}
	
	@RequestMapping(value = "/consultarGrupo", method = RequestMethod.GET)
	public ModelAndView consultarGrupo(ModelMap model, HttpServletRequest request) {
		
		List<Grupo> list_p = grupo.loadAllGrupo();
		if(!list_p.isEmpty()) {
			model.put("grupos", list_p);
			return new ModelAndView("/BatchTemplate/consultarGrupo", model);
			
		} else {
			return new ModelAndView("/BatchTemplate/consultarGrupo");
		}
	}
	
	@RequestMapping(value = "/consultarProfesor", method = RequestMethod.GET)
	public ModelAndView consultarProfesor(ModelMap model, HttpServletRequest request) {
		
		List<Profesor> list_p = profesor.loadAllProfesor();
		if(!list_p.isEmpty()) {
			model.put("profesores", list_p);
			return new ModelAndView("/BatchTemplate/consultarProfesor", model);
			
		} else {
			return new ModelAndView("/BatchTemplate/consultarProfesor");
		}
	}
	
	@RequestMapping(value = "/consultarInscripcionBatch", method = RequestMethod.GET)
	public ModelAndView consultarInscripcionBatch(ModelMap model, HttpServletRequest request) {
		
		return new ModelAndView("/BatchTemplate/consultarInscripcionBatch");
		
	}
	
	@RequestMapping(value = "/pruebaBatch", method = RequestMethod.GET)
	public ModelAndView pruebaBatch(ModelMap model, HttpServletRequest request) {
		
		List<Profesor> list_p = profesor.loadAllProfesor();
		if(!list_p.isEmpty()) {
			model.put("profesores", list_p);
			return new ModelAndView("/BatchTemplate/prueba", model);
			
		} else {
			return new ModelAndView("/BatchTemplate/prueba");
		}
	}
	
	@RequestMapping(value = "/BusquedaCorreo", method = RequestMethod.POST)
	public ModelAndView busqueda(ModelMap model, HttpServletRequest request) {
		String nombre = request.getParameter("nombre").toUpperCase();
		ModelAndView mv= new ModelAndView("search::search_list"); 
		List<Profesor>	list_p1 = profesor.findAll();
		List<Profesor>	list_p2 = profesor.findAll();
			
		//Filtrando por Nombre
		if (nombre != "") {
			for (Profesor p : list_p1) {
				String nom = p.getNombre().toUpperCase();
				if( !nom.contains(nombre) ) {
					list_p2.remove(p);
				}
			}
		}
			
		if(!list_p1.isEmpty()) {
			model.put("profesores", list_p2);
			mv.addObject("searchList",list_p2);
			return new ModelAndView("/CorreosTemplate/AvisosCorreo", model);
		} else {
			return new ModelAndView("/CorreosTemplate/AvisosCorreo", model);
		}
	}
	
	@RequestMapping(value = "/AvisosCorreo", method = RequestMethod.GET)
	public ModelAndView avisos(ModelMap model, HttpServletRequest request) {
		
		return new ModelAndView("/CorreosTemplate/AvisosCorreo", model);
	}
	
	
	@PostMapping("/runExcelCurso")
	public ModelAndView insertaExcel(@RequestParam("file") MultipartFile file) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		
		try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path ruta = Paths.get(UPLOADED_FOLDER);
            if(!Files.exists(ruta)) {
            	Files.createDirectory(ruta);
            	
            }
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            File fileToImport = new File(UPLOADED_FOLDER + file.getOriginalFilename());
          //Launch the Batch Job
            JobExecution jobExecution = jobLauncher.run(cursoExcelJob, new JobParametersBuilder()
            		.addLong("time", System.currentTimeMillis())
                    .addString("fullPathFileName", fileToImport.getAbsolutePath())
                    .toJobParameters()); 
            System.out.println("Exit Status : " + jobExecution.getStatus());
            
            fileToImport.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return new ModelAndView("BatchTemplate/consultarCursos");
        
	}
	
	@PostMapping("/runExcelGrupo")
	public ModelAndView insertaExcelGrupo(@RequestParam("file") MultipartFile file) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		
		try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path ruta = Paths.get(UPLOADED_FOLDER);
            if(!Files.exists(ruta)) {
            	Files.createDirectory(ruta);
            	
            }
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            File fileToImport = new File(UPLOADED_FOLDER + file.getOriginalFilename());
          //Launch the Batch Job
            JobExecution jobExecution = jobLauncher.run(grupoExcelJob, new JobParametersBuilder()
            		.addLong("time", System.currentTimeMillis())
                    .addString("fullPathFileName", fileToImport.getAbsolutePath())
                    .toJobParameters()); 
            System.out.println("Exit Status : " + jobExecution.getStatus());
            
            fileToImport.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return new ModelAndView("BatchTemplate/consultarGrupo");
        
	}
	
	@PostMapping("/runExcelInscripcion")
	public ModelAndView insertaExcelInscripcion(@RequestParam("file") MultipartFile file) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		
		try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path ruta = Paths.get(UPLOADED_FOLDER);
            if(!Files.exists(ruta)) {
            	Files.createDirectory(ruta);
            	
            }
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            File fileToImport = new File(UPLOADED_FOLDER + file.getOriginalFilename());
          //Launch the Batch Job
            JobExecution jobExecution = jobLauncher.run(inscripcionExcelJob, new JobParametersBuilder()
            		.addLong("time", System.currentTimeMillis())
                    .addString("fullPathFileName", fileToImport.getAbsolutePath())
                    .toJobParameters()); 
            System.out.println("Exit Status : " + jobExecution.getStatus());
            
            fileToImport.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return new ModelAndView("BatchTemplate/consultarInscripcionBatch");
        
	}
	
	@PostMapping("/runExcelProfesor")
	public ModelAndView insertaExcelProfesor(@RequestParam("file") MultipartFile file) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		
		try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path ruta = Paths.get(UPLOADED_FOLDER);
            if(!Files.exists(ruta)) {
            	Files.createDirectory(ruta);
            	
            }
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            File fileToImport = new File(UPLOADED_FOLDER + file.getOriginalFilename());
          //Launch the Batch Job
            JobExecution jobExecution = jobLauncher.run(profesorExcelJob, new JobParametersBuilder()
            		.addLong("time", System.currentTimeMillis())
                    .addString("fullPathFileName", fileToImport.getAbsolutePath())
                    .toJobParameters()); 
            System.out.println("Exit Status : " + jobExecution.getStatus());
            
            fileToImport.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return new ModelAndView("BatchTemplate/consultarProfesor");
        
	}
	
	
}

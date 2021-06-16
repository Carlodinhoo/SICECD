package com.cocay.sicecd.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PushbackInputStream;

import org.hibernate.exception.DataException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.ExhaustedRetryException;

import com.cocay.sicecd.model.Curso;
import com.cocay.sicecd.model.Grupo;
import com.cocay.sicecd.model.Inscripcion;
import com.cocay.sicecd.model.Profesor;
import com.cocay.sicecd.repo.CursoBatchRep;
import com.cocay.sicecd.repo.GrupoBatchRep;
import com.cocay.sicecd.repo.InscripcionBatchRep;
import com.cocay.sicecd.repo.ProfesorBatchRep;
import com.cocay.sicecd.step.CursoExcelRowMapper;
import com.cocay.sicecd.step.GrupoExcelRowMapper;
import com.cocay.sicecd.step.InscripcionExcelRowMapper;
import com.cocay.sicecd.step.ProcessorCurso;
import com.cocay.sicecd.step.ProcessorGrupo;
import com.cocay.sicecd.step.ProcessorInscripcion;
import com.cocay.sicecd.step.ProcessorProfesor;
import com.cocay.sicecd.step.ProfesorExcelRowMapper;
import com.cocay.sicecd.step.VerificacionArchivo;
import com.cocay.sicecd.step.WriterCurso;
import com.cocay.sicecd.step.WriterGrupo;
import com.cocay.sicecd.step.WriterInscripcion;
import com.cocay.sicecd.step.WriterProfesor;

@Configuration
@EnableBatchProcessing
public class ExcelConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	//Procesadores
	@Autowired
	private ProcessorCurso processorCurso;
	@Autowired
	private ProcessorGrupo processorGrupo;
	@Autowired
	private ProcessorInscripcion processorInscripcion;
	@Autowired
	private ProcessorProfesor processorProfesor;
	
	//Repositorios que implementan JPA
	@Autowired
    private CursoBatchRep cursoRep;
	@Autowired
	private GrupoBatchRep grupoBatchRep;
	@Autowired
	private InscripcionBatchRep inscripcionBatchRep;
	@Autowired
	private ProfesorBatchRep profesorBatchRep;	
	
	@Bean
	public SkipPolicy fileVerification() {
		return new VerificacionArchivo();
	}
	
	/*
     *  Configuracion batch, para el proceso de cursos
     */ 
	
	@Bean
	@StepScope
	PoiItemReader<Curso> excelCursoReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) throws FileNotFoundException {
        PoiItemReader<Curso> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1);
        PushbackInputStream input = new PushbackInputStream(new FileInputStream(pathToFile));
        InputStreamResource resource = new InputStreamResource(input);
        reader.setResource(resource);
        reader.setRowMapper(excelRowMapper());
        return reader;
    }
	
	/*
	 * Metodo para leer un excel sin cabecera
	 *
	private RowMapper<Curso> excelRowMapper() {
        BeanWrapperRowMapper<Curso> rowMapper = new BeanWrapperRowMapper<>();
        rowMapper.setTargetType(Curso.class);
        return rowMapper;
    }*/
 
    private RowMapper<Curso> excelRowMapper() {
        return new CursoExcelRowMapper();
    }
    
    @Bean
    Step cursoExcelStep(PoiItemReader<Curso> excelCursoReader) {
        return stepBuilderFactory.get("cursoExcelStep")
                .<Curso, Curso>chunk(100)
                .reader(excelCursoReader)
                .faultTolerant()
				.skip(ExhaustedRetryException.class)
				.skip(DataIntegrityViolationException.class)
				.skip(DataException.class)
				.skipLimit(100000)
				.skipPolicy(fileVerification())
                .processor(processorCurso)
                .writer(new WriterCurso(cursoRep))
                .build();
    }

    @Bean
    Job cursoExcelJob(PoiItemReader<Curso> excelCursoReader) {
        return jobBuilderFactory.get("cursoExcelJob")
                .incrementer(new RunIdIncrementer())
                .flow(cursoExcelStep(excelCursoReader))
                .end()
                .build();
    }
    
    /*
     *  Configuracion batch, para el proceso de grupos
     */ 
    
    @Bean
	@StepScope
	PoiItemReader<Grupo> excelGrupoReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) throws FileNotFoundException {
        PoiItemReader<Grupo> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1);
        PushbackInputStream input = new PushbackInputStream(new FileInputStream(pathToFile));
        InputStreamResource resource = new InputStreamResource(input);
        reader.setResource(resource);
        reader.setRowMapper(excelRowMapperGrupo());
        return reader;
    }
 
    private RowMapper<Grupo> excelRowMapperGrupo() {
        return new GrupoExcelRowMapper();
    }

    @Bean
    Step grupoExcelStep(PoiItemReader<Grupo> excelGrupoReader) {
        return stepBuilderFactory.get("grupoExcelStep")
                .<Grupo, Grupo>chunk(100)
                .reader(excelGrupoReader)
                .faultTolerant()
				.skip(ExhaustedRetryException.class)
				.skip(DataIntegrityViolationException.class)
				.skip(DataException.class)
				.skipLimit(100000)
				.skipPolicy(fileVerification())
                .processor(processorGrupo)
                .writer(new WriterGrupo(grupoBatchRep))
                .build();
    }

    @Bean
    Job grupoExcelJob(PoiItemReader<Grupo> excelGrupoReader) {
        return jobBuilderFactory.get("grupoExcelJob")
                .incrementer(new RunIdIncrementer())
                .flow(grupoExcelStep(excelGrupoReader))
                .end()
                .build();
    }
    
    /*
     *  Configuracion batch, para el proceso de inscripcion
     */ 
    
    @Bean
	@StepScope
	PoiItemReader<Inscripcion> excelInscripcionReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) throws FileNotFoundException {
        PoiItemReader<Inscripcion> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1);
        PushbackInputStream input = new PushbackInputStream(new FileInputStream(pathToFile));
        InputStreamResource resource = new InputStreamResource(input);
        reader.setResource(resource);
        reader.setRowMapper(excelRowMapperInscripcion());
        return reader;
    }
 
    private RowMapper<Inscripcion> excelRowMapperInscripcion() {
        return new InscripcionExcelRowMapper();
    }

    @Bean
    Step inscripcionExcelStep(PoiItemReader<Inscripcion> excelInscripcionReader) {
        return stepBuilderFactory.get("inscripcionExcelStep")
                .<Inscripcion, Inscripcion>chunk(100)
                .reader(excelInscripcionReader)
                .faultTolerant()
				.skip(ExhaustedRetryException.class)
				.skip(DataIntegrityViolationException.class)
				.skip(DataException.class)
				.skipLimit(100000)
				.skipPolicy(fileVerification())
                .processor(processorInscripcion)
                .writer(new WriterInscripcion(inscripcionBatchRep))
                .build();
    }

    @Bean
    Job inscripcionExcelJob(PoiItemReader<Inscripcion> excelInscripcionReader) {
        return jobBuilderFactory.get("inscripcionExcelJob")
                .incrementer(new RunIdIncrementer())
                .flow(inscripcionExcelStep(excelInscripcionReader))
                .end()
                .build();
    }
    
    /*
     *  Configuracion batch, para el proceso de profesores
     */ 
    @Bean
	@StepScope
	PoiItemReader<Profesor> excelProfesorReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) throws FileNotFoundException {
        PoiItemReader<Profesor> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1);
        PushbackInputStream input = new PushbackInputStream(new FileInputStream(pathToFile));
        InputStreamResource resource = new InputStreamResource(input);
        reader.setResource(resource);
        reader.setRowMapper(excelRowMapperProfesor());
        return reader;
    }
 
    private RowMapper<Profesor> excelRowMapperProfesor() {
        return new ProfesorExcelRowMapper();
    }

    @Bean
    Step profesorExcelStep(PoiItemReader<Profesor> excelProfesorReader) {
        return stepBuilderFactory.get("profesorExcelStep")
                .<Profesor, Profesor>chunk(100)
                .reader(excelProfesorReader)
                .faultTolerant()
				.skip(ExhaustedRetryException.class)
				.skip(DataIntegrityViolationException.class)
				.skip(DataException.class)
				.skipLimit(100000)
				.skipPolicy(fileVerification())
                .processor(processorProfesor)
                .writer(new WriterProfesor(profesorBatchRep))
                .build();
    }

    @Bean
    Job profesorExcelJob(PoiItemReader<Profesor> excelProfesorReader) {
        return jobBuilderFactory.get("profesorExcelJob")
                .incrementer(new RunIdIncrementer())
                .flow(profesorExcelStep(excelProfesorReader))
                .end()
                .build();
    }
}

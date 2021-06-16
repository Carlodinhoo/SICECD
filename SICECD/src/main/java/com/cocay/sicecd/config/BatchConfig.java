package com.cocay.sicecd.config;

import org.hibernate.exception.DataException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
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
import com.cocay.sicecd.step.ProcessorCurso;
import com.cocay.sicecd.step.ProcessorGrupo;
import com.cocay.sicecd.step.ProcessorInscripcion;
import com.cocay.sicecd.step.ProcessorProfesor;
import com.cocay.sicecd.step.VerificacionArchivo;
import com.cocay.sicecd.step.WriterCurso;
import com.cocay.sicecd.step.WriterGrupo;
import com.cocay.sicecd.step.WriterInscripcion;
import com.cocay.sicecd.step.WriterProfesor;


@Configuration
@EnableBatchProcessing
public class BatchConfig  {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
    private CursoBatchRep cursoRep;
	
	@Autowired
    private GrupoBatchRep grupoRep;
	
	@Autowired
	private ProfesorBatchRep profesorRep;
	
	@Autowired
	private InscripcionBatchRep inscripcionRep;
	
	@Autowired
	private ProcessorGrupo processorGrupo;
	
	@Autowired
	private ProcessorCurso processorCurso;
	
	@Autowired
	private ProcessorInscripcion processorInscripcion;
	
	@Autowired
	private ProcessorProfesor processorProfesor;
	
	@Bean
	public SkipPolicy fileVerificationSkipper() {
		return new VerificacionArchivo();
	}
	
    @Bean
    @StepScope
    public FlatFileItemReader<Curso> importReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) {
        FlatFileItemReader<Curso> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(pathToFile));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<Curso>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
            	setNames(new String[] {"clave", "nombre", "tempTipoCurso", "horas"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Curso>() {{
                setTargetType(Curso.class);
            }});
        }});
        return reader;
    }
 
	@Bean
	public Job jobCurso(ItemReader<Curso> importReader) {
		return jobBuilderFactory.get("jobCurso").incrementer(new RunIdIncrementer())
				.flow(step1(importReader)).end().build();
	}

	@Bean
	public Step step1(ItemReader<Curso> importReader) {
		return stepBuilderFactory.get("step1").<Curso, Curso>chunk(100)
				.reader(importReader)
				.faultTolerant()
				.skip(ExhaustedRetryException.class)
				.skip(DataIntegrityViolationException.class)
				.skip(DataException.class)
				.skipLimit(100000)
				.skipPolicy(fileVerificationSkipper())
				.processor(processorCurso).writer(new WriterCurso(cursoRep)).build();
	}
	
    @Bean(name = "importReaderGrupo")
    @StepScope
    public FlatFileItemReader<Grupo> importReaderGrupo(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) {
    	FlatFileItemReader<Grupo> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(pathToFile));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<Grupo>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
            	setNames(new String[] {"clave", "tempCurso", "tempProfesor"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Grupo>() {{
                setTargetType(Grupo.class);
            }});
        }});
        return reader;
    }
    
	@Bean
	public Job jobGrupo() {
		return jobBuilderFactory.get("jobGrupo")
				.incrementer(new RunIdIncrementer())
				.flow(stepGrupo())
				.end()
				.build();
	}

	@Autowired
	@Qualifier("importReaderGrupo")
	ItemReader<Grupo> importReaderGrupo;
	
	@Bean
	public Step stepGrupo() {
		return stepBuilderFactory.get("stepGrupo").<Grupo, Grupo>chunk(100)
				.reader(importReaderGrupo)
				.faultTolerant()
				.skip(ExhaustedRetryException.class)
				.skip(DataIntegrityViolationException.class)
				.skip(DataException.class)
				.skipLimit(100000)
				.skipPolicy(fileVerificationSkipper())
				.processor(processorGrupo).writer(new WriterGrupo(grupoRep)).build();
	}
	
	@Bean
    @StepScope
    public FlatFileItemReader<Profesor> importReaderProfesor(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) {
    	FlatFileItemReader<Profesor> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(pathToFile));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<Profesor>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
            	setNames(new String[] {"nombre", "apellido_paterno", "apellido_materno", "rfc", "correo", "telefono", "tempEstado", "ciudad_localidad", "tempGenero", "plantel", "clave_plantel", "tempTurno", "tempGradoP", "ocupacion"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Profesor>() {{
                setTargetType(Profesor.class);
            }});
        }});
        return reader;
    }
	
	@Autowired
	@Qualifier("importReaderProfesor")
	private ItemReader<Profesor> importReaderProfesor;

	@Bean
	public Job jobProfesor() {
		return jobBuilderFactory.get("jobProfesor").incrementer(new RunIdIncrementer())
				.flow(stepProfesor()).end().build();
	}

	@Bean
	public Step stepProfesor() {
		return stepBuilderFactory.get("stepProfesor").<Profesor, Profesor>chunk(100)
				.reader(importReaderProfesor)
				.faultTolerant()
				.skip(ExhaustedRetryException.class)
				.skip(DataIntegrityViolationException.class)
				.skip(DataException.class)
				.skipLimit(100000)
				.skipPolicy(fileVerificationSkipper())
				.processor(processorProfesor).writer(new WriterProfesor(profesorRep)).build();
	}
	
	@Bean
    @StepScope
    public FlatFileItemReader<Inscripcion> importReaderInscripcion(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) {
    	FlatFileItemReader<Inscripcion> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(pathToFile));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<Inscripcion>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
            	setNames(new String[] {"tempGrupo", "tempCurso", "tempProfesor", "calif"});
            	setStrict(false);
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Inscripcion>() {{
                setTargetType(Inscripcion.class);
            }});
        }});
        return reader;
    }
	
	@Autowired
	@Qualifier("importReaderInscripcion")
	private ItemReader<Inscripcion> importReader;

	@Bean
	public Job jobInscripcion() {
		return jobBuilderFactory.get("jobInscripcion").incrementer(new RunIdIncrementer())
				.flow(stepInscripcion()).end().build();
	}

	@Bean
	public Step stepInscripcion() {
		return stepBuilderFactory.get("stepInscripcion").<Inscripcion, Inscripcion>chunk(100)
				.reader(importReader)
				.faultTolerant()
				.skip(ExhaustedRetryException.class)
				.skip(DataIntegrityViolationException.class)
				.skip(DataException.class)
				.skipLimit(100000)
				.skipPolicy(fileVerificationSkipper())
				.processor(processorInscripcion).writer(new WriterInscripcion(inscripcionRep)).build();
	}
}
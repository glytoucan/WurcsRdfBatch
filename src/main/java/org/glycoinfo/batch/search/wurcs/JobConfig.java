package org.glycoinfo.batch.search.wurcs;

import java.util.List;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfig {

  @Bean
  public Job importUserJob(JobBuilderFactory jobs, Step s1) {
    return jobs.get("MotifRdf").incrementer(new RunIdIncrementer()).flow(s1).end().build();
  }

  @Bean
  public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<SparqlEntity> reader,
      ItemWriter<List<SparqlEntity>> writer, ItemProcessor<SparqlEntity, List<SparqlEntity>> processor) {
    return stepBuilderFactory.get("step1").<SparqlEntity, List<SparqlEntity>> chunk(1).reader(reader)
        .processor(processor).writer(writer).build();
  }
}
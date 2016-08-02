package org.glycoinfo.batch.search.wurcs;

import java.util.List;

import org.glycoinfo.batch.SparqlListItemConfig;
import org.glycoinfo.batch.SparqlListProcessor;
import org.glycoinfo.convert.GlyConvertConfig;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.glycan.MotifInsertSparql;
import org.glycoinfo.rdf.glycan.wurcs.MotifSequenceSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.WurcsRDFInsertSparql;
import org.glycoinfo.rdf.glycan.wurcs.WurcsRDFMSInsertSparql;
import org.glycoinfo.rdf.service.impl.GlycanProcedureConfig;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ VirtSesameTransactionConfig.class, GlycanProcedureConfig.class, GlyConvertConfig.class, SparqlListItemConfig.class})
public class Config {
  @Value("${wurcs.graph.target:<http://rdf.glytoucan.org/sequence/wurcs>}")
  private String graphtarget;

  @Value("${wurcs.graph.ms:<http://rdf.glytoucan.org/wurcs/ms>}")
  private String graphms;

  // graph base to set the graph to insert into. The format type (toFormat())
  // will be added to the end.
  // example: http://glytoucan.org/rdf/demo/0.7/wurcs
  public static String graphbase = "http://rdf.glytoucan.org/motif";

  @Bean(name="itemReaderSelectSparql")
  SelectSparql itemReaderSelectSparql() {
    SelectSparql select = new MotifSequenceSelectSparql();
    select.setFrom(
        "FROM <http://rdf.glytoucan.org/core> FROM <http://rdf.glytoucan.org/sequence/wurcs> FROM <http://rdf.glytoucan.org/motif>");
    return select;
  }
  
  @Bean(name="itemWriterInsertSparql")
  InsertSparql getInsertSparql() {
    MotifInsertSparql insert = new MotifInsertSparql();
    insert.setGraphBase(graphbase);
    return insert;
  }
  
  @Bean(name="itemProcessorSelectSparql")
  SelectSparql motifSparql() {
    SubstructureSearchSparql motifSparql = new SubstructureSearchSparql(); 
    motifSparql.setGraphms(graphms);
    motifSparql.setGraphtarget(graphtarget);
    return motifSparql;
  }

  @Bean
  public ItemProcessor<SparqlEntity, List<SparqlEntity>> processor() {
    SparqlListProcessor process = new SparqlListProcessor();
    process.setConverter(new MotifConverter());
    process.setPostConverter(new MotifPostConverter());
    process.setPutall(true);
    return process;
  }
  
  @Bean(name="wurcsRDFInsertSparql")
  InsertSparql wurcsRDFInsertSparql() {
    WurcsRDFInsertSparql wrdf = new WurcsRDFInsertSparql();
    wrdf.setSparqlEntity(new SparqlEntity());
    wrdf.setGraph("http://rdf.glytoucan.org/sequence/wurcs");
    return wrdf;
  }
  
  @Bean(name="wurcsRDFMSInsertSparql")
  InsertSparql wurcsRDFMSInsertSparql() {
    WurcsRDFMSInsertSparql wrdf = new WurcsRDFMSInsertSparql();
    wrdf.setSparqlEntity(new SparqlEntity());
    wrdf.setGraph("http://rdf.glytoucan.org/wurcs/ms");
    return wrdf;
  }
  
  @Bean
  SelectSparql substructureSearchSparql() {
    SubstructureSearchSparql sss = new SubstructureSearchSparql();
    sss.setGraphtarget("<http://rdf.glytoucan.org/sequence/wurcs>");
    sss.setGraphms("<http://rdf.glytoucan.org/wurcs/ms>");
    return sss;
  }
}
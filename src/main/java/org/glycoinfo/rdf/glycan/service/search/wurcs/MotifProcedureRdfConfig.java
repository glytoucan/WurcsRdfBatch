package org.glycoinfo.rdf.glycan.service.search.wurcs;

import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.MotifSequenceSelectSparql;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MotifProcedureRdfConfig {
  @Value("${wurcs.graph.target:<http://rdf.glytoucan.org/sequence/wurcs>}")
  private String graphtarget;

  @Value("${wurcs.graph.ms:<http://rdf.glytoucan.org/wurcs/ms>}")
  private String graphms;
  
  @Bean
  MotifProcedure motifProcedure() {
    return new MotifProcedureRdf();
  }
  
  @Bean(name="motifSequenceSelectSparql")
  SelectSparql motifSequenceSelectSparql() {
    SelectSparql select = new MotifSequenceSelectSparql();
    select.setFrom(
        "FROM <http://rdf.glytoucan.org/core> FROM <http://rdf.glytoucan.org/sequence/wurcs> FROM <http://rdf.glytoucan.org/motif>");
    return select;
  }

  @Bean(name="substructureSearchSparql")
  SelectSparql substructureSearchSparql() {
    SubstructureSearchSparql motifSparql = new SubstructureSearchSparql(); 
    motifSparql.setGraphms(graphms);
    motifSparql.setGraphtarget(graphtarget);
    return motifSparql;
  }
}

package org.glycoinfo.rdf.glycan.wurcs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { WurcsMSTest.class, VirtSesameTransactionConfig.class } )
@Configuration
@EnableAutoConfiguration
public class WurcsMSTest {
  
  private static final Log logger = LogFactory.getLog(WurcsMSTest.class);

	@Autowired
	SparqlDAO sparqlDAO;

//	@Autowired
//	SelectSparql selectSparql;

	
	@Autowired
	InsertSparql insertSparql;
	
//	@Bean
//	SelectSparql selectSparql() {
//		MotifSequenceSelectSparql sparql = new MotifSequenceSelectSparql();
//		return sparql;
//	}
	
	@Bean
	InsertSparql insertSparql() {
		WurcsRDFMSInsertSparql sparql = new WurcsRDFMSInsertSparql();
		return sparql;
	}
	
	@Test
	public void testInsertSparql() throws SparqlException {
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
//		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,3,2/[11122h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1");
		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
		
		insertSparql.setSparqlEntity(sparqlentity);

		logger.debug(insertSparql.getSparql());
	}
}
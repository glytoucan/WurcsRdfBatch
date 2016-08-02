package org.glycoinfo.rdf.glycan.service.search.wurcs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.batch.SparqlListItemConfig;
import org.glycoinfo.batch.search.wurcs.Config;
import org.glycoinfo.batch.search.wurcs.MotifRdfSparqlBatchTest;
import org.glycoinfo.convert.GlyConvertConfig;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { MotifProcedureTest.class, MotifProcedureRdfConfig.class, VirtSesameTransactionConfig.class})
@IntegrationTest
@EnableAutoConfiguration
public class MotifProcedureTest {
  
  private static final Log logger = LogFactory.getLog(MotifProcedureTest.class);
  
  @Autowired
  MotifProcedure motifProcedure;

  @Test
  public void testSubstructureSearch() throws Exception {
    // see SearchTest
    return;
  }

//  @Test
//  public void testFindMotifs() throws Exception {
//    
//    // structure should have motifs : G99992LL
//    String acc = "WURCS=2.0/6,10,9/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][a2112h-1b_1-5_2*NCC/3=O][Aad21122h-2a_2-6_5*NCC/3=O]/1-2-3-4-2-5-6-4-2-5/a4-b1_b4-c1_d2-e1_e4-f1_f6-g2_h2-i1_i4-j1_c?-d1_c?-h1";
//    acc = "G95954RU";
//
//    // find motifs for it
//    List<Map<String, Object>> list = motifProcedure.findMotifs(acc);
//
//    ArrayList<String> compare = new ArrayList<String>();
//    for (Map <String,Object> sparqlEntity : list) {
//      Object id = sparqlEntity.get(Saccharide.PrimaryId);
//      compare.add(id.toString());
//    }
//    
//    // check results
//    
//    ArrayList<String> correct = new ArrayList<String>();
//    correct.add("G00034MO");
//    correct.add("G00042MO");
//    correct.add("G00032MO");
//    correct.add("G00055MO");
//    correct.add("G00068MO");
//    
//    logger.debug(compare.toString());
//    Assert.assertTrue(compare.containsAll(correct));
//  }

}

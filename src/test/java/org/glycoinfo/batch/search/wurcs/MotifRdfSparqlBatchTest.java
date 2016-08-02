package org.glycoinfo.batch.search.wurcs;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.batch.SparqlItemWriter;
import org.glycoinfo.batch.SparqlListItemConfig;
import org.glycoinfo.batch.glyconvert.ConvertSelectSparql;
import org.glycoinfo.convert.GlyConvertConfig;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlBean;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Glycosidic_topology;
import org.glycoinfo.rdf.glycan.Motif;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.glycan.service.search.wurcs.MotifProcedureRdfConfig;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author aoki
 *
 *         Test cases for the Motif search batch program.
 *         The ETL for this batch:<br>
 *         <br>
 *         Extract: Select all motif wurcs strings.<br>
 *         Transform: For each string, select the super structures of the motif.<br>
 *         Load: For each super structure, insert the has_motif relationship.<br>
 * 
 *         This will attempt to execute the extract, transform and load sections
 *         individually.
 *
 *         This work is licensed under the Creative Commons Attribution 4.0
 *         International License. To view a copy of this license, visit
 *         http://creativecommons.org/licenses/by/4.0/.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { MotifRdfSparqlBatchTest.class, Config.class,
    VirtSesameTransactionConfig.class, GlyConvertConfig.class, SparqlListItemConfig.class})
@IntegrationTest
@EnableAutoConfiguration
public class MotifRdfSparqlBatchTest {
  protected Log logger = LogFactory.getLog(getClass());

  @Autowired
  SparqlDAO sparqlDAO;

  @Autowired
  @Qualifier("itemReaderSelectSparql")
  SelectSparql itemReaderSelectSparql;

  @Autowired
  @Qualifier("itemWriterInsertSparql")
  InsertSparql itemWriterInsertSparql;

  @Autowired
  ItemProcessor<SparqlEntity, List<SparqlEntity>> processor;

  @Autowired
  GlycanProcedure glycanProcedure;

  // using a weird wurcs...
  String wurcs = "WURCS=2.0/8,9,8/[a1122h-1x_1-5][a2122h-1x_1-5][a2112h-1x_1-5][a1221m-1x_1-5][a221h-1x_1-5][a2112h-1x_1-5_2*NCC/3=O][a2122h-1x_1-5_2*NCC/3=O][Aad21122h-2x_2-6_5*NCC/3=O]/1-2-3-4-5-6-7-8-4/a?-b1_b?-c1_c?-d1_d?-e1_e?-f1_f?-g1_g?-h2_h?-i1";

  String weirdG88203PI = "WURCS=2.0/2,4,3/[a2112h-1x_1-5_2*NCC/3=O][a2122h-1x_1-5_2*NCC/3=O]/1-1-2-2/a?-b1_b?-c1_c?-d1";

  @Test
  @Transactional
  public void testOne() throws Exception {

    // register one new one, just in case.
    String output = glycanProcedure.register(
        "WURCS=2.0/7,9,8/[a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][a221h-1x_1-5][a2112h-1x_1-5][a1221m-1x_1-5][a1221m-1a_1-5]/1-1-2-3-4-5-3-6-7/a4-b1_a6-i1_b4-c1_c3-d1_c6-g1_d?-e1_e?-f1_g?-h1",
        "254");

    List<SparqlEntity> onewurcsList = sparqlDAO.query(itemReaderSelectSparql);

    SparqlEntity onewurcs = onewurcsList.iterator().next();

    String accNum = onewurcs.getValue(Saccharide.PrimaryId);
    logger.debug("accNum:>" + accNum);

    // process it - find all super structures
    List<SparqlEntity> processed = processor.process(onewurcs);

    int count = 0;
    for (Iterator iterator = processed.iterator(); iterator.hasNext();) {
      SparqlEntity sparqlEntity = (SparqlEntity) iterator.next();
      itemWriterInsertSparql.setSparqlEntity(sparqlEntity);
      sparqlDAO.insert(itemWriterInsertSparql);
      count++;
    }
    
    // out of the onewurcs, select the results 
    String confirmSelect = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n" + 
        "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#> " +
        "SELECT distinct ?ResultURI\n" +
        "FROM <http://rdf.glytoucan.org/core> FROM <http://rdf.glytoucan.org/sequence/wurcs> FROM <http://rdf.glytoucan.org/motif> WHERE {\n" + 
        "?ResultURI glycan:has_motif <" + onewurcs.getValue(Motif.URI) + "> .\n" + 
        "}";
    
    SelectSparqlBean sb = new SelectSparqlBean(confirmSelect);
    List<SparqlEntity> confirmList = sparqlDAO.query(sb);
//    Assert.assertEquals(count, confirmList.size());
    
  }

//  @Test
//  @Transactional
//  public void testOneG88203PI() throws Exception {
//    SparqlEntity onewurcs = new SparqlEntity();
//
//    onewurcs.setValue(Saccharide.PrimaryId, "G88203PI");
//    onewurcs.setValue(ConvertSelectSparql.Sequence, weirdG88203PI);
//
//    String accNum = onewurcs.getValue(Saccharide.PrimaryId);
//    logger.debug("accNum:>" + accNum);
//
//    // process it - check id's (if wurcs generated is invalid then registration
//    // process will pick it up)
//    SparqlEntity processed = processor.process(onewurcs);
//    String id1 = processed.getValue(Glycosidic_topology.PrimaryId_1);
//    String id2 = processed.getValue(Glycosidic_topology.PrimaryId_2);
//    Assert.assertEquals(accNum, id1);
//    Assert.assertNotNull(id2);
//
//    itemWriterInsertSparql.setSparqlEntity(processed);
//    // insert statement
//    sparqlDAO.insert(itemWriterInsertSparql);
//  }
//
//  @Test
//  @Transactional
//  public void testNotLevel1G24678II() throws Exception {
//    SparqlEntity onewurcs = new SparqlEntity();
//
//    onewurcs.setValue(Saccharide.PrimaryId, "G24678I");
//    onewurcs.setValue(ConvertSelectSparql.Sequence, "WURCS=2.0/1,1,0/[hU122h]/1/");
//
//    String accNum = onewurcs.getValue(Saccharide.PrimaryId);
//    logger.debug("accNum:>" + accNum);
//
//    // process it - check id's (if wurcs generated is invalid then registration
//    // process will pick it up)
//    SparqlEntity processed = processor.process(onewurcs);
//    String id1 = processed.getValue(Glycosidic_topology.PrimaryId_1);
//    String id2 = processed.getValue(Glycosidic_topology.PrimaryId_2);
//    Assert.assertNull(id1);
//    Assert.assertNull(id2);
//  }
//
//  @Test
//  @Transactional
//  public void testG00029MO() throws Exception {
//    SparqlEntity onewurcs = new SparqlEntity();
//
//    onewurcs.setValue(Saccharide.PrimaryId, "G00029MO");
//    onewurcs.setValue(ConvertSelectSparql.Sequence,
//        "WURCS=2.0/4,7,6/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-2-4-4/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4_g1-f3|f6");
//
//    String accNum = onewurcs.getValue(Saccharide.PrimaryId);
//    logger.debug("accNum:>" + accNum);
//
//    // process it - check id's (if wurcs generated is invalid then registration
//    // process will pick it up)
//    SparqlEntity processed = processor.process(onewurcs);
//    String id1 = processed.getValue(Glycosidic_topology.PrimaryId_1);
//    String id2 = processed.getValue(Glycosidic_topology.PrimaryId_2);
//    Assert.assertEquals(accNum, id1);
//    Assert.assertEquals("G37692XE", id2);
//  }
//
//  @Test
//  @Transactional
//  public void testG00012RZ() throws Exception {
//    SparqlEntity onewurcs = new SparqlEntity();
//
//    onewurcs.setValue(Saccharide.PrimaryId, "G00012RZ");
//    onewurcs.setValue(ConvertSelectSparql.Sequence,
//        "WURCS=2.0/6,15,14/[a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][a1221m-1a_1-5][a2112h-1b_1-5][Aad21122h-2a_2-6_5*NCC/3=O]/1-1-2-3-3-4-1-4-5-6-1-4-5-1-5/a4-b1_a6-f1_b4-c1_c3-d1_c6-e1_g3-h1_g4-i1_k3-l1_k4-m1_n4-o1_i?-j2_g1-a?|b?|c?|d?|e?|f?}_k1-a?|b?|c?|d?|e?|f?}_n1-a?|b?|c?|d?|e?|f?}");
//
//    String accNum = onewurcs.getValue(Saccharide.PrimaryId);
//    logger.debug("accNum:>" + accNum);
//
//    // process it - check id's (if wurcs generated is invalid then registration
//    // process will pick it up)
//    SparqlEntity processed = processor.process(onewurcs);
//    String id1 = processed.getValue(Glycosidic_topology.PrimaryId_1);
//    String id2 = processed.getValue(Glycosidic_topology.PrimaryId_2);
//    Assert.assertEquals(accNum, id1);
//    Assert.assertNotNull(id1);
//    // Assert.assertEquals("G01092LE", id2);
//  }
//
//  @Test
//  @Transactional
//  public void testG03796OG() throws Exception {
//    SparqlEntity onewurcs = new SparqlEntity();
//
//    onewurcs.setValue(Saccharide.PrimaryId, "G03796OG");
//    onewurcs.setValue(ConvertSelectSparql.Sequence,
//        "WURCS=2.0/3,3,2/[a2122h-1x_1-5_2*NSO/3=O/3=O][a2121A-1x_1-5_2*OSO/3=O/3=O][a2122h-1x_1-5_2*N_6*OSO/3=O/3=O]/1-2-3/a?-b1_b?-c1");
//
//    String accNum = onewurcs.getValue(Saccharide.PrimaryId);
//    logger.debug("accNum:>" + accNum);
//
//    // process it - check id's (if wurcs generated is invalid then registration
//    // process will pick it up)
//    SparqlEntity processed = processor.process(onewurcs);
//    String id1 = processed.getValue(Glycosidic_topology.PrimaryId_1);
//    String id2 = processed.getValue(Glycosidic_topology.PrimaryId_2);
//    Assert.assertEquals(accNum, id1);
//    Assert.assertNotNull(id1);
//    // Assert.assertEquals("G01092LE", id2);
//  }
//
//  @Test
//  @Transactional
//  public void testG00026MO() throws Exception {
//    SparqlEntity onewurcs = new SparqlEntity();
//
//    onewurcs.setValue(Saccharide.PrimaryId, "G00026MO");
//    onewurcs.setValue(ConvertSelectSparql.Sequence,
//        "WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1");
//
//    String accNum = onewurcs.getValue(Saccharide.PrimaryId);
//    logger.debug("accNum:>" + accNum);
//
//    // process it - check id's (if wurcs generated is invalid then registration
//    // process will pick it up)
//    SparqlEntity processed = processor.process(onewurcs);
//    String id1 = processed.getValue(Glycosidic_topology.PrimaryId_1);
//    String id2 = processed.getValue(Glycosidic_topology.PrimaryId_2);
//    Assert.assertEquals(accNum, id1);
//    Assert.assertNotNull(id1);
//    // Assert.assertEquals("G01092LE", id2);
//  }
//
//  // G29484KH
//  // WURCS=2.0/4,6,5/[a2122h-1a_1-5][a11221h-1a_1-5][a2122h-1b_1-5_2*NCC/3=O][a2112h-1a_1-5]/1-1-1-2-3-4/a3-b1_a6-f1_b2-c1_c6-d1_d7-e1
//
//  @Test
//  @Transactional
//  public void testG29484KH() throws Exception {
//    SparqlEntity onewurcs = new SparqlEntity();
//
//    onewurcs.setValue(Saccharide.PrimaryId, "G29484KH");
//    onewurcs.setValue(ConvertSelectSparql.Sequence,
//        "WURCS=2.0/4,6,5/[a2122h-1a_1-5][a11221h-1a_1-5][a2122h-1b_1-5_2*NCC/3=O][a2112h-1a_1-5]/1-1-1-2-3-4/a3-b1_a6-f1_b2-c1_c6-d1_d7-e1");
//
//    String accNum = onewurcs.getValue(Saccharide.PrimaryId);
//    logger.debug("accNum:>" + accNum);
//
//    // process it - check id's (if wurcs generated is invalid then registration
//    // process will pick it up)
//    SparqlEntity processed = processor.process(onewurcs);
//    String id1 = processed.getValue(Glycosidic_topology.PrimaryId_1);
//    String id2 = processed.getValue(Glycosidic_topology.PrimaryId_2);
//    Assert.assertEquals(accNum, id1);
//    Assert.assertNotNull(id1);
//    // Assert.assertEquals("G01092LE", id2);
//  }
//
//  @Test
//  @Transactional
//  public void testG95507UG() throws Exception {
//    SparqlEntity onewurcs = new SparqlEntity();
//
//    onewurcs.setValue(Saccharide.PrimaryId, "G95507UG");
//    onewurcs.setValue(ConvertSelectSparql.Sequence,
//        "WURCS=2.0/1,3,4/[a2122h-1a_1-5_2*OC_3*OC_6*N]/1-1-1/a1-c4_a4-b1_b4-c1_c1-c4~4:6");
//
//    String accNum = onewurcs.getValue(Saccharide.PrimaryId);
//    logger.debug("accNum:>" + accNum);
//
//    // process it - check id's (if wurcs generated is invalid then registration
//    // process will pick it up)
//    SparqlEntity processed = processor.process(onewurcs);
//    Assert.assertNull(processed);
//  }
}
package org.glycoinfo.rdf.glycan.service.search.wurcs;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.convert.GlyConvertConfig;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.glycan.SaccharideSelectSparql;
import org.glycoinfo.rdf.glycan.search.wurcs.MotifSearchSparql;
import org.glycoinfo.rdf.glycan.wurcs.WurcsGlycoSequenceSelectSparql;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glycoinfo.rdf.service.impl.GlycanProcedureConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { SearchTest.class, GlycanProcedureConfig.class, VirtSesameTransactionConfig.class, GlyConvertConfig.class, MotifProcedureRdfConfig.class } )
@Configuration
@EnableAutoConfiguration
public class SearchTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(SearchTest.class);

  @Autowired
  GlycanProcedure glycanProcedure;

  @Autowired
	MotifProcedure motifProcedure;
	
	@Autowired
	SparqlDAO sparqlDAO;
	
	@Bean
	MotifSearchSparql getMotifSearchSparql() {
		MotifSearchSparql search = new MotifSearchSparql();
		return search;
	}
	
//	@Bean(name="substructureSearchSparql")
//	SelectSparql getSearchSparql() {
//		SubstructureSearchSparql search =  new SubstructureSearchSparql();
//		return search;
//	}
	
//	@Test
//	public void testSelectSparql() throws SparqlException {
//		MotifSearchSparql search = getMotifSearchSparql();
//		SparqlEntity sparqlentity = new SparqlEntity();
//		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
//		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,3,2/[11122h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1");
//		search.setSparqlEntity(sparqlentity);
//
//		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
//				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
//				+ "SELECT ?SaccharideURI ?PrimaryId\n"
//				+ "SELECT ?SaccharideURI ?PrimaryId\n"
//				+ "from <http://glytoucan.org/rdf/demo/0.2>\n"
//				+ "from <http://glycoinfo.org/graph/wurcs>\n\n"
//				+ "WHERE {<rdf://sacharide.test> wurcs:has_uniqueRES  ?uRES1, ?uRES2 .\n"
//				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11122h-1b_1-5> .\n"
//				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/21122h-1a_1-5> .\n"
//				+ "# RES\n"
//				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
//				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
//				+ "# LIN\n"
//				+ "?gseq wurcs:has_LIN ?LINb1a3a6 .\n"
//				+ "# LIN1\n"
//				+ "?LINb1a3a6 wurcs:has_GLIPS   ?GLIPSb1 ,   ?GLIPSa3a6 .\n"
//				+ "# LIN1: GLIPS1\n"
//				+ "?GLIPSb1 wurcs:has_GLIP ?GLIPb1 .\n"
//				+ "?GLIPb1 wurcs:has_SC_position 1 .\n"
//				+ "?GLIPb1 wurcs:has_RES ?RESb .\n"
//				+ "?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//						+ "# LIN1: GLIPS2\n"
//						+ "?GLIPSa3a6 wurcs:has_GLIP ?GLIPa3a6 .\n"
//						+ "?GLIPa3 wurcs:has_SC_position 3 .\n"
//						+ "?GLIPa3 wurcs:has_RES ?RESa .\n"
//						+ "?GLIPa6 wurcs:has_SC_position 6 .\n"
//						+ "?GLIPa6 wurcs:has_RES ?RESa .\n"
//						+ "?GLIPSa3a6 wurcs:isFuzzy \"true\"^^xsd:boolean .\n"
//								+ "}", search.getSparql());
//	}
	
//	@Test
//	public void testSearchSelectSparql() throws SparqlException {
//		MotifSearchSparql search = getMotifSearchSparql();
//		SparqlEntity sparqlentity = new SparqlEntity();
//		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
//		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-2-4-2/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4_g1-f2|f4");
//		search.setSparqlEntity(sparqlentity);
//
//		logger.debug(search.getSparql());
//		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
//				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
//				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
//				+ "FROM <http://rdf.glytoucan.org/core>\n"
//				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
//				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
//				+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
//				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
//				+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
//				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
//				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
//				+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
//				+ "# RES\n"
//				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
//				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
//				+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
//				+ "# LIN\n"
//				+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
//				+ "# LIN1\n"
//				+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
//				+ "# LIN2\n"
//				+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
//				+ "\n"
//				+ " # LIN1: GLIPS1\n"
//				+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
//				+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
//				+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
//				+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//				+ "# LIN1: GLIPS2\n"
//				+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
//				+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
//				+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
//				+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//						+ "# LIN2: GLIPS1\n"
//						+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
//						+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
//						+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
//						+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//								+ "# LIN2: GLIPS2\n"
//								+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
//								+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
//								+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
//								+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//								+ "}}\n"
//								+ "", search.getSparql());
//	}
	
	// 	0030MO
//	@Test
	public void testSearchSelectSparql0030MO() throws SparqlException {
		MotifSearchSparql search = getMotifSearchSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/1,4,3/[12122h-1b_1-5]/1-1-1-1/a4-b1_b4-c1_c4-d1");
		search.setSparqlEntity(sparqlentity);

		logger.debug(search.getSparql());
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
				+ "FROM <http://rdf.glytoucan.org/core>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
				+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
				+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
				+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
				+ "# RES\n"
				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
				+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
				+ "# LIN\n"
				+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
				+ "# LIN1\n"
				+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
				+ "# LIN2\n"
				+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
				+ "\n"
				+ " # LIN1: GLIPS1\n"
				+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
				+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
				+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
				+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
				+ "# LIN1: GLIPS2\n"
				+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
				+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
				+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
				+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
						+ "# LIN2: GLIPS1\n"
						+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
						+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
						+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
						+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "# LIN2: GLIPS2\n"
								+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
								+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
								+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
								+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "}}\n"
								+ "", search.getSparql());
	}

	//http://ts.nobu.glycoinfo.org/sparql?default-graph-uri=http%3A%2F%2Fwww.glycoinfo.org%2Fwurcs&query=PREFIX+glycan%3A+%3Chttp%3A%2F%2Fpurl.jp%2Fbio%2F12%2Fglyco%2Fglycan%23%3E%0D%0APREFIX+toucan%3A++%3Chttp%3A%2F%2Fwww.glytoucan.org%2Fglyco%2Fowl%2Fglytoucan%23%3E%0D%0ASELECT+%3FMotifURI+%3FPrimaryId+%3FGlycoSequenceURI+%3FSequence%0D%0AFROM+%3Chttp%3A%2F%2Frdf.glytoucan.org%3E%0D%0AFROM+%3Chttp%3A%2F%2Frdf.glytoucan.org%2Fsequence%2Fwurcs%3E%0D%0AWHERE+{%3FMotifURI+a+glycan%3Aglycan_motif+.%0D%0A%3FMotifURI+toucan%3Ahas_primary_id+%3FPrimaryId+.%0D%0A%3FMotifURI+glycan%3Ahas_glycosequence+%3FGlycoSequenceURI+.%0D%0A%3FGlycoSequenceURI+glycan%3Ahas_sequence+%3FSequence+.%0D%0A%3FGlycoSequenceURI+glycan%3Ain_carbohydrate_format+glycan%3Acarbohydrate_format_wurcs+.}%0D%0A&format=text%2Fhtml&timeout=0&debug=on
	// G00026MO

//	@Test
//	public void testSearchSelectSparql0026MO() throws SparqlException {
//		MotifSearchSparql search = getMotifSearchSparql();
//		SparqlEntity sparqlentity = new SparqlEntity();
//		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
//		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1");
//		//											   WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1
//		search.setSparqlEntity(sparqlentity);
//
//		logger.debug(search.getSparql());
//		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
//				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
//				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
//				+ "FROM <http://rdf.glytoucan.org/core>\n"
//				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
//				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
//				+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
//				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
//				+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
//				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
//				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
//				+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
//				+ "# RES\n"
//				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
//				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
//				+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
//				+ "# LIN\n"
//				+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
//				+ "# LIN1\n"
//				+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
//				+ "# LIN2\n"
//				+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
//				+ "\n"
//				+ " # LIN1: GLIPS1\n"
//				+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
//				+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
//				+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
//				+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//				+ "# LIN1: GLIPS2\n"
//				+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
//				+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
//				+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
//				+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//						+ "# LIN2: GLIPS1\n"
//						+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
//						+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
//						+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
//						+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//								+ "# LIN2: GLIPS2\n"
//								+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
//								+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
//								+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
//								+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//								+ "}}\n"
//								+ "", search.getSparql());
//	}

	
	// WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4-4-4/a4-b1_b4-c1_c3-d1_c6-e1_e3-f1_e6-g1
	// 28MO
	
//	@Test
//	public void testSearchSelectSparql0028MO() throws SparqlException {
//		MotifSearchSparql search = getMotifSearchSparql();
//		SparqlEntity sparqlentity = new SparqlEntity();
//		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
//		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4-4-4/a4-b1_b4-c1_c3-d1_c6-e1_e3-f1_e6-g1");
//		search.setSparqlEntity(sparqlentity);
//
//		logger.debug(search.getSparql());
//		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
//				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
//				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
//				+ "FROM <http://rdf.glytoucan.org/core>\n"
//				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
//				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
//				
//				+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
//				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
//				+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
//				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
//				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
//				+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
//				+ "# RES\n"
//				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
//				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
//				+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
//				+ "# LIN\n"
//				+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
//				+ "# LIN1\n"
//				+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
//				+ "# LIN2\n"
//				+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
//				+ "\n"
//				+ " # LIN1: GLIPS1\n"
//				+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
//				+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
//				+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
//				+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//				+ "# LIN1: GLIPS2\n"
//				+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
//				+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
//				+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
//				+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//						+ "# LIN2: GLIPS1\n"
//						+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
//						+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
//						+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
//						+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//								+ "# LIN2: GLIPS2\n"
//								+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
//								+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
//								+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
//								+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//								+ "}}\n"
//								+ "", search.getSparql());
//	}

	
	// 31MO
	// WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1
//	@Test
//	public void testSearchSelectSparql0031MO() throws SparqlException {
//		MotifSearchSparql search = getMotifSearchSparql();
//		SparqlEntity sparqlentity = new SparqlEntity();
//		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
//		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1");
//		search.setSparqlEntity(sparqlentity);
//
//		logger.debug(search.getSparql());
//		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
//				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
//				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
//				+ "FROM <http://rdf.glytoucan.org/core>\n"
//				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
//				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
//				+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
//				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
//				+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
//				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
//				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
//				+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
//				+ "# RES\n"
//				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
//				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
//				+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
//				+ "# LIN\n"
//				+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
//				+ "# LIN1\n"
//				+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
//				+ "# LIN2\n"
//				+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
//				+ "\n"
//				+ " # LIN1: GLIPS1\n"
//				+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
//				+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
//				+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
//				+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//				+ "# LIN1: GLIPS2\n"
//				+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
//				+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
//				+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
//				+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//						+ "# LIN2: GLIPS1\n"
//						+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
//						+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
//						+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
//						+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//								+ "# LIN2: GLIPS2\n"
//								+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
//								+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
//								+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
//								+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//								+ "}}\n"
//								+ "", search.getSparql());
//	}
	
	// G07490EK
//	WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a3-b1
	
			
			// 31MO
			// WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1
//			@Test
//			public void testSearchSelectSparqlG07490EK() throws SparqlException {
//				MotifSearchSparql search = getMotifSearchSparql();
//				SparqlEntity sparqlentity = new SparqlEntity();
//				sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
//				sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a3-b1");
//				search.setSparqlEntity(sparqlentity);
//
//				logger.debug(search.getSparql());
//				assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
//						+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
//						+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
//						+ "FROM <http://rdf.glytoucan.org/core>\n"
//						+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
//						+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
//						+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
//						+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
//						+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
//						+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
//						+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
//						+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
//						+ "# RES\n"
//						+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
//						+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
//						+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
//						+ "# LIN\n"
//						+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
//						+ "# LIN1\n"
//						+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
//						+ "# LIN2\n"
//						+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
//						+ "\n"
//						+ " # LIN1: GLIPS1\n"
//						+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
//						+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
//						+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
//						+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//						+ "# LIN1: GLIPS2\n"
//						+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
//						+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
//						+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
//						+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//								+ "# LIN2: GLIPS1\n"
//								+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
//								+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
//								+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
//								+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//										+ "# LIN2: GLIPS2\n"
//										+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
//										+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
//										+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
//										+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
//										+ "}}\n"
//										+ "", search.getSparql());
//			}
	
			// G51379UR
			//WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a6-b1
		
//			@Test
//			public void testSearchSelectSparqlG51379UR() throws SparqlException {
//				MotifSearchSparql search = getMotifSearchSparql();
//				SparqlEntity sparqlentity = new SparqlEntity();
//				sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
//				sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a6-b1");
//				search.setSparqlEntity(sparqlentity);
//
//				logger.debug(search.getSparql());
//				assertEquals("", search.getSparql());
//			}

			// G72618IM
			// WURCS=2.0/2,3,2/[11122h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1

//			@Test
//			public void testSearchSelectSparqlG72618IM() throws SparqlException {
//				MotifSearchSparql search = getMotifSearchSparql();
//				SparqlEntity sparqlentity = new SparqlEntity();
//				sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
//				sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,3,2/[11122h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1");
//				search.setSparqlEntity(sparqlentity);
//
//				logger.debug(search.getSparql());
//				assertEquals("", search.getSparql());
//			}
			
//	@Test
//	public void testInsertSparql() {
//		ConvertInsertSparql convert = getConvertInsertSparql();
//		SparqlEntity se = new SparqlEntity();
//		se.setValue("SaccharideURI", "<testSaccharideURI>");
//		se.setValue("SequenceURI", "<testSequenceURI>");
//		se.setValue("ConvertedSequence", "testConvertedSequence!");
//		convert.setSparqlEntity(se);
//		assertEquals("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
//				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
//				+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
//				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
//				+ "INSERT INTO\n"
//				+ "GRAPH <http://glytoucan.org/rdf/demo/0.7wurcs>\n"
//				+ "USING <http://glytoucan.org/rdf/demo/0.2>\n"
//				+ "USING <http://glytoucan.org/rdf/demo/msdb/8>\n"
//				+ "USING <http://purl.jp/bio/12/glyco/glycan/ontology/0.18>\n"
//				+ "USING <http://www.glytoucan.org/glyco/owl/glytoucan>\n"
//				+ "{ <testSaccharideURI> glycan:has_glycosequence <testSequenceURI> .\n"
//				+ "<testSequenceURI> glycan:has_sequence \"testConvertedSequence!\"^^xsd:string .\n"
//				+ "<testSequenceURI> glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs .\n"
//				+ "<testSequenceURI> glytoucan:is_glycosequence_of <testSaccharideURI> .\n }\n", convert.getSparql());
//	}
			@Test
			@Transactional
			public void testRegisterAndSelect() throws SparqlException, MotifException {
				String sequence = "RES\n" + 
				    "1b:b-dglc-HEX-1:5\n" + 
				    "2s:n-acetyl\n" + 
				    "3b:b-dglc-HEX-1:5\n" + 
				    "4s:n-acetyl\n" + 
				    "5b:b-dman-HEX-1:5\n" + 
				    "6b:a-dman-HEX-1:5\n" + 
				    "7b:a-dman-HEX-1:5\n" + 
				    "8b:x-llyx-PEN-1:5\n" + 
				    "9b:x-lgal-HEX-1:5|6:d\n" + 
				    "LIN\n" + 
				    "1:1d(2+1)2n\n" + 
				    "2:1o(4+1)3d\n" + 
				    "3:3d(2+1)4n\n" + 
				    "4:3o(4+1)5d\n" + 
				    "5:5o(3+1)6d\n" + 
				    "6:5o(6+1)7d\n" + 
				    "7:7o(-1+1)8d\n" + 
				    "8:8o(-1+1)9d";
				
				logger.debug("sequence:>" + sequence + "<");
//				glycanProcedure.setSequence(sequence);
//				glycanProcedure.setContributorId("1");
				glycanProcedure.setBatch(false);
				String id = glycanProcedure.register(sequence, "1");
				
				SparqlEntity se = new SparqlEntity();
				se.setValue(Saccharide.PrimaryId, id);
				
				SaccharideSelectSparql select = new SaccharideSelectSparql();
				select.setFrom("FROM <http://rdf.glytoucan.org/core>\n");
				select.setSparqlEntity(se);

				List<SparqlEntity> sachList = sparqlDAO.query(select);
				
				logger.debug("size:>" + sachList.size());				

				WurcsGlycoSequenceSelectSparql glycoSelect = new WurcsGlycoSequenceSelectSparql();
				glycoSelect.setFrom("FROM <http://rdf.glytoucan.org/sequence/wurcs>\nFROM <http://rdf.glytoucan.org/core>\n");
				glycoSelect.setSparqlEntity(se);

				List<SparqlEntity> glycosachList = sparqlDAO.query(glycoSelect);
				
				logger.debug(glycosachList.toString());
				logger.debug("size:>" + glycosachList.size());				
				
				org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql gSelect = new org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql();
				gSelect.setFrom("FROM <http://rdf.glytoucan.org/sequence/wurcs>\nFROM <http://rdf.glytoucan.org/core>\n");
				gSelect.setSparqlEntity(se);

				List<SparqlEntity> gList = sparqlDAO.query(gSelect);
				
				logger.debug(gList.toString());
				logger.debug("size:>" + gList.size());				
				
				se = glycanProcedure.searchByAccessionNumber(id);
				Assert.assertNotNull(se.getValue(GlycoSequence.Sequence));
				
				logger.debug(se.toString());
				
				sequence = se.getValue(GlycoSequence.Sequence);
				List<Map<String, Object>> list = motifProcedure.substructureSearch(sequence, "100", "0");
				Map<String, Object> resultSE = list.get(0);
				Assert.assertNotNull(resultSE.get(SubstructureSearchSparql.SubstructureSearchSaccharideURI));
			
				boolean foundit=false;
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				  Map<String, Object> sparqlEntity = (Map<String, Object>) iterator.next();
					String resultID = (String) sparqlEntity.get(SubstructureSearchSparql.SubstructureSearchSaccharideURI);
					if (StringUtils.isNotBlank(resultID) && resultID.contains(id))
						foundit=true;
				}
				Assert.assertTrue(foundit);
			}
			
			@Test
			public void testCodePoints() {
				String test = "this is a | Test & needs to be translated into C0depoint!";
				StringBuilder sb = new StringBuilder();
				
				sb.appendCodePoint('|');
				
				logger.debug(sb.toString());				
//				String converted = test.replaceAll("\\|", sb.toString());
//				str.replaceAll("[|]","pipe");
				
//				String converted = StringEscapeUtils.escapeJava(test);
				
				String converted = "";
				converted = test.replaceAll("[|]", unicodeEscaped('|'));
				converted = converted.replaceAll("[&]", unicodeEscaped('&'));

				logger.debug(converted);
			}
			
			  public static String unicodeEscaped(char ch) {
			      if (ch < 0x10) {
			          return "\\u000" + Integer.toHexString(ch);
			      } else if (ch < 0x100) {
			          return "\\u00" + Integer.toHexString(ch);
			      } else if (ch < 0x1000) {
			          return "\\u0" + Integer.toHexString(ch);
			      }
			      return "\\u" + Integer.toHexString(ch);
			  }
}
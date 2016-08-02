package org.glycoinfo.rdf.glycan.search.wurcs;

import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MotifSearchSparql extends SelectSparqlBean {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(MotifSearchSparql.class);

	public MotifSearchSparql() {
		this.define = "DEFINE sql:select-option \"order\"";
		this.prefix = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "PREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#>";
		this.select = "DISTINCT ?" + Saccharide.URI;
//				+ " ?" + Saccharide.PrimaryId; 
		this.from = "FROM <http://rdf.glytoucan.org/core>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "FROM <http://rdf.glycoinfo.org/wurcs/0.5.0>\n"
				+ "FROM <http://rdf.glycoinfo.org/wurcs/0.5.0/ms>";
	}

	@Override
	public String getWhere() throws SparqlException {
		this.where = ""
//	"?" + Saccharide.URI + " toucan:has_primary_id ?" + Saccharide.PrimaryId + " .\n"
//				+ "GRAPH <http://www.glycoinfo.org/wurcs> {"
//				+ "SELECT *\n"
//				+ "WHERE {\n"
				+ "?" + Saccharide.URI + " glycan:has_glycosequence ?gseq .\n";
				
//				"?glycans a glycan:glycan_motif .\n"
//				+ "?glycans glycan:has_glycosequence ?gseq .\n"
//				+ "?gseq glycan:has_sequence ?Seq .\n"
//				+ "?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct\n";
		SubstructureSearchSparql substructureSearchSparql = new SubstructureSearchSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(GlycoSequence.Sequence, getSparqlEntity().getValue(GlycoSequence.Sequence));
		substructureSearchSparql.setSparqlEntity(se);
//		try {
			this.where += substructureSearchSparql.getWhere();
//		} catch (WURCSFormatException e) {
//			throw new SparqlException(e);
//		}
		
//		this.where += "}";
//		this.where += "}";
		return where;
	}

/*
	 * 
	 * total list:
	 * 
	 * PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> PREFIX toucan:
	 * <http://www.glytoucan.org/glyco/owl/glytoucan#> SELECT DISTINCT ?glycans
	 * ?AccessionNumber ?Seq from <http://glytoucan.org/rdf/demo/0.6> from
	 * <http://glytoucan.org/rdf/demo/0.6/rdf> WHERE { ?glycans a
	 * glycan:saccharide . ?glycans toucan:has_primary_id ?AccessionNumber .
	 * ?glycans glycan:has_glycosequence ?gseq . ?gseq glycan:has_sequence ?Seq
	 * . ?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf }
	 * 
	 * 
	 * motifs to check:
	 * 
	 * PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> PREFIX toucan:getSearchSparql
	 * <http://www.glytoucan.org/glyco/owl/glytoucan#> SELECT DISTINCT ?glycans
	 * ?AccessionNumber ?Seq from <http://glytoucan.org/rdf/demo/0.6> from
	 * <http://glytoucan.org/rdf/demo/0.6/rdf> WHERE { ?glycans a
	 * glycan:glycan_motif . ?glycans toucan:has_primary_id ?AccessionNumber .
	 * ?glycans glycan:has_glycosequence ?gseq . ?gseq glycan:has_sequence ?Seq
	 * . ?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf }
	 * 
	 * 
	 * PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> PREFIX toucan:
	 * <http://www.glytoucan.org/glyco/owl/glytoucan#> SELECT DISTINCT ?glycans
	 * ?id ?Seq from <http://glytoucan.org/rdf/demo/0.6> from
	 * <http://bluetree.jp/test> WHERE { ?glycans a glycan:glycan_motif .
	 * ?glycans toucan:has_primary_id ?id . ?glycans glycan:has_glycosequence
	 * ?gseq . ?gseq glycan:has_sequence ?Seq . ?gseq
	 * glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf }
	 * 
	 * glycan:has_motif
	 */

	// triple store batch process
	// conversion
	// retrieve a list of asc#
	// for each asc #
	// convert to kcf
	// insert into rdf

	// mass

	// motif/substructure
	// retrieve a list of asc#
	// for each asc# -
	// retrieve list of structures to find subs
	// search for substructure - kcam
	// insert substructure/motif
}
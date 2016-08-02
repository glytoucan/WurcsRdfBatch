package org.glycoinfo.rdf.glycan.service.search.wurcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.wurcs.MotifSequenceSelectSparql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MotifProcedureRdf implements MotifProcedure {
  
  private static final Log logger = LogFactory.getLog(MotifProcedureRdf.class);

  @Autowired
  @Qualifier("motifSequenceSelectSparql")
  SelectSparql motifSequenceSelectSparql;

  @Autowired
  @Qualifier("substructureSearchSparql")
  SelectSparql substructureSearchSparql;
  
  @Autowired
  SparqlDAO sparqlDAO;

  @Override
  public List<Map<String, Object>> substructureSearch(String sequence, String limit, String offset) throws MotifException {

    // wurcs to sparql
    SparqlEntity se = new SparqlEntity();
    se.setValue(GlycoSequence.Sequence, sequence);
    substructureSearchSparql.setLimit(limit);
    substructureSearchSparql.setOffset(offset);
    substructureSearchSparql.setSparqlEntity(se);
    List<SparqlEntity> results;
    try {
      results = sparqlDAO.query(substructureSearchSparql);
    } catch (SparqlException e) {
      throw new MotifException(e);
    }
    ArrayList<Map<String, Object>> stringResults = new ArrayList<Map<String, Object>>();
    for (SparqlEntity sparqlEntity : results) {
      stringResults.add(sparqlEntity.getData());
    }
    
    return stringResults;
  }

  @Override
  public List<Map<String, Object>> findMotifs(String acc) throws MotifException {
    motifSequenceSelectSparql.setOrderBy("PrimaryId");
    List<SparqlEntity> motifList;
    try {
      motifList = sparqlDAO.query(motifSequenceSelectSparql);
    } catch (SparqlException e) {
      throw new MotifException(e);
    }

    ArrayList<SparqlEntity> results = new ArrayList<SparqlEntity>();
    for (SparqlEntity motifSparqlEntity : motifList) {
      String motifSequence = motifSparqlEntity.getValue(GlycoSequence.Sequence);
      String motifId = motifSparqlEntity.getValue(GlycoSequence.AccessionNumber);
      SparqlEntity se = new SparqlEntity();

      se.setValue(GlycoSequence.Sequence, motifSequence);
      se.setValue(GlycoSequence.AccessionNumber, acc);
      se.setValue(SubstructureSearchSparql.LIMITID, "true");
      substructureSearchSparql.setSparqlEntity(se);
      List<SparqlEntity> listResult;
      try {
        listResult = sparqlDAO.query(substructureSearchSparql);
      } catch (SparqlException e) {
        throw new MotifException(e);
      }
      logger.debug("checking motif:>" + motifId);
      logger.debug("listResult:>" + listResult);
      if (!listResult.isEmpty()) {
        logger.debug("adding!" + motifId);
        results.add(motifSparqlEntity);
      }
    }

    List<Map<String, Object>> stringResults = new ArrayList<Map<String, Object>>();
    for (SparqlEntity sparqlEntity : results) {
      stringResults.add(sparqlEntity.getData());
    }
    
    return stringResults;
  }
}

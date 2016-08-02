package org.glycoinfo.rdf.glycan.service.search.wurcs;

import java.util.List;
import java.util.Map;

public interface MotifProcedure {

  List<Map<String, Object>> findMotifs(String acc) throws MotifException;

  List<Map<String, Object>> substructureSearch(String sequence, String limit, String offset) throws MotifException;

}
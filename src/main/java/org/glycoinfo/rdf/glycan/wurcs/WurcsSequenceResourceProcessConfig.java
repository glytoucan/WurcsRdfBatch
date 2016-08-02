package org.glycoinfo.rdf.glycan.wurcs;

import org.glycoinfo.convert.GlyConvertConfig;
import org.glycoinfo.rdf.service.impl.GlycanProcedureConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value={GlycanProcedureConfig.class, GlyConvertConfig.class})
public class WurcsSequenceResourceProcessConfig {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(WurcsSequenceResourceProcessConfig.class);
	
	@Bean
	WurcsSequenceResourceProcess wurcsSequenceResourceProcess() {
		return new WurcsSequenceResourceProcess();
	}
}
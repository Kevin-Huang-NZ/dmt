package mahara.generator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "generator")
@Data
public class GeneratorConfig {
	private String onOff;
	private String templateFolder;
	private String outputFolder;
	private String baseFolder;
	private String javaSourceFolder;
	private String mapperPkg;
	private String modelPkg;
	private String servicePkg;
	private String serviceImplPkg;
	private String controllerPkg;
	private String dbUrl;
	private String dbRootPassword;
	private String tableSchema;
}

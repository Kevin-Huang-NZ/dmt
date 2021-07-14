package mahara.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.CaseFormat;

import freemarker.template.Configuration;
import freemarker.template.Template;
import mahara.util.MakeDir;

@Component
public class Generator {
	Logger logger = LoggerFactory.getLogger(Generator.class);

	@Autowired
	private Configuration freeMarkerCfg;

	private List<String> targetNames = new ArrayList<String>();

	private String javaSourceParh = "";
	private String outputParh = "";

	@Autowired
	private GeneratorConfig generatorConfig;

	public void generate() {
		this.javaSourceParh = generatorConfig.getBaseFolder() + generatorConfig.getJavaSourceFolder();
		this.outputParh = generatorConfig.getBaseFolder() + generatorConfig.getOutputFolder();
		this.getNames();
		this.generateService();
		this.generateApi();
		this.generateController();
		this.generateAllHtml();
		this.generateAuthData();
//		this.generatePageHtml("sys_page","page.ftl");
//		this.generatePageHtml("sys_role","page.ftl");
//		this.generatePageHtml("sys_fun","page.ftl");
		
	}

	private void getNames() {
		targetNames = new ArrayList<String>();

		File mapperFolder = new File(javaSourceParh + convertPkgToPath(generatorConfig.getMapperPkg()));
		File[] mappers = mapperFolder.listFiles();
		
		for (int i = 0; i < mappers.length; i++) {
			String mapperFileName = mappers[i].getName();
			String targetName = mapperFileName.substring(0, mapperFileName.indexOf("Mapper"));
			targetNames.add(targetName);
		}
	}

	private void generateService() {

		OutputStreamWriter out = null;
		try {
			Template template = freeMarkerCfg.getTemplate("service.ftl");
			Template templateImpl = freeMarkerCfg.getTemplate("service-impl.ftl");

			for (int i = 0; i < this.targetNames.size(); i++) {
				String targetName = targetNames.get(i);
				String lcTargetName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, targetName);

				Map<String, Object> root = new HashMap<String, Object>();
				root.put("targetName", targetName);
				root.put("lcTargetName", lcTargetName);
				root.put("modelPkg", generatorConfig.getModelPkg());
				root.put("mapperPkg", generatorConfig.getMapperPkg());
				root.put("servicePkg", generatorConfig.getServicePkg());
				root.put("serviceImplPkg", generatorConfig.getServiceImplPkg());

				String packageDir = MakeDir.makeByPackage(generatorConfig.getServicePkg(), this.outputParh);
				String fileName = packageDir + "/" + targetName + "Service.java";
				out = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
				template.process(root, out);

				out.close();

				packageDir = MakeDir.makeByPackage(generatorConfig.getServiceImplPkg(), this.outputParh);
				fileName = packageDir + "/" + targetName + "ServiceImpl.java";
				out = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
				templateImpl.process(root, out);

				out.close();

			}
		} catch (Exception e) {
			logger.error("Failed to get template.", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void generateController() {

		OutputStreamWriter out = null;
		try {
			Template template = freeMarkerCfg.getTemplate("controller.ftl");

			for (int i = 0; i < this.targetNames.size(); i++) {
				String targetName = targetNames.get(i);
				
				String lcTargetName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, targetName);
				String lowerTargetName = targetName.toLowerCase();

				Map<String, Object> root = new HashMap<String, Object>();
				root.put("targetName", targetName);
				root.put("lowerTargetName", lowerTargetName);
				root.put("lcTargetName", lcTargetName);
				root.put("controllerPkg", generatorConfig.getControllerPkg());
				root.put("modelPkg", generatorConfig.getModelPkg());
				root.put("servicePkg", generatorConfig.getServicePkg());

				String packageDir = MakeDir.makeByPackage(generatorConfig.getControllerPkg(), this.outputParh);
				String fileName = packageDir + "/" + targetName + "Controller.java";
				out = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
				template.process(root, out);

				out.close();

			}
		} catch (Exception e) {
			logger.error("Failed to get template.", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void generateApi() {

		OutputStreamWriter out = null;
		try {
			Template template = freeMarkerCfg.getTemplate("api.ftl");
			String pagesDir = MakeDir.make("pages", this.outputParh);
			String fileName = pagesDir + "/all_api.js";
			File oldFile = new File(fileName);
			if (oldFile.exists()) {
				oldFile.delete();
			}

			out = new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8");
			for (int i = 0; i < this.targetNames.size(); i++) {
				String targetName = targetNames.get(i);
				
				String lowerTargetName = targetName.toLowerCase();

				Map<String, Object> root = new HashMap<String, Object>();
				root.put("targetName", targetName);
				root.put("lowerTargetName", lowerTargetName);
				template.process(root, out);
			}
		} catch (Exception e) {
			logger.error("Failed to get template.", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void generateAllHtml() {
		for (int i = 0; i < this.targetNames.size(); i++) {
			String targetName = targetNames.get(i);
			String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, targetName);
			this.generatePageHtml(tableName, "page.ftl");
		}
	}

	private void generatePageHtml(String tableNameParam, String tmplFile) {
		OutputStreamWriter out = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = "select t.table_name, t.column_name, t.is_nullable, t.character_maximum_length from `information_schema`.`columns` t where t.table_schema = ? and t.table_name = ? order by t.ordinal_position";
		try {
			conn = DriverManager.getConnection(generatorConfig.getDbUrl(), "root", generatorConfig.getDbRootPassword());
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, generatorConfig.getTableSchema());
			stmt.setString(2, tableNameParam);
			ResultSet rs = stmt.executeQuery();

			String tableName = "";
			String firstFieldName = "";
			String secondFieldName = "";
			int index = 0;
			List<Map<String, Object>> fields = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				tableName = rs.getString("table_name");
				String columnName = rs.getString("column_name");
				if ("id".equals(columnName)) {
					continue;
				}
				Map<String, Object> fieldMap = new HashMap<String, Object>();
				String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
				Long maxLen = rs.getLong("character_maximum_length");
				String isNullable = rs.getString("is_nullable");
//         if (maxLen == null) {
//        	 maxLen = 0l;
//         }
				fieldMap.put("fieldName", fieldName);
				fieldMap.put("maxLen", maxLen);
				fieldMap.put("isNullable", isNullable);

				fields.add(fieldMap);
				
				index++;
				if (index == 1) {
					firstFieldName = fieldName;
				} else if (index == 2) {
					secondFieldName = fieldName;
				}
			}
			rs.close();

			Template template = freeMarkerCfg.getTemplate(tmplFile);

			String beanName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
			String urlPath = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName).toLowerCase();

			Map<String, Object> root = new HashMap<String, Object>();
			root.put("beanName", beanName);
			root.put("urlPath", urlPath);
			root.put("firstFieldName", firstFieldName);
			root.put("secondFieldName", secondFieldName);
			root.put("fields", fields);

			String fileName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, tableName);

			String pagesDir = MakeDir.make("pages", this.outputParh);
			String fileFullName = pagesDir + "/" + fileName + ".vue";
			out = new OutputStreamWriter(new FileOutputStream(fileFullName), "UTF-8");
			template.process(root, out);

			out.close();
		} catch (Exception e) {
			logger.error("Failed to get template.", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private void generateAuthData() {

		OutputStreamWriter out = null;
		try {
			Template template = freeMarkerCfg.getTemplate("auth-sql.ftl");
			String pagesDir = MakeDir.make("sql", this.outputParh);
			String fileName = pagesDir + "/all_auth.sql";
			File oldFile = new File(fileName);
			if (oldFile.exists()) {
				oldFile.delete();
			}

			out = new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8");
			for (int i = 0; i < this.targetNames.size(); i++) {
				String targetName = targetNames.get(i);
				
				String lowerTargetName = targetName.toLowerCase();

				Map<String, Object> root = new HashMap<String, Object>();
				root.put("lowerTargetName", lowerTargetName);
				template.process(root, out);
			}
		} catch (Exception e) {
			logger.error("Failed to get template.", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private String convertPkgToPath(String pkg) {
		String path = pkg.replaceAll("\\.", "/");
		return path;
	}

//	public static void main(String[] args) {
//		try {
//			List<Field> fieldList = new ArrayList<>();
//			Class tempClass = Class.forName("cn.gov.mca.dms.core.dto.DmExportDetailDoWithBLOBs");
//			while (tempClass != null) {
//				fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
//				tempClass = tempClass.getSuperclass();
//			}
//			for (Field f : fieldList) {
//				System.out.println("name:" + f.getName() + "-------type:"
//						+ f.getType().getName().substring(f.getType().getName().lastIndexOf(".") + 1));
//			}
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}

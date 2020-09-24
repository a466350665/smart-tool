package com.smart.tool.system;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.smart.tool.generate.Controller;
import com.smart.tool.generate.Dao;
import com.smart.tool.generate.Mapper;
import com.smart.tool.generate.Model;
import com.smart.tool.generate.Service;
import com.smart.tool.generate.ServiceImpl;

/**
 * 生成器主类
 * 
 * @author Joe
 */
public class Generator extends BaseFrame {

	private static final long serialVersionUID = 6800734227505322482L;
	
	private static final String ADMIN = "admin";

	public static void main(String[] args) {
		Generator editor = new Generator();
		editor.setDefaultCloseOperation(3);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ownerSize = new Dimension(680, 530);
		editor.setSize(ownerSize);
		editor.setLocation((screenSize.width - ownerSize.width) / 2, (screenSize.height - ownerSize.height) / 2);
		editor.setVisible(true);
	}

	protected void changeTextValue() {
	    String moduleString = (StringUtils.isNotBlank(moduleText.getText()) ?  "/" + moduleText.getText() : "");
		this.modelText.setText(getPackageName() + "model" + moduleString
				+ (analyzer != null ? "/" + analyzer.getModelName() + ".java" : ""));
		this.serviceText.setText(getPackageName() + "service" + moduleString
				+ (analyzer != null ? "/" + analyzer.getModelName() + "Service.java" : ""));
		this.ServiceImplText.setText(getPackageName() + "service/impl" + moduleString
				+ (analyzer != null ? "/" + analyzer.getModelName() + "ServiceImpl.java" : ""));
		this.daoText.setText(getPackageName() + "dao" + moduleString
				+ (analyzer != null ? "/" + analyzer.getModelName() + "Dao.java" : ""));
		this.modelXmlText.setText(getPackageName() + "mapper" + moduleString
				+ (analyzer != null ? "/" + analyzer.getModelName() + ".xml" : ""));
		
		String adminString = (adminCheckBox.isSelected() ? "/" + ADMIN : "");
		this.controllerText.setText(getPackageName() + "controller" + adminString + moduleString
				+ (analyzer != null ? "/" + analyzer.getModelName() + "Controller.java" : ""));

//	    this.listText.setText(getViewBasePath() + adminString + moduleString
//	        + (analyzer != null ? "/" + analyzer.getLowerModelName() + ".jsp" : ""));
//	    this.editText.setText(getViewBasePath()  + adminString + moduleString
//	        + (analyzer != null ? "/" + analyzer.getLowerModelName() + "Edit.jsp" : ""));
	}

	private String getPackageName() {
		return getClassBasePath() + "/" + "com" + "/" + config.getCompanyName() + "/"
				+ (StringUtils.isNotBlank(projectText.getText()) ? projectText.getText() + "/" : "");
	}

	private String getClassBasePath() {
		return "src/main/java";
	}

//	private String getViewBasePath() {
//		return "src/main/webapp/WEB-INF/view";
//	}

	@Override
    protected void generateFile(String basePath) {
        generateModelFile(basePath);
        generateModelXmlFile(basePath);
        generateServiceFile(basePath);
        generateServiceImplFile(basePath);
        generateDaoFile(basePath);
        generateControllerFile(basePath);
//        generateListFile(basePath);
//        generateEditFile(basePath);
    }

	private void generateModelFile(String basePath) {
		List<DummyField> allList = analyzer.getFieldList();
		Set<String> excludeFieldSet = persistentMap.get(extendsBox.getSelectedItem());
		List<DummyField> fieldList = new ArrayList<>();
		boolean containDate = false;
		boolean containDecimal = false;
		for (DummyField dumField : allList) {
			if (excludeFieldSet.contains(dumField.getFieldName())) {
				continue;
			}
			if ("Date".equals(dumField.getFieldType())) {
				containDate = true;
			}
			else if("BigDecimal".equals(dumField.getFieldType())) {
			    containDecimal = true;
			}
			fieldList.add(dumField);
		}
		FileUtils.createFile(
				basePath,
				modelText.getText(),
				new Model(config.getCompanyName(), projectText.getText(), moduleText.getText(),
						analyzer.getModelName(), fieldList, analyzer.getTableName(), extendsBox.getSelectedItem()
								.toString(), Long.valueOf(StringUtils.getRandom(17)).toString(), analyzer
								.isContainEnable(), containDate, containDecimal, analyzer.getTableComment()).getHtml());
	}

	private void generateModelXmlFile(String basePath) {
		List<DummyField> allList = analyzer.getFieldList();
		Set<String> excludeFieldSet = persistentMap.get(extendsBox.getSelectedItem());
		List<DummyField> fieldList = new ArrayList<>();
		boolean containDate = false;
		for (DummyField dumField : allList) {
			if (excludeFieldSet.contains(dumField.getFieldName())) {
				continue;
			}
			if ("Date".equals(dumField.getFieldType())) {
				containDate = true;
			}
			fieldList.add(dumField);
		}
		FileUtils.createFile(
				basePath,
				modelXmlText.getText(),
				new Mapper(config.getCompanyName(), projectText.getText(), moduleText.getText(), analyzer
						.getModelName(), fieldList, analyzer.getTableName(), extendsBox.getSelectedItem().toString(),
						Long.valueOf(StringUtils.getRandom(17)).toString(), analyzer.isContainEnable(), containDate,
						analyzer.getTableComment()).getHtml());
	}

	private void generateServiceFile(String basePath) {
		FileUtils.createFile(
				basePath,
				serviceText.getText(),
				new Service(config.getCompanyName(), projectText.getText(), moduleText.getText(), analyzer
						.getModelName(), analyzer.isContainEnable()).getHtml());
	}

	private void generateServiceImplFile(String basePath) {
		FileUtils.createFile(
				basePath,
				ServiceImplText.getText(),
				new ServiceImpl(config.getCompanyName(), projectText.getText(), moduleText.getText(), analyzer
						.getModelName(), analyzer.isContainEnable()).getHtml());
	}

	private void generateDaoFile(String basePath) {
		FileUtils.createFile(basePath, daoText.getText(), new Dao(config.getCompanyName(), projectText.getText(),
				moduleText.getText(), analyzer.getModelName(), analyzer.isContainEnable()).getHtml());
	}

	private void generateControllerFile(String basePath) {
		List<DummyField> allList = analyzer.getFieldList();
		Set<String> excludeFieldSet = persistentMap.get(extendsBox.getSelectedItem());
		List<DummyField> fieldList = new ArrayList<DummyField>();
		boolean containDate = false;
		boolean containDecimal = false;
		for (DummyField dumField : allList) {
			if (excludeFieldSet.contains(dumField.getFieldName())) {
				continue;
			}
			if ("Date".equals(dumField.getFieldType())) {
				containDate = true;
			}
			else if("BigDecimal".equals(dumField.getFieldType())) {
                containDecimal = true;
            }
			fieldList.add(dumField);
		}

        FileUtils.createFile(basePath, controllerText.getText(),
            new Controller(config.getCompanyName(), projectText.getText(), moduleText.getText(),
                analyzer.getModelName(), analyzer.getMappingName(), fieldList, analyzer.isContainEnable(), containDate,
                containDecimal, analyzer.getTableComment(), adminCheckBox.isSelected() ? ADMIN : null).getHtml());
	}

//	private void generateListFile(String basePath) {
//		if (StringUtils.isNotBlank(this.listText.getText())) {
//			List<DummyField> allList = analyzer.getFieldList();
//			Set<String> excludeFieldSet = persistentMap.get(extendsBox.getSelectedItem());
//			List<DummyField> fieldList = new ArrayList<>();
//			for (DummyField dumField : allList) {
//				if (excludeFieldSet.contains(dumField.getFieldName())) {
//					continue;
//				}
//				fieldList.add(dumField);
//			}
//
//			FileUtils.createFile(
//					basePath,
//					listText.getText(),
//					new com.smart.tool.generate.List(analyzer.getTableComment(), analyzer.getModelName(), analyzer
//							.isContainEnable(), Analyzer.ENABLE_NAME, fieldList).getHtml());
//		}
//	}
//
//	private void generateEditFile(String basePath) {
//		if (StringUtils.isNotBlank(this.editText.getText())) {
//			List<DummyField> allList = analyzer.getFieldList();
//			Set<String> excludeFieldSet = persistentMap.get(extendsBox.getSelectedItem());
//			List<DummyField> fieldList = new ArrayList<>();
//			for (DummyField dumField : allList) {
//				if (excludeFieldSet.contains(dumField.getFieldName())) {
//					continue;
//				}
//				fieldList.add(dumField);
//			}
//			FileUtils
//					.createFile(basePath, editText.getText(),
//							new Edit(analyzer.getTableComment(), analyzer.getModelName(), analyzer.isContainEnable(),
//									fieldList).getHtml());
//		}
//	}

	/**
	 * 首字母大写
	 */
	public static String getUpperStr(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
	}

	/**
	 * 首字母小写
	 */
	public static String getLowerStr(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
	}
}

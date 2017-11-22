<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE hibernate-mapping PUBLIC
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN" 
    "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">

<hibernate-mapping>
	
	<class name="${model.packageName}.model.${model.objectName}VO" lazy="false"
		table="${model.tableName}" batch-size="10">
		
${model.modelFields}	</class>

</hibernate-mapping>

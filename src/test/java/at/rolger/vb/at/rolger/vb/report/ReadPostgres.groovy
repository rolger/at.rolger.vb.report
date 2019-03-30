package at.rolger.vb.at.rolger.vb.report

import java.sql.SQLException;
import java.util.List;

import groovy.lang.GString;
import groovy.sql.GroovyRowResult;
import groovy.sql.Sql

import org.codehaus.jackson.map.ObjectMapper
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat


public void readAllSpieler(def Sql sql) {
	def spieler = sql.rows("select * from SPIELER");
	
	spieler.each { s -> println s }
}

// updateWith(Sql.newInstance("jdbc:mysql://localhost:3306/volleyball", "root", "root", "com.mysql.jdbc.Driver"))

readAllSpieler(Sql.newInstance("jdbc:postgresql://ec2-54-247-99-159.eu-west-1.compute.amazonaws.com:5432/d8gujkr40h33o2", 
			"umxvddxnindwuv",	
			"0098fb424e9e6130a7414ba06801e878a176230c32c2838b3e126b3af6349df6", 
			"org.postgresql.Driver"))


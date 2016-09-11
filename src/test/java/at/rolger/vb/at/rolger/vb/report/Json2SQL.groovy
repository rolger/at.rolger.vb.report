 package at.rolger.vb.at.rolger.vb.report

import java.sql.SQLException;
import java.util.List;

import groovy.lang.GString;
import groovy.sql.GroovyRowResult;
import groovy.sql.Sql

import org.codehaus.jackson.map.ObjectMapper
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

public Spieler[] readJsonWithoutException() {
	ObjectMapper mapper = new ObjectMapper()

	try {
		return mapper.readValue(new File("C:/Users/Roland.Germ/projects/at.rolger.vb/at.rolger.vb.report/src/test/java/at/rolger/vb/at/rolger/vb/report/vbdata2016.json"), Spieler[].class)
	} catch (Exception e) {
		e.printStackTrace()
	}
}

public Integer berechneWochenTag(LocalDate aDate) {
	aDate.getDayOfWeek()
}

public String berechneSemester(LocalDate aDate) {
	def semester = ""
	if (aDate.getMonthOfYear() >=9 || aDate.getMonthOfYear() <= 1) {
		semester = "WS "
	} else {
		semester = "SS "
	}
	if (aDate.getMonthOfYear() == 1) {
		semester += (aDate.getYear()-1)
	}else {
		semester += aDate.getYear()
	}
	semester
}

public void cleanDB(def Sql sql) {
	sql.execute('delete from SPIELABEND')
	sql.execute('delete from SPIELER')
}

public void updateWith(def Sql sql) {

	readJsonWithoutException().each { Spieler aSpieler ->
		updateSpieler(sql, aSpieler)
		updateAnwesenheit(sql, aSpieler)
	}
}

void updateSpieler(def Sql sql, def Spieler aSpieler) {
	def dbStoreFormat = DateTimeFormat.forPattern("yyyy-MM-dd")
	def maxSpielerId = sql.rows('select max(id) from spieler')[0].get("max(id)")
	
	def spielerRow = sql.rows("select * from SPIELER WHERE FIRSTNAME = ${aSpieler.firstName} AND NAME = ${aSpieler.name}")
	def count = spielerRow.size()
	if (count == 0) {
		spielerRow = sql.rows("select * from SPIELER WHERE FIRSTNAME = ${aSpieler.firstName}")
		count = spielerRow.size()
	}
	println "${aSpieler.id} ${aSpieler.firstName} ${aSpieler.name} : ${count}"

	def birthdate = aSpieler.getBirthdate()?.toString(dbStoreFormat)
	if (count == 0) {
		aSpieler.id = maxSpielerId + 1
		sql.execute """insert into SPIELER (id, firstName, name, birthdate, address, telefon, geschlecht, active)
					values (${aSpieler.id}, 
							${aSpieler.firstName}, 
							${aSpieler.name}, 
							${birthdate}, 
							${aSpieler.address}, 
							${aSpieler.telefon}, 
							${aSpieler.sex},
							${aSpieler.active})
			"""
	} else if (count == 1) {
		aSpieler.id = spielerRow[0].get("id")
		sql.executeUpdate """update SPIELER set
				firstName=${aSpieler.firstName}, 
				name=${aSpieler.name}, 
				birthdate=${birthdate}, 
				address=${aSpieler.address}, 
				telefon=${aSpieler.telefon}, 
				geschlecht=${aSpieler.sex}, 
				active=${aSpieler.active}
				where id=${aSpieler.id}"""
	} else {
		println "Invalid number of ${aSpieler.id} ${aSpieler.firstName} ${aSpieler.name}"
	}
}
	

void updateAnwesenheit(def Sql sql, def Spieler aSpieler) {
	def dbStoreFormat = DateTimeFormat.forPattern("yyyy-MM-dd")
	def maxSpielabendId = sql.rows('select max(id) from spielabend')[0].get("max(id)")
	
	aSpieler.getAbende().each { dateAsString ->
		LocalDate spielAbend = DateTimeFormat.forPattern("dd.MM.yyyy").parseLocalDate(dateAsString)
		def dbFormattedDate = spielAbend.toString(dbStoreFormat);

		def abendRow = sql.rows("select * from spielabend where spieler_id = ${aSpieler.id} and tag = ${dbFormattedDate}");

		if (abendRow.isEmpty()) {
			maxSpielabendId ++
			sql.execute """insert into SPIELABEND (id, tag, spieler_id, wochen_tag, semester)
				values (${maxSpielabendId}, 
				${dbFormattedDate}, 
				${aSpieler.id}, 
				${berechneWochenTag(spielAbend)}, 
				${berechneSemester(spielAbend)})
				"""
		}
	}
}

class SQLPrinter extends Sql {

	SQLPrinter() {
		super(Sql.newInstance("jdbc:mysql://localhost:3306/volleyball", "root",	"root", "com.mysql.jdbc.Driver"))
	}

	@Override
	public void open() {
	}

	@Override
	public void close() {
		super.close
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		println sql
		true
	}

	@Override
	public boolean execute(GString sql) throws SQLException {
		println sql
		true
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		println sql
		0
	}

	@Override
	public int executeUpdate(GString gstring) throws SQLException {
		println gstring
		0
	}

	@Override
	public List<GroovyRowResult> rows(String sql) throws SQLException {
		def result = super.rows(sql)
		println result
		result
	}

	@Override
	public List<GroovyRowResult> rows(GString sql) throws SQLException {
		def result = super.rows(sql)
		println result
		result
	}
}

updateWith(Sql.newInstance("jdbc:mysql://localhost:3306/volleyball", "root", "root", "com.mysql.jdbc.Driver"))
//updateWith(new SQLPrinter())


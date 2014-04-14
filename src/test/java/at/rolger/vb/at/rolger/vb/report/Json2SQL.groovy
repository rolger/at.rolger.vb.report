 package at.rolger.vb.at.rolger.vb.report

import groovy.sql.Sql

import org.codehaus.jackson.map.ObjectMapper
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

public Spieler[] readJsonWithoutException() {
	ObjectMapper mapper = new ObjectMapper()

	try {
		return mapper.readValue(new File("vbdata.json"), Spieler[].class)
	} catch (Exception e) {
		e.printStackTrace()
	}
}

public Integer berechneWochenTag(LocalDate aDate) {
	aDate.getDayOfWeek()
}

public String berechneSemester(LocalDate aDate) {
	def semester = ""
	if (aDate.getMonthOfYear() >=9 && aDate.getMonthOfYear() <= 1) {
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


def sql = Sql.newInstance("jdbc:mysql://localhost:3306/volleyball", "root",
		"root", "com.mysql.jdbc.Driver")

sql.execute 'delete from SPIELABEND'
sql.execute 'delete from SPIELER'

def maxSpielabendId = 1
def dbStoreFormat = DateTimeFormat.forPattern("yyyy-MM-dd")

readJsonWithoutException().each { Spieler aSpieler ->

	def birthdate = aSpieler.getBirthdate()?.toString(dbStoreFormat)
	sql.execute "insert into SPIELER (id, firstName, name, birthdate, address, telefon) values (${aSpieler.id}, ${aSpieler.firstName}, ${aSpieler.name}, ${birthdate}, ${aSpieler.address}, ${aSpieler.telefon})"

	aSpieler.getAbende().each { dateAsString ->
		LocalDate spielAbend = DateTimeFormat.forPattern("dd.MM.yyyy").parseLocalDate(dateAsString)
		def dbFormattedDate = spielAbend.toString(dbStoreFormat);

		sql.execute "insert into SPIELABEND (id, tag, spieler_id, wochen_tag, semester) values (${maxSpielabendId}, ${dbFormattedDate}, ${aSpieler.id}, ${berechneWochenTag(spielAbend)}, ${berechneSemester(spielAbend)})"
		maxSpielabendId ++
	}
}

sql.eachRow("select * from SPIELER ") { println "Gromit likes ${it.firstName} ${it.name}" }

sql.close
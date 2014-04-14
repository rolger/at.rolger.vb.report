package at.rolger.vb.at.rolger.vb.report;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Spieler {
	private int id;
	private String firstName;
	private String name;
	private String address;
	private LocalDate birthdate;
	private String telefon;
	private List<String> abende;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
		this.birthdate = formatter.parseLocalDate(dateString);
	}

	@Override
	public String toString() {
		String str = String.valueOf(getId());

		str += ":\t" + getFirstName() + " " + getName() + " / "
				+ getBirthdate();
		str += "\n\t" + getAddress();
		str += "\n\t" + getTelefon();

		str += "\n\t";

		for (String training : abende) {
			str += " " + training;
		}

		return str;
	}

	public List<String> getAbende() {
		return abende;
	}

	public void setAbende(List<String> abende) {
		this.abende = abende;
	}
}

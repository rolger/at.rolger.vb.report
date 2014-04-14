package at.rolger.vb.at.rolger.vb.report;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class AppTest {

	@Test
	public void readJsonWithoutException() {
		Exception exc = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			Spieler[] alleSpieler = mapper.readValue(new File("vbdata.json"),
					Spieler[].class);

			for (Spieler spieler : alleSpieler) {
				System.out.println(spieler);
			}

		} catch (JsonGenerationException e) {
			e.printStackTrace();
			exc = e;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			exc = e;
		} catch (IOException e) {
			e.printStackTrace();
			exc = e;
		}
		Assert.assertThat(exc, CoreMatchers.nullValue());
	}

}

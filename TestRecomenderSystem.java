import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class TestRecomenderSystem {	
	
	
	@Test
	void testListFriends() throws FileNotFoundException {
		RecomenderSystem rs = new RecomenderSystem (2199, 12717, "user_friends.dat", 19524, 92834, "user_artists.dat", "artists.dat");
		List<Integer> expected = rs.listFriends(837);
		assertEquals(Arrays.asList(2006, 1975, 1801, 78, 3), expected);
	}
	@Test
	void testCommonFriends() throws FileNotFoundException {
		RecomenderSystem rs = new RecomenderSystem (2199, 12717, "user_friends.dat", 19524, 92834, "user_artists.dat", "artists.dat");
		List<Integer> expected = rs.commonFriends(460, 837);
		assertEquals(Arrays.asList(3), expected);
	}
	@Test
	void testListArtists() throws FileNotFoundException {
		RecomenderSystem rs = new RecomenderSystem (2199, 12717, "user_friends.dat", 19524, 92834, "user_artists.dat", "artists.dat");
		List<String> expected = rs.listArtists(2, 4);
		assertEquals(Arrays.asList("George Michael	", "Depeche Mode	", "Moby	", "Coldplay	", "Röyksopp	", "Air	", "Duran Duran"), expected);
	}
	@Test
	void testRecomended10() throws FileNotFoundException {
		RecomenderSystem rs = new RecomenderSystem (2199, 12717, "user_friends.dat", 19524, 92834, "user_artists.dat", "artists.dat");
		List<String> expected = rs.recomended10(9);
		assertEquals(Arrays.asList("BIG BANG	"," Sonata Arctica	"," BIG BANG	"," Mustafa Sandal	"," a-ha	, Omnia	"," Lady Gaga	"," Madonna	"," Hubert Kah	"," Enigma	"), expected);
	}
	
	@Test
	void testListTop10() throws FileNotFoundException {
		RecomenderSystem rs = new RecomenderSystem (2199, 12717, "user_friends.dat", 19524, 92834, "user_artists.dat", "artists.dat");
		List<String> expected = rs.listTop10();
		assertEquals(Arrays.asList("Britney Spears	"," Lady Gaga	"," Depeche Mode	"," Christina Aguilera	"," Paramore	"," Rihanna	"," Madonna	"," The Beatles	"," Shakira	"," Muse	"), expected);
	} 
}

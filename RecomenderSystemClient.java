import java.io.FileNotFoundException;

import javax.swing.text.html.HTMLDocument.Iterator;

import edu.princeton.cs.algs4.Graph;

public class RecomenderSystemClient {
	public static void main(String[] args) throws FileNotFoundException{
		//Create the recommender system object
		RecomenderSystem rs = new RecomenderSystem (2199, 12717, "user_friends.dat", 19524, 92834, "user_artists.dat", "artists.dat");
		System.out.println(rs.listFriends(837));
		System.out.println(rs.commonFriends(460, 837));
		System.out.println(rs.listArtists(2, 4));
		System.out.println(rs.recomended10(9));
		System.out.println(rs.listTop10());
	}
}

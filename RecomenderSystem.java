import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.MinPQ;

public class RecomenderSystem {
	Graph friendsG;
	EdgeWeightedGraph artistsG;
	Hashtable<Integer, String> htArtistsNames; 
	Scanner scanner1;
	Scanner scanner2;
	Scanner scanner3;
	int friendsVertices;
	int friendsEdges; 
	int artistsVertices;
	int artistsEdges;
	//the constructor
	RecomenderSystem(int friendsVertices, int friendsEdges, String friendsFile, int artistsVertices, int artistsEdges, String artistsFile, String artistNameFile) throws FileNotFoundException{
		this.friendsVertices = friendsVertices;
		this.friendsEdges = friendsEdges;
		this.artistsVertices = artistsVertices;
		this.artistsEdges = artistsEdges;
		friendsG = new Graph(friendsVertices);
		artistsG = new EdgeWeightedGraph(artistsVertices);
		htArtistsNames = new Hashtable <Integer, String> ();
		scanner1 = new Scanner(new File(friendsFile));
		scanner2 = new Scanner(new File(artistsFile));
		scanner3 = new Scanner(new File(artistNameFile));
		createFriendsGraph(); //populate the friends graph
		createArtistsGraph(); //populate the artists graph
		populateTheArtistName(); //populate the artists name hash table
	}
	//this method will populate the artists name hash table
	private void populateTheArtistName () {
		scanner3.next();   //get rid of the table head
		scanner3.next();
		scanner3.next();
		scanner3.next();
		while (scanner3.hasNextLine()) {
			int n = scanner3.nextInt();	
			String a = scanner3.nextLine();
			Pattern pattern = Pattern.compile("\\s(.*)([\\s]?http:\\/\\/www.last.fm\\/music.*)");
			Matcher matcher = pattern.matcher(a);
			while(matcher.find()) {
				htArtistsNames.put(n, matcher.group(1));
			}
		}
	}
	//this method will populate the artists weighted graph
	private void createArtistsGraph() {
		scanner2.next();   //get rid of the table head
		scanner2.next();
		scanner2.next();
		for(int i = 0; i < artistsEdges; i++){
			int v = scanner2.nextInt();
			int w = scanner2.nextInt();
			double weight = scanner2.nextInt();
			Edge e = new Edge(v, w, weight);
			artistsG.addEdge(e);
		}		
	}
	//this method will populate the friends graph
	private void createFriendsGraph() {
		scanner1.next();		//get rid of the table head
		scanner1.next();
		for(int i = 0; i < friendsEdges; i++){
			int v = scanner1.nextInt();
			int w = scanner1.nextInt();
			friendsG.addEdge(v, w);
		}		
	}
	//this method will return the friends of a given user
	public List<Integer> listFriends (int user) {
		List<Integer> friendList = new ArrayList<Integer>();
		Iterable< Integer > itFriends = friendsG.adj(user);
		java.util.Iterator<Integer> it = itFriends.iterator();
		while(it.hasNext()) {
			friendList.add(it.next()); 
		}
		friendList = isUnique(friendList);
		return friendList;
	}
	//this method will return a list with just unique elements from a passed list
	private List<Integer> isUnique(List<Integer> list) {
		for (int k = 0; k < list.size(); k++) {
			for (int l = k+1; l < list.size(); l++) {
				if(list.get(k).equals(list.get(l))) {
					list.remove(l);
				}
			}			
		}
		return list;
	}
	//this method will return the common friends of two passed users 
	public List<Integer> commonFriends (int user1, int user2){
		List<Integer> friendList1 = new ArrayList<Integer>();
		List<Integer> friendList2 = new ArrayList<Integer>();
		friendList1 = listFriends (user1);
		friendList2 = listFriends (user2);
		return common(friendList1, friendList2);
	}
	//this method will return the list of artists listened by both users
	public List<String> listArtists(int u1, int u2){
		List<Integer> artistsList1 = new ArrayList<Integer>();
		List<Integer> artistsList2 = new ArrayList<Integer>();
		List<Integer> commonArtistsList = new ArrayList<Integer>();
		List<String> common = new ArrayList<String>();
		artistsList1 = listArtistsListened(u1);
		artistsList2 = listArtistsListened(u2);
		commonArtistsList = common(artistsList1, artistsList2);
		for(Integer a :commonArtistsList) {
			common.add(htArtistsNames.get(a));
			}
		return common;
	}
	//this method will return a list of artists listened by a user
	private List<Integer> listArtistsListened(int u){
		List<Integer> artistsListened = new ArrayList<Integer>();
		Iterable<Edge> itArtists = artistsG.adj(u);
		Iterator<Edge> itAr = itArtists.iterator();
		while(itAr.hasNext()) {
			artistsListened.add(itAr.next().other(u));
		}		
		return artistsListened;
	}
	//this method returns the lists of top 10 most popular artists listened by all users
	public List<String> listTop10(){		 
		List<Integer> top10 = new ArrayList<Integer>();
		List<String> theTop10 = new ArrayList<String>();
		Hashtable<Integer, Integer> ht = new Hashtable <Integer, Integer> (); //create the hash table
		MinPQ<Integer> mpqTop10 = new MinPQ<Integer>(10);
		for(int i = 2; i < 12; i++) {
		int weight1 = getWeightforVertex(i);
			ht.put(weight1, i); //put the first ten weights and artists in a hash table
			mpqTop10.insert(weight1);
		}
		for(int a = 12; a < artistsVertices; a++) {
			int weight2 = getWeightforVertex(a);
			if(weight2 > mpqTop10.min()) {
				ht.put(weight2, a); //put the weight and the artist in a hash table
				mpqTop10.insert(weight2);
				mpqTop10.delMin(); //remove the minimum
			}
		}
		for(int q = 0; q < 10; q++) {
			top10.add(ht.get(mpqTop10.delMin())); //add the first 10 artists to the top
		}	
		Collections.reverse(top10);
		for(Integer a :top10) {
		theTop10.add(htArtistsNames.get(a));
		}
		return theTop10;
	}

	//this method will return the recommended 10 most popular artists listen by the given user and his/her friends
	public List<String> recomended10(int user){
		List<Integer> recomended10 = new ArrayList<Integer>();
		List<Integer> friends = new ArrayList<Integer>();
		List<String> recomended = new ArrayList<String>();
		Hashtable<Integer, Integer> htListenedByF = new Hashtable <Integer, Integer> (); //create the hash table to keep track of (artist, weight)
		Hashtable<Integer, Integer> htListenFtop = new Hashtable <Integer, Integer> (); //create the hash table to keep track of (weight, artist)		
		friends = listFriends(user); // get friends inside list
		friends.add(user); // add the user inside the friend list
		List<Integer> listenedByFriends = new ArrayList<Integer>();	
		//PriorityQueue<Integer> topF = new PriorityQueue<>(20000, Collections.reverseOrder()); //create the priority queue
		MaxPQ<Integer> mxpqTop10 = new MaxPQ<Integer>(10);
		for(Integer f: friends){    //for each friend
			listenedByFriends = listArtistsListened(f);   // get artists listened by friend inside a list
			for(Integer a: listenedByFriends){     //for each listened artist
				boolean b = htListenedByF.containsKey(a);		//find if the artist is in the hash
				int weight = getWeight(f, a);  //get the weight for friend artist edge
				if(b == true) {  //if the artist is in the hash table
					int w1 = htListenedByF.get(a);  //get previous weight in order to add to the new weight
					htListenedByF.remove(a);		//remove it from the hash
					htListenedByF.put(a, w1 + weight);   //update the hash table with the new weight
					mxpqTop10.insert(weight + w1);
					htListenFtop.remove(weight);	//remove it from the second hash
					htListenFtop.put(w1 + weight, a);		//update the second hash table					
				}
				else {		//if the artist is not yet in the hash table	
					htListenedByF.put(a, weight);    //put the artist and weight in a hash table	
					mxpqTop10.insert(weight);
					htListenFtop.put(weight, a); //add to the second hash table
				}	
			} 
		}
		
		for(int q = 0; q < 10; q++) {
			recomended10.add(htListenFtop.get(mxpqTop10.delMax())); //gate the first 10 artists from the top
		}
		for(Integer a :recomended10) {
			recomended.add(htArtistsNames.get(a));
			}
		return recomended;
	}
	//this method will return the weight between two vertices
	private int getWeight (int vertex1, int vertex2) { 
		int weight = 0;
		for(Edge e: artistsG.edges()) {
			if((e.either() == vertex1 && e.other(e.either()) == vertex2) || (e.other(e.either()) == vertex1 && e.either() == vertex2)) {				
				weight = (int) e.weight();
			}	
		}
		return weight;
	}
	//this method will return the total weight for a vertex 
	private int getWeightforVertex (int vertex) {
		int sum = 0;
		for(Edge e: artistsG.edges()) {
			if(e.either() == vertex || e.other(e.either()) == vertex) {				
				sum += e.weight();
			}	
		}
		return sum;
	}
	//this method will return the common elements from two passed lists
	private List<Integer> common(List<Integer> l1, List<Integer> l2){
		List<Integer> comm = new ArrayList<Integer>();
		for (int i = 0; i < l1.size(); i++) {
			for (int j = 0; j < l2.size(); j++) {
				if(l1.get(i) == l2.get(j)) {
					comm.add(l1.get(i));
				}
			}
		}
		return comm;
	}
}
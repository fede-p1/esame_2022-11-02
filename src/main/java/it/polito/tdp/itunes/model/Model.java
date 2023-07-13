package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	ItunesDAO dao;
	SimpleGraph<Track, DefaultEdge> graph;
	
	public Model() {
		dao = new ItunesDAO();
	}
	
	public List<String> getAllGenreName(){
		return dao.getAllGenreName();
	}
	
	public double getMin(String nameGenre){
		return dao.getMin(nameGenre);
	}
	public double getMax(String nameGenre){
		return dao.getMax(nameGenre);
	}
	
	public SimpleGraph<Track, DefaultEdge> creaGrafo(String nameGenre, double min, double max){
		
		graph = new SimpleGraph<>(DefaultEdge.class);
		
		List<Track> vertex = new ArrayList<>(dao.getTracks(nameGenre, min, max));
		
		Graphs.addAllVertices(graph, vertex);
		
		List<PlaylistTrack> playlists = new ArrayList<>(dao.getAllPlaylistTrack());
		
		for (Track t1 : graph.vertexSet())
			for (Track t2 : graph.vertexSet())
				if ((!t1.equals(t2)) && (!graph.containsEdge(t2, t1))) {
					int count1 = 0;
					int count2 = 0;
					for (PlaylistTrack pt : playlists) {
						if (pt.getTrackId() == t1.getTrackId())
							count1++;
						if (pt.getTrackId() == t2.getTrackId())
							count2++;
					}
					t1.setNumPlaylist(count1);
					t2.setNumPlaylist(count2);
					if (count1 == count2)
						graph.addEdge(t2, t1);
				}
		
		return graph;
		
	}
	
	public ItunesDAO getDao() {
		return dao;
	}

	public void setDao(ItunesDAO dao) {
		this.dao = dao;
	}

	public SimpleGraph<Track, DefaultEdge> getGraph() {
		return graph;
	}

	public void setGraph(SimpleGraph<Track, DefaultEdge> graph) {
		this.graph = graph;
	}
	
	private Set<Track> connessaMax;

	public List<Set<Track>> connesse() {
		ConnectivityInspector<Track,DefaultEdge> conn = new ConnectivityInspector<>(graph);
		int sizeMax = 0;
		for (Set<Track> set : conn.connectedSets()) {
			if (set.size() > sizeMax) {
				sizeMax = set.size();
			}
		}
		for (Set<Track> set : conn.connectedSets()) {
			if (set.size() == sizeMax) {
				connessaMax = new HashSet<>(set);
			}
		}
		return conn.connectedSets();		
	}
	
	private List<Track> soluzione;
	
	public List<Track> getPlaylist(double dTOT){
		
		double dtot = dTOT*60*1000;
		
		List<Track> parziale = new ArrayList<>();
		soluzione = new ArrayList<>();
		
		ricorsiva(parziale,dtot);
		
		return soluzione;
		
	}
	
	private void ricorsiva(List<Track> parziale, double dtot) {
		
		if (parziale.size() > soluzione.size())
			soluzione = new ArrayList<>(parziale);
		
		for (Track t : this.connessaMax) {
			if ((!parziale.contains(t)) && ((getDurata(parziale) + t.getMilliseconds()) <= dtot)) {
				parziale.add(t);
				ricorsiva(parziale,dtot);
				parziale.remove(parziale.size()-1);
			}
			
		}
		
		
	}
	
	private double getDurata(List<Track> parziale) {
		
		double durata = 0.0;
		
		for (Track t : parziale)
			durata +=  t.getMilliseconds();
		
		return durata;
		
	}
	
	
	
}

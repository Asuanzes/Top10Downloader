package es.tetexe.top10downloader;

import android.media.Image;

public class Application {

	private String name;
	private String artist;
	private String releaseDate;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String date) {
		this.artist = date;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String toString(){
		return "Name: " + this.name + "\n" +
	"Fecha: " +  this.artist + "\n" +
				"Estado evento: " + this.releaseDate+ "\n";
	}

}

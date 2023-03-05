package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;
   // public HashMap<Artist,List<Song>> artistlikedMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();
        //artistlikedMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User u = new User(name,mobile);
        users.add(u);
        return u;
    }

    public Artist createArtist(String name) {
        Artist a = new Artist(name);
        artists.add(a);
        return a;
    }

    public Album createAlbum(String title, String artistName) {
        Artist a = new Artist(artistName);
        if(artists.contains(a) == false)
        {
            artists.add(a);
        }
        Album alm = new Album(title);
        //alm.setReleaseDate();
        albums.add(alm);
        if(artistAlbumMap.containsKey(a))
        {
            artistAlbumMap.get(a).add(alm);
        }
        else {
            List<Album> l = new ArrayList<>();
            l.add(alm);
            artistAlbumMap.put(a,l);
        }
        return alm;

    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Album alm = new Album(albumName);
        Song s = new Song(title,length);
        if(albums.contains(alm) == false)
        {
            throw new Exception("Album does not exist");
        }
        else {

            songs.add(s);
            if(albumSongMap.containsKey(alm))
            {
                albumSongMap.get(alm).add(s);
            }
            else {
                List<Song> l = new ArrayList<>();
                l.add(s);
                albumSongMap.put(alm,l);
            }
        }

        return s;

    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        boolean flag = false;
        User us = null;
        Playlist p = new Playlist(title);
        for(User u : users)
        {
            if(users.contains(u.getMobile().equals(mobile)))
            {
                flag = true;
                us = u;
                break;
            }
        }
        if(flag == false)
        {
            throw new Exception("User does not exist");
        }
        else {
            List<Song> l = new ArrayList<>();
            for(Song s : songs)
            {
                if(s.getLength() == length)
                {
                    l.add(s);
                }
            }
            playlistSongMap.put(p,l);
            creatorPlaylistMap.put(us,p);
            List<User> lt = new ArrayList<>();
            lt.add(us);
            playlistListenerMap.put(p,lt);
            playlists.add(p);

        }


        return p;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        boolean flag = false;
        User us = null;
        Playlist p = new Playlist(title);
        for(User u : users)
        {
            if(users.contains(u.getMobile().equals(mobile)))
            {
                flag = true;
                us = u;
                break;
            }
        }
        if(flag == false)
        {
            throw new Exception("User does not exist");
        }
        else {
            List<Song> l = new ArrayList<>();
            for(String songtitle : songTitles)
            {
                for(Song s : songs)
                {
                    if(s.getTitle().equals(songtitle))
                    {
                        l.add(s);
                    }
                }
            }
            playlistSongMap.put(p,l);
            creatorPlaylistMap.put(us,p);
            List<User> lt = new ArrayList<>();
            lt.add(us);
            playlistListenerMap.put(p,lt);
            playlists.add(p);
        }

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean flag = false;
        User us = null;
        Playlist p = new Playlist(playlistTitle);
        if(playlists.contains(p) == false)
        {
            throw new Exception("Playlist does not exist");
        }
        for(User u : users)
        {
            if(users.contains(u.getMobile().equals(mobile)))
            {
                flag = true;
                us = u;
                break;
            }
        }
        if(flag == false)
        {
            throw new Exception("User does not exist");
        }

        if(creatorPlaylistMap.containsKey(us) == false && playlistListenerMap.get(p).contains(us) == false)
        {
            if(playlistListenerMap.containsKey(p))
            {
                playlistListenerMap.get(p).add(us);
            }
            else {
                List<User> l = new ArrayList<>();
                l.add(us);
                playlistListenerMap.put(p,l);
            }
        }

        return p;



    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        boolean f1 = false;
        User us = null;
        for(User u : users)
        {
            if(u.getMobile().equals(mobile))
            {
                us = u;
                f1 = true;
                break;
            }
        }
        if(f1 == false)
        {
            throw new Exception("User does not exist");
        }
        boolean f2 =false;
        Song song = null;
        for(Song s : songs)
        {
            if(s.getTitle().equals(songTitle))
            {
                song = s;
                f2 = true;
                break;
            }
        }
        if(f2 == false)
        {
            throw new Exception("Song does not exist");
        }
        Album alm = null;
        for(Album alb : albumSongMap.keySet())
        {
            if(albumSongMap.get(alb).contains(song))
            {
                alm = alb;
                break;
            }
        }
        Artist art = null;
        for(Artist a : artistAlbumMap.keySet())
        {
            if(artistAlbumMap.get(a).contains(alm))
            {
                art = a;
                break;
            }
        }

        if(songLikeMap.containsKey(song))
        {
            if(songLikeMap.get(song).contains(us) == false)
            {
                songLikeMap.get(song).add(us);

            }

        }
        else {
            List<User> l = new ArrayList<>();
            l.add(us);
            songLikeMap.put(song,l);

        }
        song.setLikes(song.getLikes()+1);
        art.setLikes(art.getLikes()+1);


    }

    public String mostPopularArtist() {
        int max = Integer.MIN_VALUE;
        StringBuilder name = new StringBuilder();
        for(Artist a : artists)
        {
            if(a.getLikes() > max)
            {
                max = a.getLikes();
                name.append(a.getName());
            }
        }

        return name.toString();

    }

    public String mostPopularSong() {
        int max = Integer.MIN_VALUE;
        StringBuilder name = new StringBuilder();
        for(Song s : songs)
        {
            if(s.getLikes() > max)
            {
                max = s.getLikes();
                name.append(s.getTitle());
            }
        }

        return name.toString();
    }
}

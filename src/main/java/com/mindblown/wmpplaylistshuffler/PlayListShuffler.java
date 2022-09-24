/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mindblown.wmpplaylistshuffler;

import com.mindblown.htmlanalyzer.HtmlElements;
import com.mindblown.htmlanalyzer.HtmlScanner;
import com.mindblown.htmlanalyzer.HtmlTag;
import com.mindblown.util.ArrayListUtil;
import com.mindblown.util.FileUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author beamj
 */
public class PlayListShuffler {
    
    //ANY CHANGES MADE TO THESE VARIABLES MUST BE UPDATED IN THE RESET-VARS FUNCTION!!
    private HtmlScanner htmlScanner;
    private HtmlElements playlistElements;
    private ArrayList<HtmlTag> playlistTags;

    public PlayListShuffler() {
        resetVars();
    }
    
    /**
     * Turns all the instance variables of this object to null. This is usually used by the object 
     * when the object wants to remove all traces of any playlist files being shuffled. This is so the 
     * transition to a new playlist file goes smoothly.
     */
    private void resetVars(){
        htmlScanner = null;
        playlistElements = null;
        playlistTags = null;
    }
    
    /**
     * Changes the order of the songs in a WPL playlist file
     * @param playlistFile the playlist file to shuffle
     */
    public void shufflePlaylistFile(File playlistFile){
        //Reset all variables
        resetVars();
        //The way the playlist gets shuffled is by getting its media tags and shuffling their order
        //in the file
        
        //Scan the playlist file and get its html elements
        htmlScanner = new HtmlScanner(playlistFile);
        playlistElements = htmlScanner.getHtmlElements();
        playlistTags = playlistElements.getTags();
        
        //Get the playlist's HtmlElements with the songs shuffled.
        HtmlElements shuffledPlaylistHE = shufflePlayList();
//        HtmlElements shuffledPlaylistHE = new HtmlElements(playlistTags);
        //Use the HtmlElements.toString() function to recreate what the tags would look like in the file.
        String shuffledPlaylistFileStr = shuffledPlaylistHE.toString();
        
        //Then find the WPL version tag at the beginning of each WPL file, and append that to the recreated file tags
        String origPlaylistFileStr = findWplVersionTag();
        shuffledPlaylistFileStr = origPlaylistFileStr + "\n" + shuffledPlaylistFileStr;
        
        System.out.println(shuffledPlaylistFileStr);
//        
//        //Write the shuffled playlist file to the playlist file
//        try(PrintWriter writer = new PrintWriter(Main.endPlaylistFile)){
//            writer.write(shuffledPlaylistFileStr);
//        } catch (FileNotFoundException ex) {
//            System.out.println(ex);
//        }
//        
        //Reset all variables
        resetVars();
    }
    
    /**
     * Finds the WPL version tag that appears at the beginning of all WPL files in an HTML string
     * @return the wpl version tag in the playlist file being shuffled
     */
    private String findWplVersionTag(){
        
        /*
        The method I'm doing this is cheat and is lazy, since it's not worth it to work really hard on this.
        All I'm doing is finding the first < and > pair in the html string and returning the < + everything in between + >
        */
        
        //Get the HTML string from the HtmlScanner that has scanned the html of the playlist file to shuffle
        String origPlaylistFileStr = htmlScanner.getHtml();
        
        //Get the indices of the markers of the begining and end of the wpl version tag
        int startInd = origPlaylistFileStr.indexOf('<');
        int endInd = origPlaylistFileStr.indexOf('>', startInd);
        //If any of them don't exist, return a blank string
        if(startInd == -1 || endInd == -1){
            return "";
        }
        
        //Return the wpl version tag that is marked by the start and end indices.
        String wplVersionTag = origPlaylistFileStr.substring(startInd, endInd + 1);
        return wplVersionTag;
    }
    
    /**
     * Shuffle the song elements in the playlist file, and return an HtmlElements containing the playlist's elements 
     * with the songs shuffled
     * @return an HtmlElements containing the playlist's elements with the songs shuffled
     */
    private HtmlElements shufflePlayList(){
        /*
        First find the song tags (tags with tag-name "media", make a list of them,
        and find the index in playlistTags that marks the beginning of the list of song tags.
        Then, shuffle the list of the song tags.
        Then, starting at the song-tags-beginning-index, replace the song tags with the elements
        in the shuffled song tags list. Essentially, I'm replacing the ordered list of song tags
        in the song-tags-list with the shuffled list of song tags one-by-one.
        */
        int songsStartInd = -1;
        ArrayList<HtmlTag> songTags = new ArrayList<>();
        for(int i = 0; i < playlistTags.size(); i++){
            HtmlTag tag = playlistTags.get(i);
            if(tag.getTagName().equals("media")){
                //If a song is found, add it to the songTags list, and if no songs have been found before,
                //update the songs-start-index
                if(songsStartInd == -1){
                    //If no songs have been found before, set the starting song index to the current index
                    songsStartInd = i;
                }
                songTags.add(tag);
            } else {
                //If the tag isn't a sound, then there are two options. If no songs have been
                //found, move on and do nothing. If a song has been found before, then the list
                //of songs has ended, so stop the iteration.
                if(songsStartInd == -1){
                    //Do nothing
                } else {
                    //Stop the iteration.
                    i = playlistTags.size();
                }
            }
        }
        if(songsStartInd == -1){
            //If there are no songs in the file given, then just return an empty HtmlElements object
            return new HtmlElements();
        }
        
        //Shuffle the song tags
        songTags = ArrayListUtil.shuffleList(songTags);
        
        //Go through the shuffled song tags and replace the song tags in the playlistTags list
        //with these
        for(int i = 0; i < songTags.size(); i++){
            //Find the location of the song tag in the playlist tag list that should be replaced
            int playlistSongInd = i + songsStartInd;
            //Replace the song tag in the playlist tag list with the shuffled song tag
            playlistTags.set(playlistSongInd, songTags.get(i));
        }
        
        //Create a new HtmlElements from the shuffled playlist tags, and then return it
        HtmlElements shuffledPlaylistHE = new HtmlElements(playlistTags);
        return shuffledPlaylistHE;
    }
}
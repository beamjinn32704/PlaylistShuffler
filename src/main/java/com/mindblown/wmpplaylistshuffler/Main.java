/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mindblown.wmpplaylistshuffler;

import com.mindblown.util.FileUtil;
import com.mindblown.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author beamj
 */
public class Main {
    
    public static File endPlaylistFile = new File("D:\\Backupps\\Muziek\\Music Storage\\Playlists\\Shuffled Playlist.wpl");
    
    public static void main(String[] args) {
        //When the program opens, open a dialog box that asks for the playlist file to change, and then shuffle it.
//        File playlistFile = FileUtil.getFile(FileUtil.filesOnly, "Choose Windows Media Player Playlist File To Shuffle",
//                new FileNameExtensionFilter("WPL Playlist Files", "wpl"), null);
        File playlistFile = new File("D:\\Backupps\\Muziek\\Music Storage\\Playlists\\Good Playlist.wpl");
        
        if(playlistFile == null){
            //If no file is chosen, exit the program.
            return;
        }
        
        /*
        CHANGES MADE FOR DEBUGGING
        1. MADE A END-PLAYLIST-FILE IN MAIN AND SET THE PLAYLIST-SHUFFLER FUNCTION TO END WITH THAT FILE
        2. MADE THE SONGS NOT SHUFFLED
        3. STOPPED SORTING THE TRIBS (Attribs:getAttribs() Line 51) and (HtmlTagData:constructor() Line 32)
        
        DISCOVERIES MADE:
        1. SHUFFLING THE SONGS ISN'T THE PROBLEM.
        2. TWO PROBLEMS FOUND WERE THESE: THE META TAGS AT THE BEGINNING OF THE WPL FILES HAVE TO HAVE A '/' BEFORE THE '>'
        IN EACH OF THE TAGS. FIX THIS IN HTML TAG. THE SECOND PROBLEM IS THAT THE TITLE TAG DOESN'T HAVE THE ACTUAL TITLE INSIDE
        THE TITLE TAG. IT'S JUST BLANK SPACE. FIX THIS IN THE HTML SCANNER (IN THE CLEAN FUNCTION MOST LIKELY).
        3. THE LAST AND FINAL PROBLEM IS THAT THE MEDIA TAGS ALSO NEED TO HAVE '/' 's BEFORE THE '>'
        
        CURRENT PROGRESS:
        I HAVE TECHINCALLY FIXED EVERYTHING, BUT I STILL NEED TO ADD THE TITLE TO IN BETWEEN THE TITLE TAGS. THE TITLE
        IS PUT INTO THE /TITLE TAG'S CLEANED DATA. WHEN ORGANIZING THE TAGS FROM HTMLSCANNER, MAKE SURE THE LIB
        PUTS THE TITLE STRING BACK TO THE STARTER TITLE
        */
        //Shuffle the playlist
        PlayListShuffler playlistShuffler = new PlayListShuffler();
        playlistShuffler.shufflePlaylistFile(playlistFile);
//        Util.open(endPlaylistFile);
    }
}

// -*- Java -*-
/*
 * <copyright>
 *
 *  Copyright (c) 2002
 *  Institute for Information Processing and Computer Supported New Media (IICM),
 *  Graz University of Technology, Austria.
 *
 * </copyright>
 *
 * <file>
 *
 *  Name:    LineStorage.java
 *
 *  Purpose: LineStorage holds all input lines and provides a public interface to manipulate the lines.
 *
 *  Created: 19 Sep 2002
 *
 *  $Id$
 *
 *  Description:
 *
 * </file>
*/

package kwic.oo;

/*
 * $Log$
*/

import java.util.ArrayList;

/**
 *  An object of the LineStorage class holds a number of lines and provides a number of public methods
 *  to manipulate the lines. A line is defined as a set of words, and a word consists of a number of
 *  characters. Methods defined by the LineStorage class allow objects of other classes to:
 *  <ul>
 *  <li>set, read and delete a character from a particular word in a particular line
 *  <li>add a new character to a particular word in a particular line
 *  <li>obtain the number of characters in a particular word in a particular line
 *  <li>set, read and delete a word from a particular line
 *  <li>add a new word to a particular line
 *  <li>add an empty word to a particular line
 *  <li>obtain words count in a particular line
 *  <li>set, read and delete a particular line
 *  <li>add a new line
 *  <li>add an empty line
 *  <li>obtain lines count
 *  </ul>
 *  @author  dhelic
 *  @version $Id$
*/

public class Line{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */
//----------------------------------------------------------------------

/**
 * ArrayList holding all lines. Each line itself is represeneted as an
 * Arraylist object holding all words from that line. The ArrayList class is a
 * standard Java Collection class, which  implements the typical buffer
 * functionality, i.e., it keeps its objects in an array of a fix capacity.
 * When the current capacity is exceeded, ArrayList object resizes its array
 * automatically, and copies the elements of the old array into the new one.
 */

  private ArrayList words_ = new ArrayList();

//----------------------------------------------------------------------
/**
 * Constructors
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Methods
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * This method sets a new character on the specified index of
 * a particular word in a particular line
 * @param c new character
 * @param position character index in the word
 * @param word word index in the line
 * @param line line index
 * @return void
 * @see #getChar
 * @see #addChar
 * @see #deleteChar
 */

  public void setChar(char c, int position, int word){

        // get the specified line
    ArrayList current_line = words_;

        // get the specified word
    String current_word = (String) current_line.get(word);

        // Get character representation of the specified word
    char[] chars = current_word.toCharArray();

        // set the new character at the specified positon
    chars[position] = c;

        // make new string represenation of the word
    current_word = new String(chars);

        // replace the old word with the new one
    current_line.set(word, current_word);
  }

//----------------------------------------------------------------------
/**
 * Gets the character from the specified position in the specified word
 * in a particular line
 * @param position character index in the word
 * @param word word index in the line
 * @param line line index
 * @return char
 * @see #setChar
 * @see #addChar
 * @see #deleteChar
 */

  public char getChar(int position, int word){
    return ((String)words_.get(word)).charAt(position);
  }

//----------------------------------------------------------------------
/**
 * Adds a character at the end of the specified word in a particular line.
 * @param c new character
 * @param word word index in the line
 * @param line line index
 * @return void
 * @see #setChar
 * @see #getChar
 * @see #deleteChar
 */

  public void addChar(char c, int word){

        // get the specified line
    ArrayList current_line =(ArrayList) words_;

        // get the specified word
    String current_word = (String) current_line.get(word);

        // create a new character array with the length of
        // the old word increased by 1
    char[] chars = new char[current_word.length() + 1];

        // copy the old word into the new character array
    current_word.getChars(0, chars.length - 1, chars, 0);

        // add the new character at the end of the word
    chars[chars.length - 1] = c;

        // make new string represenation of the word
    current_word = new String(chars);

        // replace the old word with the new one
    current_line.set(word, current_word);
  }

//----------------------------------------------------------------------
/**
 * Deletes the character from the specified position in the specified word
 * in a particular line
 * @param position character index in the word
 * @param word word index in the line
 * @param line line index
 * @return void
 * @see #setChar
 * @see #getChar
 * @see #addChar
 */

  public void deleteChar(int position, int word){

        // get the specified line
    ArrayList current_line = words_;

        // get the specified word
    String current_word = (String) current_line.get(word);

        // create a new character array with the length of
        // the old word decreased by 1
    char[] chars = new char[current_word.length() - 1];

        // copy the old word into the new character array
        // skip the character that should be deleted
    current_word.getChars(0, position, chars, 0);
    current_word.getChars(position + 1, chars.length + 1, chars, position);

        // make new string represenation of the word
    current_word = new String(chars);

        // replace the old word with the new one
    current_line.set(word, current_word);
  }

//----------------------------------------------------------------------
/**
 * Gets the number of characters in this particular word.
 * @param word word index in the line
 * @param line line index
 * @return int
 */

  public int getCharCount(int word){
    return ((String)words_.get(word)).length();
  }

//----------------------------------------------------------------------
/**
 * This method sets a new word on the specified index of a particular line.
 * Character array is taken as an argument for the word.
 * @param chars new word
 * @param word word index in the line
 * @param line line index
 * @return void
 * @see #getWord
 * @see #addWord
 * @see #addEmptyWord
 * @see #deleteWord
 */

  public void setWord(char[] chars, int word){
    setWord(new String(chars), word);
  }

//----------------------------------------------------------------------
/**
 * This method sets a new word on the specified index of a particular line.
 * String is taken as an argument for the word.
 * @param chars new word
 * @param word word index in the line
 * @param line line index
 * @return void
 * @see #getWord
 * @see #addWord
 * @see #addEmptyWord
 * @see #deleteWord
 */

  public void setWord(String chars, int word){

        // get the specified line
    ArrayList current_line = words_;

        // replace the old word with the new one
    current_line.set(word, chars);
  }

//----------------------------------------------------------------------
/**
 * Gets the word from the specified position in a particular line
 * String representing the word is returned.
 * @param word word index in the line
 * @param line line index
 * @return String
 * @see #setWord
 * @see #addWord
 * @see #addEmptyWord
 * @see #deleteWord
 */

  public String getWord(int word){
    return (String) words_.get(word);
  }

//----------------------------------------------------------------------
/**
 * Adds a word at the end of the specified line.
 * The method takes a character array as an argument.
 * @param chars new word
 * @param line line index
 * @return void
 * @see #addEmptyWord
 * @see #setWord
 * @see #getWord
 * @see #deleteWord
 */

  public void addWord(char[] chars){
    addWord(new String(chars));
  }

//----------------------------------------------------------------------
/**
 * Adds a word at the end of the specified line.
 * The method takes a string as an argument.
 * @param chars new word
 * @param line line index
 * @return void
 * @see #addEmptyWord
 * @see #setWord
 * @see #getWord
 * @see #deleteWord
 */

  public void addWord(String chars){

        // get the specified line
    ArrayList current_line = words_;

        // add the new word
    current_line.add(chars);
  }

//----------------------------------------------------------------------
/**
 * Adds an empty word at the end of the specified line.
 * @param line line index
 * @return void
 * @see #setWord
 * @see #getWord
 * @see #addWord
 * @see #deleteWord
 */

  public void addEmptyWord(){

        // get the specified line
    ArrayList current_line = words_;

        // add the new word
    current_line.add(new String());
  }

//----------------------------------------------------------------------
/**
 * Deletes the word from the specified position in a particular line
 * @param word word index in the line
 * @param line line index
 * @return void
 * @see #setWord
 * @see #getWord
 * @see #addWord
 * @see #addEmptyWord
 */

  public void deleteWord(int word){

        // get the specified line
    ArrayList current_line = words_;

        // delete the specified word
    current_line.remove(word);
  }

//----------------------------------------------------------------------
/**
 * Gets the number of words in this particular line
 * @param line line index
 * @return int
 */

  public int getWordCount(){
    return words_.size();
  }


//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}

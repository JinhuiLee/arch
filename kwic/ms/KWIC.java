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
 *  Name:    KWIC.java
 *
 *  Purpose: KWIC system for Software Architecture constructional example
 *
 *  Created: 11 Sep 2002
 *
 *  $Id$
 *
 *  Description:
 *    The basic KWIC system is defined as follows. The KWIC system accepts an ordered
 *  set of lines, each line is an ordered set of words, and each word is an ordered set
 *  of characters. Any line may be "circularly shifted" by repeadetly removing the first
 *  word and appending it at the end of the line. The KWIC index system outputs a
 *  listing of all circular shifts of all lines in alphabetical order.
 * </file>
*/

package kwic.ms;

/*
 * $Log$
*/
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 *  The KWIC class implements the first architectural solution for the KWIC
 *  system proposed by Parnas in 1972. This solution is based on functional
 *  decomposition of the system. Thus, the system is decomposed into a number of
 *  modules, each module being a function. All modules share access to data. We call
 *  the shared data in this case "core storage". Thus, the architectural style
 *  utilized by this solution is main/subroutine architectural style. We have the
 *  following modules (functions):
 *  <ul>
 *  <li>Master Control (main). This function controls the sequencing among the
 *  other four functions.
 *  <li>Input. This function reads the data lines from the input medium (file) and
 *  stores them in core for processing by the remaining modules. The chracters are
 *  stored into a character array (char[]). The blank space is used to separate
 *  the words. An index is kept to show the starting address of each line in the
 *  character array. The index is stored as an integer array (int[]).
 *  <li>Circular Shift. This function is called after the input function has
 *  completed its work. It prepares an index, which gives the address of the first
 *  character of each circular shift, and the original index of the line in the
 *  index array made up by input function. It leaves its output in core with an array
 *  of pairs (original line number, starting addres). This array is stored as two
 *  dimensional integer array (int[][]).
 *  <li>Alphabetizing. This function takes as input the arrays produced by input
 *  and circular shift function. It produces an array in the same format (int[][])
 *  as that produced by circular shift function. In this case, however, the circular
 *  shifts are listed in another order (alphabetically).
 *  <li>Output. This function uses the arrays produced by input and alphabetizing
 *  function. It produces a nicely formated output listing of all circular shifts.
 *  </ul>
 *  @author  dhelic
 *  @version $Id$
*/

public class KWIC{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */
//----------------------------------------------------------------------

/**
 * The part of core for storing all characters
 *
 */

  private char[] chars_;

/**
 * Index that stores line starts as indexes of the original character
 * array
 *
 */

  private int[] line_index_;

/**
 * Two dimensional array that stores the original line number and
 * all of its circular shifts, stored as indexes of the original character
 * array
 *
 */

  private int[][] circular_shifts_;
  private char[] shift_chars;
  private int[] shift_index;

/**
 * Two dimensional array that stores alphabetized  circular shifts
 *
 */

  private int[][] alphabetized_;

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
 * input function reads the raw data from the specified file and stores it in core.
 * If some system I/O error occurs the program exits with an error message.
 * The format of raw data is as follows. Lines are separated by the line separator
 * character(s) (on Unix '\n', on Windows '\r\n'). Each line consists of a number of
 * words. Words are delimited by any number and combination of the space chracter (' ')
 * and the horizontal tabulation chracter ('\t'). The entered data is parsed in the
 * following way. All line separators are removed from the data, all horizontal tabulation
 * word delimiters are replaced by a single space character, and all multiple word
 * delimiters are replaced by a single space character. Then the parsed data is represented
 * in core as two arrays. The first array is a char array (char[] chars_), which keeps
 * all words seprated by a single space character. Since we removed line separators
 * from the data the second integer array (int[] line_index_) keeps indexes of the chars_
 * array where lines start.
 *
 * @param name file name
 * @return void
 */

  public void input(String file){

        // initialize line index
    line_index_ = new int[32];

        // initialize chars array
    chars_ = new char[2048];

        // count of valid characters in the buffer
    int char_count = 0;

        // count of parsed lines
    int line_count = 0;

        // the last read character
    int c;

        // new line flag
    boolean is_new_line = true;

        // new word flag
    boolean is_new_word = false;

        // line started flag
    boolean is_line_started = false;

    try{

          // open the file for reading
      InputStream in = new FileInputStream(file);

          // read characters until EOF is reached
      c = in.read();
      while(c != -1){

            // parse the character
        switch((byte) c){
        case '\n':
          is_new_line = true;
          break;
        case ' ':
          is_new_word = true;
          break;
        case '\t':
          is_new_word = true;
          break;
        case '\r':
          break;
        default:

              // if this is a new line we need to update the line index
          if(is_new_line){

                // if the line index array is full, we make a new index array.
                // the length of the new index array is the length of the old
                // index array + 32
                // at the end we copy the old index array into the new one and work
                // further with the new one
            if(line_count == line_index_.length){
              int[] new_index = new int[line_count + 32];
              System.arraycopy(line_index_, 0, new_index, 0, line_count);
              line_index_ = new_index;
            }

                // we assign the index in the original char array as the
                // start of the new line and increment the line counter
            line_index_[line_count] = char_count;
            line_count++;

                // we handled the new line, so we set the new line flag to false
            is_new_line = false;

                // we set line started flag
            is_line_started = false;
          }

              // if this is a new word we need to insert the word delimiter before
              // the new word
          if(is_new_word){

                //if the line has been started already add word delimiter,
                // otherwise we don't want to add word delimiter in front of the first
                // word
            if(is_line_started){

                  // if the chars array is full, we make a new chars array.
                  // the length of the new chars array is the length of the old
                  // index array + 2048
                  // at the end we copy the old index array into the new one and work
                  // further with the new one
              if(char_count == chars_.length){
                char[] new_chars = new char[char_count + 2048];
                System.arraycopy(chars_, 0, new_chars, 0, char_count);
                chars_ = new_chars;
              }

                  // we add the word delimiter in the chars array
              chars_[char_count] = ' ';
              char_count++;
            }

                // we handled the new word, so we set the new word flag to false
            is_new_word = false;
          }

              // now we want to add the chracter to the chars array

              // if the chars array is full, we make a new chars array.
              // the length of the new chars array is the length of the old
              // index array + 2048
              // at the end we copy the old index array into the new one and work
              // further with the new one
          if(char_count == chars_.length){
            char[] new_chars = new char[char_count + 2048];
            System.arraycopy(chars_, 0, new_chars, 0, char_count);
            chars_ = new_chars;
          }

              // add the character
          chars_[char_count] = (char) c;
          char_count++;

              // since we added at least one character we already
              // started the new line
          is_line_started = true;

          break;
        }

            // read the next character
        c = in.read();
      }

          // set the size of the index array to the real number of lines
      if(line_count != line_index_.length){
        int[] new_index = new int[line_count];
        System.arraycopy(line_index_, 0, new_index, 0, line_count);
        line_index_ = new_index;
      }

          // set the size of the chars array to the real number of characters
      if(char_count != chars_.length){
        char[] new_chars = new char[char_count];
        System.arraycopy(chars_, 0, new_chars, 0, char_count);
        chars_ = new_chars;
      }

    }catch(FileNotFoundException exc){

          // handle the exception if the file could not be found
      exc.printStackTrace();
      System.err.println("KWIC Error: Could not open " + file + "file.");
      System.exit(1);

    }catch(IOException exc){

          // handle other system I/O exception
      exc.printStackTrace();
      System.err.println("KWIC Error: Could not read " + file + "file.");
      System.exit(1);

    }
  }

//----------------------------------------------------------------------
/**
 * circularShift function processes the two arrays prepared by input function.
 * It produces all circular shifts of all lines stored in core. A circular
 * shift is a line where the first word is removed from the begin of a line
 * and appended on the end of the line. To obtain all circular shifts of a line
 * we repeat this process until we can't obtain any new lines. We represent a
 * circular shift of a line as a pair of indices. The first part of the pair
 * is the index in the chars_ array, where the first word of the shifted line
 * starts. To be able to determine the boundaries of the original line the second
 * part of the pair keeps the index of the original line in the line index array.
 * Thus, all circular shifts are represented as an array of such pairs, i.e., as
 * two dimensional integer array int[][] circular_shifts_.
 * @return void
 */

  public void circularShift(){

        // initialize the ciruclar shift matrix
        // initial size = [2, 256], we had 32 as the initial line number,
        // number of circular shifts per line is equal to words count,
        // we assume 4 words per line
        // in the first row of circular shift matrix we store original
        // line numbers, the second row stores the starting indices in chars array
        // of each particular circular shift
    circular_shifts_ = new int[2][256];

        // count of circular shifts
    int shift_count = 0;

        // iterate through lines and make circular shifts
    for(int i = 0; i < line_index_.length; i++){

          // end index of the i-th line
      int line_end = 0;

          // if i-th line is the last line then line end index is
          // the index of the last character
      if(i == (line_index_.length - 1))
        line_end = chars_.length;

          // otherwise line end index is starting index of the
          // next line
      else
        line_end = line_index_[i + 1];

          // iterate through characters of i-th line
      for(int j = line_index_[i]; j < line_end; j++){

            // if there is the word delimiter or this is the start
            // of the line (original line is the first circular shift)
            // then the next character
            // is the first character of the next circular shift
        if((chars_[j] == ' ') || (j == line_index_[i])){

              // if the shift matrix is full, we make a new shift matrix.
              // the number of columns of the new matrix is the columns' count of the old
              // matrix + 256
              // at the end we copy the old matrix into the new one and work
              // further with the new one

          if(shift_count == circular_shifts_[0].length){

                // copy the line number row
            int[] tmp = new int[shift_count + 256];
            System.arraycopy(circular_shifts_[0], 0, tmp, 0, shift_count);
            circular_shifts_[0] = tmp;

                // copy the indices row
            tmp = new int[shift_count + 256];
            System.arraycopy(circular_shifts_[1], 0, tmp, 0, shift_count);
            circular_shifts_[1] = tmp;
          }

              // set the original line number
          circular_shifts_[0][shift_count] = i;
              // set the starting index of this circular shift
          circular_shifts_[1][shift_count] = (j == line_index_[i]) ? j : j + 1;

              // increment the shift count
          shift_count++;
        }

      }
    }

        // set the columns size of shift matrix to the real number of shifts
    if(shift_count != circular_shifts_[0].length){

          // copy the line number row
      int[] tmp = new int[shift_count];
      System.arraycopy(circular_shifts_[0], 0, tmp, 0, shift_count);
      circular_shifts_[0] = tmp;

          // copy the indices row
      tmp = new int[shift_count];
      System.arraycopy(circular_shifts_[1], 0, tmp, 0, shift_count);
      circular_shifts_[1] = tmp;
    }

    shift_chars = new char[1024*10];
    shift_index = new int[1024*10];
    int pos=0;
    int index_pos=0;
    for(int i = 0; i < circular_shifts_[0].length; i++){
      int line_number = circular_shifts_[0][i];
      int shift_start = circular_shifts_[1][i];
      int line_start = line_index_[line_number];
      int line_end = 0;
      shift_index[index_pos++]=pos;
      if(line_number == (line_index_.length - 1))
        line_end = chars_.length;
      else
        line_end = line_index_[line_number + 1];
      if(line_start != shift_start){
        for(int j = shift_start; j < line_end; j++)
          shift_chars[pos++]=chars_[j];
        shift_chars[pos++]=' ';
        for(int j = line_start; j < (shift_start - 1); j++)
          shift_chars[pos++]=chars_[j];
      }else
        for(int j = line_start; j < line_end; j++)
          shift_chars[pos++]=chars_[j];
    }


    if (circular_shifts_[0].length!=shift_index.length)
    {
      int[] tmp = new int[circular_shifts_[0].length];
      System.arraycopy(shift_index, 0, tmp, 0, shift_count);
      shift_index = tmp;

    }
    //Test Demo
    //System.out.print(shift_index.length);
    // for(int i=0;i<shift_index.length;i++)
    // {
    //   System.out.print(shift_index[i]);
    //   if (i<shift_index.length-1)
    //   for (int j=shift_index[i];j<shift_index[i+1];j++)
    //     System.out.print(shift_chars[j]);
    //   System.out.println();
    // }




  }

//----------------------------------------------------------------------
/**
 * alphabetizing functions sorts lines alphabetically. The output of this
 * function is represented in the same way as the output of circularShift
 * function, i.e., as an array of pairs. Now, pairs are ordered differently,
 * i.e., in an alphabetical order. The result is stored in two dimensional
 * integer array int[][] alphabetized_.
 * @return void
 */

  public void alphabetizing(){


    List<String> strs = new ArrayList<String>();
    for(int i=0;i<shift_index.length;i++)
    {
      if (i<shift_index.length-1)
      {
        String tmp=new String(shift_chars,shift_index[i],shift_index[i+1]-shift_index[i]);
        strs.add(tmp);
      }
    }
    Collections.sort(strs);
    int pos=0;
    int index_pos=0;
    for(String str : strs)
    {
        shift_index[index_pos++]=pos;
        for (int i=0;i<str.length();i++)
        {
          shift_chars[pos++]=str.charAt(i);
        }
    }
  }

//----------------------------------------------------------------------
/**
 * output function is responsible for printing the alphabetized lines in
 * a nice format. Thus, each line is printed on the standard output in
 * a single line.
 * @return void
 */

  public void output(){
    for(int i=0;i<shift_index.length;i++)
    {
      if (i<shift_index.length-1)
      for (int j=shift_index[i];j<shift_index[i+1];j++)
      {
        if (shift_chars[shift_index[i]]>='0' && shift_chars[shift_index[i]]<='9')
          continue;
        System.out.print(shift_chars[j]);

      }
      System.out.println();
    }
  }

//----------------------------------------------------------------------
/**
 * main function controls all other functions in the system. It implements
 * the sequence of calls to other functions to obtain the desired functionality
 * of the system. Before any other function is called, main function checks the
 * command line arguments. The program expects exactly one command line argument
 * specifying the name of the file that contains the data. If the program have
 * not been started with proper command line arguments, main function exits
 * with an error message. Otherwise, input function is called first to read the
 * data from the file. When input function has finished circularShift and alphabetizing
 * functions are called in that order. circularShift function makes all circular
 * shifts of all lines that were entered. alphabetizing function sorts all circular
 * shifts alphabetically. Finally, output function prints the results in a nice
 * format.
 * @param args command line argumnets
 * @return void
 */

  public static void main(String[] args){
    KWIC kwic = new KWIC();
    if(args.length != 1){
      System.err.println("KWIC Usage: java kwic.ms.KWIC file_name");
      System.exit(1);
    }
    kwic.input(args[0]);
    kwic.circularShift();
    kwic.alphabetizing();
    kwic.output();
  }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}

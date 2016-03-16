package kwic.es;

/*
 * $Log$
*/

import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
/**
 *  CircularShifter class implemets the "Observer" part of the standard
 *  "Observable"-"Observer" mechanism. Thus, an instance of CircularShifter
 *  class declares its interest in tracking changes in an object of LineStorage
 *  class, which holds the original lines read from a KWIC input file. Therefore,
 *  any event produced and sent by the LineStorageWrapper object whenever its internal
 *  state is changed (i.e., whenever a new line has been added) is catched by
 *  CircularShiter object. In turn, this leads to production of circular shifts for
 *  the newly added line. Circular shifts are kept within an CircularShifter object again
 *  in the form of a LineStorageWrapper object.
 *  @author  dhelic
 *  @version $Id$
*/

public class WordsIndex implements Observer{


//----------------------------------------------------------------------
/**
 * Methods
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 */
  HashMap<String , Integer> map =new  HashMap<String,Integer>();

  public void update(Observable observable, Object arg){

        // cast to the line storage object
    LineStorageWrapper lines = (LineStorageWrapper) observable;

        // cast to the event object
    LineStorageChangeEvent event = (LineStorageChangeEvent) arg;

        // take actions depending on the type of the change
    switch(event.getType()){

          // if this is an ADD change make all circular shifts
          // of the new line and add them to shifts storage
    case LineStorageChangeEvent.ADD:

      String[] line = lines.getLine(lines.getLineCount() - 1);

        // iterate through all words of the line
        // and make all shifts
        for(int i = 0; i < line.length; i++){

            String str=line[i];
            if (map.containsKey(str))
            {
              int count=map.get(str).intValue();
              map.put(str,count+1);
            }
            else
            {
              map.put(str,1);
            }
        }



      break;
    case LineStorageChangeEvent.DELETE:
      String deletedStr = event.getArg();
      StringTokenizer tokenizer = new StringTokenizer(deletedStr);
          // if this is not an empty line keep the words so that we can add the line
          // to the line storage
          if(tokenizer.countTokens() > 0){

                // keep the words
            ArrayList words = new ArrayList();
            while(tokenizer.hasMoreTokens())
            {
              String str=tokenizer.nextToken();
              if (map.containsKey(str))
              {
                int count=map.get(str).intValue();
                map.put(str,count-1);
                if (count==1) map.remove(str);
              }
              else
              {
                System.out.println("error");
              }
            }
          }

      break;
    default:
      break;
    }
  }

  public void print()
  {
    Iterator it = map.keySet().iterator();
        while(it.hasNext()) {
            String key = (String)it.next();
            System.out.print(key+": ");
            System.out.println(map.get(key));
        }
  }
//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}

package pl.op.danex11.alarmscalculator840c;

import android.util.Log;

import java.util.Scanner;

class CalculateAlarm {
    String alarm;
    String dbaddress;
 
     
 public CalculateAlarm(String alarmNo){
     this.alarm = alarmNo;
     Log.i("alarmNo", alarmNo);
    dbaddress = "nn";
    Log.i("dbaddress",dbaddress);
    String chars12 = alarmNo.substring(0, 2);
     Log.i("",chars12);
    String chars34s = alarmNo.substring(2, 4);
     Log.i("chars34s",chars34s);
     int digs34 = Integer.valueOf(chars34s);
    String chars56s =  alarmNo.substring(4, 6);
    int digs56 = Integer.valueOf(chars56s);


     if (new String(chars12).equals("70"))
     {
         dbaddress = group70(digs34, digs56);
     }
     else if (new String(chars12).equals("60"))
     {
         dbaddress = group60(digs34, digs56);

     }
    else if (new String(chars12).equals("50") || (chars12).equals("51")|| (chars12).equals("52")|| (chars12).equals("53")|| (chars12).equals("54")|| (chars12).equals("55"))
    {
        dbaddress=group50(digs34,digs56);
    }   
    else { dbaddress="not found";}
 }




    public String getDbAddress(){
     return dbaddress;
    }
 
    public String group70(int digs34loc, int digs56loc)
    {
        int byte70 = 180 + (8 * digs34loc);
        byte70 += digs56loc/8;        //truncate (to int) (i.e. drop everything after the decimal dot)
        int bit70 = digs56loc%8;
        String bb = String.format("DBX%3d.%2d", byte70, bit70);
        return bb;
    }

    public String group60(int digs34loc, int digs56loc)
    {
        int byte70 = 142 + (2 * digs34loc);
        byte70 += digs56loc/8;        //truncate (to int) (i.e. drop everything after the decimal dot)
        int bit70 = digs56loc%8;
        String bb = String.format("DBX%3d.%2d", byte70, bit70);
        return bb;
    }

    public String group50(int digs34, int digs56)
    {
        //group 50
        int byte50 = 0;
        int bit50;
        boolean wrongdata = false;
        String dig34string = Integer.toString(digs34);
        Log.w("logtag", "dig34string: " + dig34string);
        //revert zero that dissapeard on conversion to Int for numbers like "02"
        if(dig34string.length() == 1){dig34string = "0" + dig34string;}
        Log.w("logtag", "dig34stringappended: " + dig34string);
        String dig3 = dig34string.substring(0,1);
        Log.w("logtag","dig3: " + dig3);
        String dig4 = dig34string.substring(1,2);
        Log.w("logtag","dig4: " + dig4);

        //String dig56 = Integer.toString(digs56).substring(4, 6);


        if (dig3.equals("0")){
            if (digs56 > 30) {
                wrongdata = true;
            }
            Log.w("logtag","0dig4: " + dig4);
            if (dig4.equals("0")){byte50=0;}
            if (dig4.equals("1")){byte50=2;}
            if (dig4.equals("2")){byte50=6;}
            if (dig4.equals("3")){byte50=10;}
        }

        if (dig3.equals("1")){
            if (digs56 > 15) {
                wrongdata = true;
            }
            Log.w("logtag","1dig4:" + dig4);
            if (dig4.equals("0")){byte50=12;}
            if (dig4.equals("1")){byte50=14;}
            if (dig4.equals("2")){byte50=16;}

        }

        int bytebydigs56 = (digs56/8);
        byte50 +=bytebydigs56;

        bit50 = digs56%8;

        String bb = String.format("DBX%3d.%2d", byte50, bit50);

        return bb;





    }
}

public class CalculatorDemo{

    public static void main(String[] args){
        System.out.println("Write six digit alarm no.: ");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        CalculateAlarm calculateInstance = new CalculateAlarm(input);
        System.out.println("For alarm " + input + " adress in DB2 is " + calculateInstance.getDbAddress());
        in.close();
  
    }
}


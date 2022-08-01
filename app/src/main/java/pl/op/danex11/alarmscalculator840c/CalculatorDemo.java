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
    else if (new String(chars12).equals("50"))
    {
        dbaddress="TODO group 50";
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

    public void group50(int digs34, int digs567)
    {

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


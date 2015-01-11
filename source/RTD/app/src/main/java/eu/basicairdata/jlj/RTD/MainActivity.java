package eu.basicairdata.jlj.RTD;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.app.PendingIntent.getActivity;
import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

//(C) BasicAirData www.basicairdata.eu JLJ
//Conversion between two systems
public class MainActivity extends ActionBarActivity {
    public void runcalc(View view) {
        EditText mEdit,mEdit1,mEdit2;
        TextView tView,tView1,tView2,tView3,tView4;
        double Rm;//Resistance measurement Ohm
        double R0;//Resistance measurement at 0°C
        double t1;//Temperature °C
        double convt;//Convergence tollerance
        double A,B,C,t0,errtemp,i,f,fdot,t;
        try {
            mEdit = (EditText) findViewById(R.id.editText);  //Resistance of Sensor
            mEdit1 = (EditText) findViewById(R.id.editText1);//Resistance of sensor at 0°C
            mEdit2 = (EditText) findViewById(R.id.editText2);//Maximum conversion error °C
            tView = (TextView) findViewById(R.id.textView5);//Temperature °C
            Rm = Double.parseDouble(mEdit.getText().toString()); //Resistance of sensor at operating temperature
            R0 = Double.parseDouble(mEdit1.getText().toString());
            convt=Double.parseDouble(mEdit2.getText().toString());
     //       Log.d("convt",Double.toString(convt));
            A=3.9083e-3;  //Callendar-Van Dusem parameters
            B=-5.775e-7;
            C=-4.183e-12;
            t0=0;//Initial guess value for Newton
            t1=0;
            errtemp=0;//Error residual
            if (Rm>R0){
                t1=(-R0*A+pow((pow(R0,2)*pow(A,2)-4*R0*B*(R0-Rm)),0.5))/(2*R0*B);
        //        Log.d(Double.toString(t1),"positive temp");
            }
            else{ //Newton method to find implicit equation solution
            i=0;
                while ((abs(errtemp)>convt)||(i==0)){  //Convergence tolerance
                t=t0;
                f=(t - 100)*C * pow(t,3)+ B * pow(t,2) +A * t  + (1- Rm/R0);
                fdot=4*C*pow(t,3)-300*C*pow(t,2)+2*B*t+A;
                t1=t0-f/fdot;
                errtemp=(t1-t0); //Error residual
     //           Log.d("errtemp",Double.toString(errtemp)); //Handy to check the convergence
                t0=t1;
                i=i+1;
                }
            }
            tView.setText(String.format("%.3f", t1 ) );//Temperature
        }
        catch (Exception e1){
            AlertDialog.Builder messagebox=new AlertDialog.Builder(this);
            messagebox.setTitle("Check Input data");
            messagebox.setMessage(e1.getMessage());
            messagebox.setNeutralButton("OK",null);
            messagebox.show();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
      //  if (id == R.id.action_settings) {
      //      return true;
      //  }
        //About message
        if (id == R.id.about) {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("About BasicAirData");
            ad.setMessage("www.basicairdata.eu");
            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            ad.show();
        }

        return super.onOptionsItemSelected(item);
    }
    //Limits on entry values

}

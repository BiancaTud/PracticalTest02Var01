package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02var01;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class PracticalTest02Var01MainActivity extends Activity {


    // Server widgets
    private EditText serverPortEditText       = null;
    private Button connectButton            = null;

    // Client widgets
    private Button       getTemp = null;
    private Button       getHumidity = null;
    private Button       getAll = null;
    private TextView weatherForecastTextView  = null;

    private ServerThread serverThread             = null;
    private ClientThread clientThread             = null;
    private String clientAddress = "127.0.0.1";
    private String clientPort = "";
    private String city = "Bucharest";
    private String informationType = "";

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            clientPort = serverPort;
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }

        }
    }
    private GetWeatherForecastButtonClickListener getWeatherForecastButtonClickListener = new GetWeatherForecastButtonClickListener();
    private class GetWeatherForecastButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }



            weatherForecastTextView.setText("");
            switch(view.getId()) {
                case R.id.humidity:
                    informationType = "humidity";
                    break;
                case R.id.temp:
                    informationType = "temp";
                    break;
                case R.id.all:
                    informationType = "all";
                    break;
            }

            clientThread = new ClientThread(
                    clientAddress,
                    Integer.parseInt(clientPort),
                    city,
                    informationType,
                    weatherForecastTextView);
            clientThread.start();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_var01_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        //cele trei butoane
        getTemp = (Button)findViewById(R.id.temp);
        getTemp.setOnClickListener(getWeatherForecastButtonClickListener);
        getHumidity = (Button)findViewById(R.id.humidity);
        getHumidity.setOnClickListener(getWeatherForecastButtonClickListener);
        getAll = (Button)findViewById(R.id.all);
        getAll.setOnClickListener(getWeatherForecastButtonClickListener);
        weatherForecastTextView = (TextView)findViewById(R.id.weather_forecast_text_view);
    }



    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practical_test02_var01_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

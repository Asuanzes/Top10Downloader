package es.tetexe.top10downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.R.xml;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button btnParse;
	ListView listApps;
	String xmlData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		btnParse = (Button) findViewById(R.id.btnParse);
		
		listApps = (ListView) findViewById(R.id.listApps);
		
		
		
		btnParse.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				ParseApplications parse = new ParseApplications(xmlData);
				boolean operationStatus = parse.procces();
				
				if (operationStatus) {
					ArrayList<Application> allDate = parse.getApplications();
					
					ArrayAdapter<Application> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, allDate);
					
					listApps.setVisibility(View.VISIBLE);
					listApps.setAdapter(adapter);
					
				}else{
					
					Log.d("MainActivity, ", "Error parsing file");
				}
			}
		});

		new DownloadData().execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=25/xml");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will


		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();


		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//Clase para descargar/leer un recurso/servicio web (xml en este caso) en segundo plano...mediante AsyncTask

	//URL | Progress | Contenido o devolución de la url
	private class DownloadData extends AsyncTask<String, Integer, String>{

		//VARIABLE QUE CONTENDRÁ LA INFORMACIÓN DEL ARCHIVO XML
		String myXmlData;

		//MÉTODO DE LA CLASE ASYNCTASK
		protected String doInBackground(String... urls){

			//CONTROL DE ERRORES
			try {
				//ALMACENAMOS EN LA VARIABLE "MYXMLDATA" EL CONTENIDO QUE NOS DEVUELVA EL MÉTODO DOWNLOADXML,
				//A ESTE MÉTODO LE PASAMOS EL PARÁMETRO "URLS" DEL MÉTODO doInBackgraound 
				//EL 0 ENTRE CORCHETES SIGNIFICA QUE ACCEDEMOS AL PRIMERO DE La lista de urls que le pasamos
				myXmlData = downloadXML(urls[0]);

			} catch (IOException e) {
				return "Unable to download XML file";  //Control de errores
			}

			return myXmlData;
		}

		protected void onPostExecute(String result){
			Log.d("OnPostExecute", myXmlData);
			xmlData = myXmlData;
			
		}

		/* MÉTODO PARA IMPLEMENTAR BARRA DE PROGRESO DE CARGA
		 * 
		 *  protected void onProgressUpdate(Integer... progress) {
		 *      setProgress(progress[0]);
		     }

		 */


		private String downloadXML(String theUrl) throws IOException {

			//Variable especifica el número de caracteres que descargará cada vez que ejecutemos el método
			int BUFFER_SIZE = 8000000;

			//InputStream, clase nativa de java
			InputStream is=null;

			//Otra variable para almacenar el contenido
			String xmlContents = "";

			try {

				URL url = new URL(theUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				int response = conn.getResponseCode();
				Log.d("DownloadXML", "The response returned is: " + response);
				is = conn.getInputStream();

				InputStreamReader isr = new InputStreamReader(is);
				int charRead;
				char[] inputBuffer = new char[BUFFER_SIZE];

				try {

					while ((charRead = isr.read(inputBuffer))>0) {
						String readString = String.copyValueOf(inputBuffer, 0, charRead);
						xmlContents += readString;
						inputBuffer = new char[BUFFER_SIZE];
					}

					return xmlContents;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

			} finally{
				if (is != null) {
					is.close();
				}
			}



		}



	}
}

package my.bigpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

public class ServerCommunicator {
	
	public static String URLGet(Context context, String action, String data)
	{
		URL url;
		try
		{
			url = new URL("http://" + context.getResources().getString(R.string.ip_address) + "/cs/game/test.php?action=" + action);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
			osw.write(data);
			osw.flush();
			osw.close();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String returnData = br.readLine();
			
			return returnData;
		} catch (MalformedURLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}

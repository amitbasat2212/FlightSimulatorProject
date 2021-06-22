package Model.AnomalyDetactor;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

// this file will be sent

// In this class we will write about time series,
// which means reading the current csv file
public class TimeSeries {

	public HashMap<String,ArrayList<String>> features;
	public ArrayList<String> fetureName;
	int NumLine=0;


	public TimeSeries(){
			features = new HashMap<>();
			fetureName =new ArrayList<>();

	}

	public TimeSeries(String csvFileName) {
		features = new HashMap<String, ArrayList<String>>();
		fetureName = new ArrayList<>();
		try {
			loadfromfile(csvFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public void loadfromfile(String csvFileName )throws IOException {
		try{
			BufferedReader scanin = null;
			String inputline = ",";
			scanin  = new BufferedReader(new FileReader(csvFileName));
			String line = null;
			String array[];

			if((line = scanin.readLine()) != null){
				NumLine++;
				array = line.split(inputline);
				fetureName.addAll(Arrays.asList(array));

				for(int i=0;i<fetureName.size();i++)
				{
					ArrayList<String> inelize = new ArrayList<>();
					if(!features.containsKey(fetureName.get(i))) {
						features.put(fetureName.get(i), inelize);
					}
				}
			}

			while((line = scanin.readLine()) != null)
			{
				array = line.split(inputline);
				for(int i=0;i<fetureName.size();i++)
				{
					features.get(fetureName.get(i)).add(array[i]);
				}


			}



		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public HashMap<String, ArrayList<String>> getFeatures() {
		return features;
	}

	public ArrayList<String> getFetureName() {
		return fetureName;
	}

	public void setFeatures(HashMap<String, ArrayList<String>> features) {
		this.features = features;
	}

	public void setFetureName(ArrayList<String> fetureName) {
		this.fetureName = fetureName;
	}

	//this function return an integer property that represent the time step
	public IntegerProperty getTimeStep(String timeStepSt, IntegerProperty timeStepRow)
	{
		IntegerProperty timeStepRe = new SimpleIntegerProperty();
		ArrayList<String> str = features.get(timeStepSt);
		timeStepRe.setValue(Integer.parseInt(str.get(timeStepRow.getValue())));
		return timeStepRe;
	}

	public ArrayList<String> getline(int i){
		return features.get(i);
	}


	public int getNumLine() {
		return NumLine;
	}
}
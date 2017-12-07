package dataStorageAccess;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import applicationLogic.Statistic;
import applicationLogic.StatisticResult;
import dataStorageAccess.controller.DiagnosisController;
import dataStorageAccess.controller.StatisticController;

public class StatisticAccess {
	
	public static void exportStatisticCompany(int companyId) throws SQLException, FileNotFoundException {
		//Get list of Diagnoses
		ArrayList<StatisticResult> diagnosesList= DiagnosisController.getDiagnosesAndDefectsOfCompany(1);
		for (StatisticResult diagnosis : diagnosesList) {
			//Get defects from Diagnosis
			diagnosis.setDefects(StatisticController.getDefectsOfDiagnosis(diagnosis.getId()));
		}
		//Wrap result of Diagnosis + Defects in own Class
		Statistic diagnoses = new Statistic(diagnosesList);
			
		XStream xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
		String timeStamp = new SimpleDateFormat("dd_MM_yyyy").format(Calendar.getInstance().getTime());
		
		FileOutputStream fs = new FileOutputStream("exportedFiles/VdS-Statistik_" + timeStamp +".xml");
		xStream.toXML(diagnoses, fs);
	}
}
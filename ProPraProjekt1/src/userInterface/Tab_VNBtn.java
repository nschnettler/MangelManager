package userInterface;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import applicationLogic.Company;
import applicationLogic.DefectStatistic;
import dataStorageAccess.controller.CompanyController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Tab_VNBtn implements Initializable {
	@FXML TableView<Company> firmenTabelle;
	@FXML TableColumn firmenSpalte;
	
	@SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources) {
		firmenSpalte.setCellValueFactory(new PropertyValueFactory<Company,String>("name"));
		try {
			ObservableList<Company> companies = FXCollections.observableArrayList(CompanyController.getCompanies());
			firmenTabelle.setItems(companies);
			firmenTabelle.setRowFactory( tv -> {
			    TableRow<Company> row = new TableRow<>();
			    row.setOnMouseClicked(event -> {
			        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
			        	Company rowData = row.getItem();
			        	onDoubleClick(rowData);
			        }
			    });
			    return row ;
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}	

		public void changeScreenDiagnosis (ActionEvent event) throws IOException{
		
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("GUI_Main.fxml"));
		Scene tableViewScene = new Scene (tableViewParent);
		
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.show();
		
	}
	
		private void onDoubleClick(Company company) {
//			FXMLLoader loader;
//				 loader = new FXMLLoader(getClass().getResource( "GUI_Diagnosis.fxml") );
////				Parent root = loader.load();
//				Tab_InspectionResult controller = (Tab_InspectionResult) loader.getController();
//				
//				controller.setSelectedCompany(company);
//				changeScreenDiagnosis(null); 

				Tab_InspectionResult.instance.setSelectedCompany(company);
	
		}
	

}

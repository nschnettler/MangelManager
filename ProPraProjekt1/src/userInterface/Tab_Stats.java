package userInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import applicationLogic.Branch;
import applicationLogic.Company;
import applicationLogic.DefectStatistic;
import applicationLogic.ExceptionDialog;
import dataStorageAccess.BranchAccess;
import dataStorageAccess.CompanyAccess;
import dataStorageAccess.DefectAccess;
import dataStorageAccess.StatisticAccess;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class Tab_Stats implements Initializable{
	
	@FXML private ListView<Company> companyList;
	@FXML private Tab companyListTab;
	@FXML private ListView<Branch> branchList;
	@FXML private Tab branchListTab;
	@FXML private TabPane statTabs;
	@FXML private TableView<DefectStatistic> statisticTableView;
	@FXML private TableColumn<DefectStatistic,String> statsDefectsColumn;
	@FXML private TableColumn<DefectStatistic,String> statsDefectDescriptionColumn;
	@FXML private TableColumn<DefectStatistic,String> statsQuantityColumn;
	@FXML private Button xmlExpBtn;
	@FXML private ProgressIndicator statCompanyProgress;
	@FXML private ProgressIndicator statBranchProgress;
	@FXML private ProgressIndicator statResultProgress;
	@FXML private Button exportButton;
  
	private Company currentCompany;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupTabListener();
		setupStatisticLists();
		prepareStatsTable();
		loadCompanyList();
	    loadBranchesList();
	    companyList.getSelectionModel().selectFirst();
    }
	
	
	private void setupTabListener() {
		statTabs.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
			            switch (t1.getId()) {
			            	case "companyListTab":
			            		companyList.getSelectionModel().select(0);
			            		break;
			            	case "branchListTab":
			            		branchList.getSelectionModel().select(0);
			            		currentCompany = null;
			            		exportButton.setVisible(false);
			            		break;
			            }
			        }
			    }
			);
	}


	private void prepareStatsTable() {
    	statsDefectsColumn.setCellValueFactory(new PropertyValueFactory<DefectStatistic,String>("Id"));
    	statsDefectDescriptionColumn.setCellValueFactory(new PropertyValueFactory<DefectStatistic,String>("Description"));
    	statsQuantityColumn.setCellValueFactory(new PropertyValueFactory<DefectStatistic,String>("numberOccurrence"));
	}
	
	

	
	/**
	 * Prepares Company and Branch Lists
	 * 		CellFactory: Content of the rows
	 * 		Listener: Get selected row
	 */
	private void setupStatisticLists() {
		companyList.setCellFactory(param -> new ListCell<Company>() {
			@Override
			protected void updateItem(Company item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				}
				else {
					setText(item.getName());
				} 
			}
		});
		companyList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Company>() {
			@Override
			public void changed(ObservableValue<? extends Company> observable, Company oldValue, Company newValue) {
				
				if (newValue.getId() == -1) {
					loadCompanyStatistic(newValue.getId(), true);
					exportButton.setVisible(false);
				}else {
					loadCompanyStatistic(newValue.getId(), false);
					currentCompany = newValue;
					exportButton.setVisible(true);
				}
			}
		});
		
		
		branchList.setCellFactory(param -> new ListCell<Branch>() {
			@Override
			protected void updateItem(Branch item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				}
				else {
					if (item.getId() != -1) {
						setText(item.getId() + " - " + item.getDescription());
					} else {
						setText(item.getDescription());
					}
					
				} 
			}
		});
		branchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Branch>() {
			@Override
			public void changed(ObservableValue<? extends Branch> observable, Branch oldValue, Branch newValue) {
				if (newValue.getId() == -1) {
					loadBranchStatistic(newValue.getId(), true);
				}else {
					loadBranchStatistic(newValue.getId(), false);
				}
			}
		});
	}

	
	/**
	 * Loads the Companies in a Background Task
	 */
	private void loadCompanyList() {
		final Task<ObservableList<Company>> companyListTask = new Task<ObservableList<Company>>() {
            @Override
            protected ObservableList<Company> call() throws Exception {
        		return FXCollections.observableArrayList(CompanyAccess.getCompanies(true));
            }
        };
        statCompanyProgress.visibleProperty().bind(companyListTask.runningProperty());
	    companyListTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				companyList.setItems(companyListTask.getValue());
				companyList.getItems().add(0, new Company(-1, "Alle Firmen"));
				companyList.getSelectionModel().select(0);
			}
		});
	    companyListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + companyListTask.getException())
	    );
	    new Thread(companyListTask).start();
	}
	
	
	/**
	 * Loads the Branches in a Background Task
	 */
	private void loadBranchesList() {
		final Task<ObservableList<Branch>> branchListTask = new Task<ObservableList<Branch>>() {
            @Override
            protected ObservableList<Branch> call() throws Exception {
        		return FXCollections.observableArrayList(BranchAccess.getBranches(true));
            }
        };
        statBranchProgress.visibleProperty().bind(branchListTask.runningProperty());
        branchListTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				branchList.setItems(branchListTask.getValue());
				branchList.getItems().add(0, new Branch(-1, "Alle Branchen"));
				//branchList.getSelectionModel().select(0);
			}
		});
        branchListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + branchListTask.getException())
	    );
	    new Thread(branchListTask).start();
	}
	
	
	/**
	 * Loads the Statistic of a company in a Background Task
	 * @param companyId - Id of the selected Company
	 */
	private void loadCompanyStatistic(int companyId, boolean loadAll) {
		// TODO Auto-generated method stub
		final Task<ObservableList<DefectStatistic>> statisticListTask = new Task<ObservableList<DefectStatistic>>() {
            @Override
            protected ObservableList<DefectStatistic> call() throws Exception {
            	if (!loadAll) {
            		return FXCollections.observableArrayList(DefectAccess.getFrequentDefectsCompany(true, companyId));
            	} else {
            		return FXCollections.observableArrayList(DefectAccess.getFrequentDefectsCompany(false, companyId));
            	}
        		
            }
        };
        statResultProgress.visibleProperty().bind(statisticListTask.runningProperty());
        statisticListTask.setOnSucceeded(event ->
	    	statisticTableView.setItems(statisticListTask.getValue())
	    );
        statisticListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + statisticListTask.getException())
	    );
	    new Thread(statisticListTask).start();
	}
	
	/**
	 * Loads the Statistic of a Branch in a Background Task
	 * @param companyId - Id of the selected Company
	 */
	private void loadBranchStatistic(int branchId, boolean loadAll) {
		// TODO Auto-generated method stub
		final Task<ObservableList<DefectStatistic>> branchStatisticListTask = new Task<ObservableList<DefectStatistic>>() {
            @Override
            protected ObservableList<DefectStatistic> call() throws Exception {
            	if (!loadAll) {
            		return FXCollections.observableArrayList(DefectAccess.getFrequentDefectsBranch(true, branchId));
            	} else {
            		return FXCollections.observableArrayList(DefectAccess.getFrequentDefectsBranch(false, branchId));
            	}
        		
            }
        };
        statResultProgress.visibleProperty().bind(branchStatisticListTask.runningProperty());
        branchStatisticListTask.setOnSucceeded(event ->
	    	statisticTableView.setItems(branchStatisticListTask.getValue())
	    );
        branchStatisticListTask.setOnFailed(event ->
	    	System.out.println("ERROR: " + branchStatisticListTask.getException())
	    );
	    new Thread(branchStatisticListTask).start();
	}
	
	 @FXML
	 private void handleExportButton(ActionEvent event) {
		 if(currentCompany != null) {
			 FileChooser fileChooser = new FileChooser();
   		  
             //Set extension filter
             FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Statistik Dateien (*.xml)", "*.xml");
             fileChooser.getExtensionFilters().add(extFilter);
             fileChooser.setInitialFileName("VdS-Statistik_" + currentCompany.getName() + "_" + new SimpleDateFormat("dd_MM_yyyy").format(Calendar.getInstance().getTime()) +".xml");

             
             //Show save file dialog
             File file = fileChooser.showSaveDialog(null);
             
             if(file != null){
            	 try {
					StatisticAccess.exportStatisticCompany(currentCompany.getId(), file.getAbsolutePath());
            		Notifications.create()
                    .title("Erfolgreich gespeichert")
                    .text("Die Statistik "+ file.getName() +" wurde erfolgreich gespeichert ")
                    .showInformation();
				} catch (FileNotFoundException | SQLException e) {
					Notifications.create()
	                 .title("Es ist ein Problem aufgetreten")
	                 .text("Die Statistik "+ file.getName() +" konnte leider nicht gespeichert werden.")
	                 .hideAfter(Duration.INDEFINITE)
	                 .onAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							new ExceptionDialog("Export Fehler", "Fehler beim Exportieren", "Beim Exportieren der Statistik ist leider ein Fehler aufgetreten.", e);
						}
	                 })
	                 .showError();
				}
            	 
            	 
             }
	     }
	 }
}
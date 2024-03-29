package dataStorageAccess.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import applicationLogic.Flaw;
import applicationLogic.FlawListElement;
import applicationLogic.Util;
import dataStorageAccess.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FlawListController {
	/**
	 * Get FlawList of an InspectionReport
	 * 
	 * @param reportId - Id of an InspectionReport
	 * @return the flawList of the InspectionReport
	 * @throws SQLException
	 */
	public static ObservableList<FlawListElement> getFlawList(int reportId) throws SQLException {
		ArrayList<FlawListElement> result = new ArrayList<FlawListElement>();
		try (
			Connection connection = DataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT * " + 
					"FROM FlawListElement NATURAL JOIN Flaw " + 
					"WHERE inspectionReportId = " + reportId + " " + 
					"ORDER BY position"
			);
		) {
			while (resultSet.next()) {
				result.add(new FlawListElement(
						resultSet.getInt("elementId"), 
						new Flaw(resultSet.getInt("externalFlawId"), resultSet.getInt("internalFlawId"), resultSet.getBoolean("isCustomFlaw"), resultSet.getString("flawDescription"), resultSet.getBoolean("dontShowAsSuggestion")), 
						resultSet.getInt("branchId"), 
						resultSet.getInt("danger"), 
						resultSet.getString("building"), 
						resultSet.getString("room"), 
						resultSet.getString("machine"),
						resultSet.getInt("position")));
			}
		}
		return FXCollections.observableArrayList(result);
	}
	
	/**
	 * Remove complete FlawList of an InspectionReport
	 * 
	 * @param reportId - The Id of the inspectionReport
	 * @throws SQLException
	 */
	public static void removeFlawList(int reportId) throws SQLException {
		Statement statement = null;
		Connection connection = null;
		String sql = 
				"DELETE " + 
				"FROM FlawListElement " + 
				"WHERE inspectionReportId = " + reportId;
		try {
			connection = DataSource.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	
	/**
	 * Insert a FlawList into the Database
	 * 
	 * @param flawListElements - A List of Flaws
	 * @param inspectionReportId - the Id of the InspectionReport
	 * @throws SQLException
	 */
	public static void insertFlawList(ObservableList<FlawListElement> flawListElements, int inspectionReportId) throws SQLException {
		String statement = "INSERT INTO FlawListElement "
				+ "(inspectionReportId, internalFlawId, branchId, danger, building, room, machine, position) "
				+ "VALUES(?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DataSource.getConnection();
			preparedStatement = connection.prepareStatement(statement);

			for(FlawListElement flawListElement : flawListElements) {
				Util.setValues(
						preparedStatement, 
						inspectionReportId, 
						flawListElement.getFlaw().getInternalId(), 
						flawListElement.getBranchId(), 
						flawListElement.getDanger(), 
						flawListElement.getBuilding(), 
						flawListElement.getRoom(),
						flawListElement.getMachine(),
						flawListElement.getPosition()
						);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
}

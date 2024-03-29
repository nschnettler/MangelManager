package applicationLogic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Short preview of an Result, used for main resultList
 * 
 * @author Niklas Schnettler
 *
 */
public class InspectionResultPreview extends InspectionReportMinimal{
	
	@XStreamOmitField
	private int companyId;
	@XStreamOmitField
	private String companyName;
	@XStreamOmitField
	private LocalDate lastEdited;
	@XStreamOmitField
	private boolean isValid;
	
	public InspectionResultPreview(int id, LocalDate date, int companyId, String companyName, LocalDate lastEdited, boolean isValid) {
		super(id, date);
		this.companyId = companyId;
		this.companyName = companyName;
		this.lastEdited = lastEdited;
		this.isValid = isValid;
	}
	
	public InspectionResultPreview() {};

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setLastEdited(LocalDate lastEdited) {
		this.lastEdited = lastEdited;
	}

	public int getCompanyId() {
		return companyId;
	}

	public String getCompanyName() {
		return companyName;
	}
	
	public String getLastEditedNice() {
		return lastEdited.format(DateTimeFormatter.ofPattern("dd.MM.YYYY"));
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public String getValidString() {
		return isValid ? "Vollständig" : "Unvollständig";
	}
}

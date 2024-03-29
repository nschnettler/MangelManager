package applicationLogic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.NotNull;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * @author Niklas Schnettler
 *
 */
public abstract class InspectionReportMinimal {

	@XStreamAlias("ESNummer")
	private int id;
	@XStreamOmitField
	@NotNull(message="Datum der Prüfung")
	private LocalDate date;
	@XStreamOmitField
	private String niceDate;
	
	
	public InspectionReportMinimal(int id, LocalDate date) {
		this.id = id;
		this.date = date;
		if (date != null) {
			this.niceDate = date.format(DateTimeFormatter.ofPattern("dd.MM.YYYY")); 
		}
	}
	
	public InspectionReportMinimal() {};
	
	public void setId(int id) {
		this.id = id;
	}

	public void setDate(LocalDate date) {
		this.date = date;
		if (date != null) {
			this.niceDate = date.format(DateTimeFormatter.ofPattern("dd.MM.YYYY")); 
		}
	}

	public void setNiceDate(String niceDate) {
		this.niceDate = niceDate;
	}

	public int getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}
	
	public String getNiceDate() {
		return niceDate;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InspectionReportMinimal other = (InspectionReportMinimal) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
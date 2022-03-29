package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * used to make handling appointments, officeHours and vacations easier and
 * comparable
 * 
 * @author
 * 
 */
public class TimeFrame implements Serializable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = -4695970817518446192L;

	/**
	 * start of given timeFrame
	 */
	private LocalDateTime start;

	/**
	 * end of given timeFrame
	 */
	private LocalDateTime end;

	/**
	 * Create TimeFrameObject with given fields
	 * 
	 * @param start date/time when timeframe starts
	 * @param end   date/time when timeframe ends
	 * @throws Illegal argument exception if start is after end
	 */
	public TimeFrame(LocalDateTime start, LocalDateTime end) {
		if (start.isAfter(end)) {
			throw new IllegalArgumentException("Start needs to be before end!");
		}
		this.start = start;
		this.end = end;
	}

	/**
	 * checks whether given {@link TimeFrame} intersects with provided other
	 * timeFrame to check for collisions
	 * 
	 * @param other other {@link TimeFrame}
	 * @return true if timeFrames intersect, false if not
	 */
	public boolean collidesWith(TimeFrame other) {
		if(other == null) {
			return false;
		}
		if ((!this.start.isBefore(other.getStart()) && this.start.isBefore(other.getEnd()))
				|| (!this.end.isAfter(other.getEnd()) && this.end.isAfter(other.getStart()))) {
			return true;
		}
		if ((!other.start.isBefore(this.getStart()) && other.start.isBefore(this.getEnd()))
				|| (!other.end.isAfter(this.getEnd()) && other.end.isAfter(this.getStart()))) {
			return true;
		}
		return false;
	}

	/**
	 * checks whether given {@link TimeFrame} is within provided other timeFrame
	 * 
	 * @param other other {@link TimeFrame}
	 * @return true if start given <= start other <= end other <= end given, false
	 *         if not
	 */
	public boolean isWithin(TimeFrame other) {
		if(other == null) {
			return false;		}
		if (!this.getStart().isBefore(other.getStart()) && !this.getEnd().isAfter(other.getEnd())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * checks whether the time part of given {@link TimeFrame} is within the time
	 * part of other
	 * 
	 * @param other other {@link TimeFrame}
	 * @return true if start given <= start other <= end other <= end given, false
	 *         if not
	 */
	public boolean isWithinIgnoringDate(TimeFrame other) {
		if(other == null) {
			return false;
		}
		LocalTime otherStart = LocalTime.from(other.getStart());
		LocalTime otherEnd = LocalTime.from(other.getEnd());

		LocalTime thisStart = LocalTime.from(this.start);
		LocalTime thisEnd = LocalTime.from(this.end);

		return !thisStart.isBefore(otherStart) && !thisEnd.isAfter(otherEnd);
	}

	/**
	 * Checks whether the appointments start and end are in the same day.
	 * 
	 * @return true iff start and end are in the same day.
	 */
	public boolean isInOneDay() {
		if ((this.start.getDayOfYear() != this.end.getDayOfYear()) || (this.start.getYear() != this.end.getYear())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return the start
	 */
	public LocalDateTime getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public LocalDateTime getEnd() {
		return end;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeFrame other = (TimeFrame) obj;
		if (!end.equals(other.end))
			return false;
		if (!start.equals(other.start))
			return false;
		return true;
	}

}

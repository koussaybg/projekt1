package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import org.junit.Test;

public class TimeFrameTests {

	final LocalDateTime localDateTime1 = LocalDateTime.now();
	final LocalDateTime localDateTime2 = LocalDateTime.now().plusMinutes(10);
	final LocalDateTime localDateTime3 = LocalDateTime.now().plusMinutes(20);
	final LocalDateTime localDateTime4 = LocalDateTime.now().plusMinutes(30);

	@Test
	public void testConstructorWithStartBeforeEnd() {
		new TimeFrame(localDateTime1, localDateTime2);
		// tests goes wrong cause of exception
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithEndBeforeStart() {
		new TimeFrame(localDateTime2, localDateTime1);

		// tests goes wrong cause of exception
	}

	/**
	 * Should collide because of overlap on time, not days..
	 */
	@Test
	public void testCollidesWithTrueOverlap() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime3);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime2, localDateTime4);

		boolean collides = timeFrame1.collidesWith(timeFrame2);

		assertTrue(collides);
	}

	/**
	 * Should not collide because 10 minutes apart.
	 */
	@Test
	public void testCollidesWithFalse() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime2);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime3, localDateTime4);

		boolean collides = timeFrame1.collidesWith(timeFrame2);

		assertFalse(collides);
	}

	/**
	 * Should collide because second time frame is within first.
	 */
	@Test
	public void testCollidesWithTrueIsWithin() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime4);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime2, localDateTime3);

		boolean collides = timeFrame1.collidesWith(timeFrame2);

		assertTrue(collides);
	}

	/**
	 * Should not collide because second timeFrame is after first.
	 */
	@Test
	public void testCollidesWithFalseBefore() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime2);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime3, localDateTime4);

		boolean collides = timeFrame1.collidesWith(timeFrame2);

		assertFalse(collides);
	}
	
	/**
	 * Should collide because same start.
	 */
	@Test
	public void testCollidesWithTrueSameStart() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime2);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime1, localDateTime3);

		assertTrue(timeFrame1.collidesWith(timeFrame2));
	}
	
	/**
	 * should collide because second begins after first.
	 */
	@Test
	public void testCollidesWithFalseStartAfter() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime3, localDateTime4);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime1, localDateTime2);
		
		assertFalse(timeFrame1.collidesWith(timeFrame2));
	}
	
	@Test
	public void testCollidesWithTrueOtherEndsBetween() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime4);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime1, localDateTime3);
		
		assertTrue(timeFrame1.collidesWith(timeFrame2));
	}


	/**
	 * Should be within because is within.
	 */
	@Test
	public void testIsWithinSameDayTrue() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime2, localDateTime3);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime1, localDateTime4);

		boolean isWithin = timeFrame1.isWithin(timeFrame2);

		assertTrue(isWithin);
	}

	/**
	 * Should not be within because is within other.
	 */
	@Test
	public void testIsWithinSameDayFalse() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime4);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime2, localDateTime3);

		boolean isWithin = timeFrame1.isWithin(timeFrame2);

		assertFalse(isWithin);
	}

	/**
	 * Should be within because is within on date part.
	 */
	@Test
	public void testIsWithinDifferentDaysTrue() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime4);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime2.minusDays(1), localDateTime3.plusDays(1));

		boolean isWithin = timeFrame1.isWithin(timeFrame2);

		assertTrue(isWithin);
	}

	/**
	 * Should not be within timeFrame1 is the day before timeFrame2.
	 */
	@Test
	public void testIsWithinDifferentDaysFalse() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1.minusDays(1), localDateTime4.minusDays(1));
		TimeFrame timeFrame2 = new TimeFrame(localDateTime2, localDateTime3);

		boolean isWithin = timeFrame1.isWithin(timeFrame2);

		assertFalse(isWithin);
	}

	/**
	 * Should not be within timeFrame1 is the day before timeFrame2.
	 */
	@Test
	public void testIsWithinOverlapsFalse() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime2, localDateTime4);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime1, localDateTime3);

		boolean isWithin = timeFrame1.isWithin(timeFrame2);

		assertFalse(isWithin);
	}

	@Test
	public void testIsWithinSameTimeFrameTrue() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime2);

		boolean isWithin = timeFrame1.isWithin(timeFrame1);

		assertTrue(isWithin);
	}

	@Test
	public void testIsWithinSameStartTrue() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime2);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime1, localDateTime3);

		boolean isWithin = timeFrame1.isWithin(timeFrame2);

		assertTrue(isWithin);
	}

	@Test
	public void testIsWithinSameEndTrue() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime2, localDateTime3);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime1, localDateTime3);

		boolean isWithin = timeFrame1.isWithin(timeFrame2);

		assertTrue(isWithin);
	}

	@Test
	public void testIsInOneDayTrue() {
		LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
		LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		TimeFrame timeFrame = new TimeFrame(start, end);

		boolean isInOneDay = timeFrame.isInOneDay();
		assertTrue(isInOneDay);
	}

	@Test
	public void testIsInOneDayDifferentDayFalse() {
		LocalDateTime start = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
		LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		TimeFrame timeFrame = new TimeFrame(start, end);

		boolean isInOneDay = timeFrame.isInOneDay();
		assertFalse(isInOneDay);
	}

	@Test
	public void testIsInOneDayDifferentYearFalse() {
		LocalDateTime start = LocalDateTime.of(LocalDate.now().minusYears(1), LocalTime.MIN);
		LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		TimeFrame timeFrame = new TimeFrame(start, end);

		boolean isInOneDay = timeFrame.isInOneDay();
		assertFalse(isInOneDay);
	}

	@Test
	public void testIsWithinIgnoringDateTrue() {
		TimeFrame timeFrame = new TimeFrame(localDateTime2.plusYears(1), localDateTime3.plusYears(1));
		TimeFrame other = new TimeFrame(localDateTime1, localDateTime4);

		assertTrue(timeFrame.isWithinIgnoringDate(other));
	}

	@Test
	public void testIsWithinIgnoringDateFalse() {
		TimeFrame timeFrame = new TimeFrame(localDateTime2.plusYears(1), localDateTime3.plusYears(1));
		TimeFrame other = new TimeFrame(localDateTime1, localDateTime4);

		assertFalse(other.isWithinIgnoringDate(timeFrame));
	}

	@Test
	public void testIsWithinIgnoringDateOverlapsFalse() {
		TimeFrame timeFrame = new TimeFrame(localDateTime2.plusYears(1), localDateTime4.plusYears(1));
		TimeFrame other = new TimeFrame(localDateTime1, localDateTime3);

		assertFalse(other.isWithinIgnoringDate(timeFrame));
	}

	@Test
	public void testEqualsTrue() {
		TimeFrame timeFrame = new TimeFrame(localDateTime1, localDateTime2);
		TimeFrame other = new TimeFrame(localDateTime1, localDateTime2);
		assertTrue(timeFrame.equals(other));
	}

	@Test
	public void testEquals() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime3);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime1, localDateTime3);
		assertTrue(timeFrame1.equals(timeFrame2));

		LocalDateTime localDateTime5 = LocalDateTime.of(2019, Month.JULY, 15, 4, 20);
		LocalDateTime localDateTime6 = LocalDateTime.of(2019, Month.JULY, 15, 4, 20);

		TimeFrame timeFrame3 = new TimeFrame(localDateTime5, localDateTime3);
		TimeFrame timeFrame4 = new TimeFrame(localDateTime6, localDateTime3);
		
		assertTrue(timeFrame1.equals(timeFrame1));
		assertTrue(timeFrame3.equals(timeFrame4));
		assertFalse(timeFrame1.equals(timeFrame3));
		assertFalse(timeFrame1.equals(null));
		assertFalse(timeFrame1.equals(new Object()));
		assertFalse(timeFrame1.equals(new TimeFrame(localDateTime1, LocalDateTime.now())));
	}
}

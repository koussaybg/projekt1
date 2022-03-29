package model;

import java.io.Serializable;

/**
 * A PriceDurationTupel is tupel, which contains a price and a duration
 * 
 * @author onurba
 *
 */
public class PriceDurationTupel implements Serializable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = 3039932097578952258L;

	/**
	 * price
	 */
	private int price;

	/**
	 * duration
	 */
	private int duration;

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriceDurationTupel other = (PriceDurationTupel) obj;
		if (duration != other.duration)
			return false;
		if (price != other.price)
			return false;
		return true;
	}
}

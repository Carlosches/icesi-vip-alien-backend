package icesi.vip.alien.service.masterPlanSchedule;

import java.util.ArrayList;
import java.util.List;

public class LotSizingMethods {
	
	public static final String ANNUAL = "Annual"; 
	public static final String MONTHLY = "Monthly";
	public static final String WEEKLY = "Weekly";
	public static final String DAILY = "Daily";
	
	public static final int MONTHS_IN_YEAR = 12;
	public static final int WEEKS_IN_YEAR = 52;
	public static final int DAYS_IN_YEAR = 365;
	
	/**
	 * calculate all net requirements to ordering in all t times using Lot x Lot
	 * method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>
	 *            with the quantities of weekly articles or items.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public List<Integer> systemLotXLot(List<Integer> requiredArticles) {
		return requiredArticles;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Economic
	 * Order Quantiy method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param double:
	 *            unit cost of articles into the list.
	 * @param double:
	 *            cost to prepare or order all articles into the list.
	 * @param double:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public List<Integer> systemEconomicOrderQuantiy(String periodicty, List<Integer> requiredArticlesBrutes, List<Integer> requiredArticlesLxL, double itemCost,
			double preparationCost, double maintenanceCost) {
		double EOQDoub = calculateEconomicOrderQuantity(periodicty, requiredArticlesBrutes, itemCost, preparationCost, maintenanceCost);
		int EOQ = (int) EOQDoub;
		// Techo si EOQDoub > EOQ ya que son unidades, si s�lo se castea har� piso.
		if (EOQDoub > EOQ) {
			EOQ += 1;
		}
		List<Integer> result = new ArrayList<>();
		int quantity = 0;
		boolean primero = true;
		for (int i = 0; i < requiredArticlesLxL.size(); i++) {
			quantity += requiredArticlesLxL.get(i);
			if(quantity > 0 && primero) {
				result.add(EOQ);
				primero = false;
			}
			if (quantity > EOQ) {
				result.add(EOQ);
				quantity = quantity-EOQ;
			} else if (i != 0) {
				result.add(0);
			}
		}
		return result;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Economic
	 * Order Quantity method. Default: Weeks
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param double:
	 *            unit cost of articles into the list.
	 * @param double:
	 *            cost to prepare or order all articles into the list.
	 * @param double:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public double calculateEconomicOrderQuantity(String periodicity, List<Integer> requiredArticles, double itemCost,
			double preparationCost, double maintenanceCost) {
		int totalItems = 0;
		double D;
		double H;
		double EOQ;
		for (int i = 0; i < requiredArticles.size(); i++) {
			totalItems += requiredArticles.get(i);
		}
		switch (periodicity) {
		case MONTHLY:
			D = (totalItems * 1.0);
			// found the annual maintenance cost
			H = maintenanceCost * itemCost * MONTHS_IN_YEAR;
			break;
		case ANNUAL:
			D = (totalItems * 1.0) / requiredArticles.size();
			// found the annual maintenance cost
			H = maintenanceCost * itemCost;
			break;
		case DAILY:
			D = ((totalItems * 1.0) / requiredArticles.size()) * (DAYS_IN_YEAR);
			// found the annual maintenance cost
			H = maintenanceCost * itemCost * DAYS_IN_YEAR;
			break;
			//In this case, default is weekly
		default:
			// we past from week to years
			D = ((totalItems * 1.0) / requiredArticles.size()) * (WEEKS_IN_YEAR);
			// found the annual maintenance cost
			H = maintenanceCost * itemCost * WEEKS_IN_YEAR;
			break;
		}
		EOQ = Math.sqrt((2 * D * preparationCost) / H);
		return EOQ;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Periods Of
	 * Supply method.
	 * <p>
	 * 
	 * @param int:
	 *            amount of times to do a order.
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public List<Integer> systemPeriodsOfSupply(int t, List<Integer> requiredArticles) {

		List<Integer> orders = new ArrayList<Integer>();
		int counter = 0;
		int sumProducts = 0;
		for (int i = 0; i < requiredArticles.size(); i++) {
			counter++;
			sumProducts = sumProducts + requiredArticles.get(i);
			if (counter == t) {
				counter = 0;
				orders.add(sumProducts);
				sumProducts = 0;
			} else if (i == requiredArticles.size() - 1) {
				orders.add(sumProducts);
			}
		}
		ArrayList<Integer> ordersWeekly = new ArrayList<Integer>();
		for (int i = 0; i < orders.size(); i++) {
			ordersWeekly.add(orders.get(i));
			for (int j = 0; j < t-1; j++) {
				ordersWeekly.add(0);
			}
		}
		return ordersWeekly;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Period Order
	 * Quantity method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param double:
	 *            unit cost of articles into the list.
	 * @param double:
	 *            cost to prepare or order all articles into the list.
	 * @param double:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public List<Integer> systemPeriodOrderQuantity(String periodicity, List<Integer> requiredArticlesBrutes, List<Integer> requiredArticlesLxL, double itemCost,
			double preparationCost, double maintenanceCost) {
		List<Integer> returnReves = new ArrayList<Integer>();
		int totalItems = 0;
		for (int i = 0; i < requiredArticlesBrutes.size(); i++) {
			totalItems += requiredArticlesBrutes.get(i);
		}
		double demand = 0.0;
		switch (periodicity) {
		case MONTHLY:
			demand = (totalItems * 1.0);
			break;
		case ANNUAL:
			demand = (totalItems * 1.0) / requiredArticlesBrutes.size();
			
			break;
		case DAILY:
			demand = ((totalItems * 1.0) / requiredArticlesBrutes.size()) * (DAYS_IN_YEAR);
			break;
			//In this case, default is weekly
		default:
			demand = ((totalItems * 1.0) / requiredArticlesBrutes.size()) * (WEEKS_IN_YEAR);
			break;
		}
		double frequencyRequested = demand
				/ calculateEconomicOrderQuantity(periodicity, requiredArticlesBrutes, itemCost, preparationCost, maintenanceCost);
		int optimalPeriodRequested = (int)Math.round((requiredArticlesLxL.size() * 1.0) / frequencyRequested);
		if(optimalPeriodRequested == 0) {
			optimalPeriodRequested = 1;
		}
		int quantity = 0;
		for (int j = requiredArticlesLxL.size() - 1; j >= 0; j--) {
			quantity += requiredArticlesLxL.get(j);
			if (j % optimalPeriodRequested == 0) {
				returnReves.add(quantity);
				quantity = 0;
			} else {
				returnReves.add(0);
			}
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = returnReves.size() - 1; i >= 0; i--) {
			result.add(returnReves.get(i));
		}
		return result;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Least Unit
	 * Cost method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param double:
	 *            cost to prepare or order all articles into the list.
	 * @param double:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public List<Integer> systemLeastUnitCost(List<Integer> requiredArticles, double preparationCost,
			double maintenanceCost, double itemCost) {
		List<Integer> result = new ArrayList<Integer>();
		int quantity = 0;
		int StoredTimes = 0;
		int storedUnits = 0;
		int lastTime = 0;
		for (int i = 0; i < requiredArticles.size(); i++) {
			quantity += requiredArticles.get(i);
			storedUnits += requiredArticles.get(i) * StoredTimes;
			double UnitCost = (preparationCost + (maintenanceCost * (itemCost * storedUnits * 1.0))) / (quantity * 1.0);
			if (i != requiredArticles.size() - 1) {
				int nextStoredUnits = storedUnits + (requiredArticles.get(i + 1) * (StoredTimes + 1));
				double nextUnitCost = (preparationCost + (maintenanceCost * (itemCost * nextStoredUnits * 1.0)))
						/ ((quantity + requiredArticles.get(i + 1)) * 1.0);
				if (nextUnitCost > UnitCost) {
					updateAuxiliaryList(result, lastTime, i, quantity);
					quantity = 0;
					storedUnits = 0;
					StoredTimes = 0;
					lastTime = i + 1;
				} else {
					StoredTimes++;
				}
			} else {
				updateAuxiliaryList(result, lastTime, i, quantity);
			}
		}
		return result;
	}

	/**
	 * calculate all net requirements to ordering in all t times using Least Total
	 * Cost method.
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param double:
	 *            cost to prepare or order all articles into the list.
	 * @param double:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public List<Integer> systemLeastTotalCost(List<Integer> requiredArticles, double preparationCost,
			double maintenanceCost, double itemCost) {
		List<Integer> result = new ArrayList<Integer>();
		int quantity = 0;
		int StoredTimes = 0;
		int storedUnits = 0;
		int lastTime = 0;
		for (int i = 0; i < requiredArticles.size(); i++) {
			quantity += requiredArticles.get(i);
			storedUnits += requiredArticles.get(i) * StoredTimes;
			double difference = Math.abs(preparationCost - (maintenanceCost * (itemCost * storedUnits * 1.0)));
			if (i != requiredArticles.size() - 1) {
				int nextStoredUnits = storedUnits + (requiredArticles.get(i + 1) * (StoredTimes + 1));
				double nextDiference = Math.abs(preparationCost - (maintenanceCost * (itemCost * nextStoredUnits * 1.0)));
				if (nextDiference > difference) {
					updateAuxiliaryList(result, lastTime, i, quantity);
					quantity = 0;
					storedUnits = 0;
					StoredTimes = 0;
					lastTime = i + 1;
				} else {
					StoredTimes++;
				}
			} else {
				updateAuxiliaryList(result, lastTime, i, quantity);
			}
		}
		return result;
	}

	// preguntarle a jeison
	// preguntarme que?
	/**
	 * Update the list with all net requeriments adding.
	 * <p>
	 * 
	 * @param ArrayList<Integer>:
	 *            list with the quantities of weekly articles or items.
	 * @param int:
	 *            cost to prepare or order all articles into the list.
	 * @param int:
	 *            cost to maintenance all articles into the list.
	 * @param int:
	 *            cost to maintenance all articles into the list.
	 * @return ArrayList<Integer> arrayList with all the net requirements to
	 *         ordering in all t times.
	 */
	public void updateAuxiliaryList(List<Integer> list, int j, int i, int quantity) {
		list.add(quantity);
		int k = j;
		while (k < i) {
			list.add(0);
			k++;
		}
	}

}
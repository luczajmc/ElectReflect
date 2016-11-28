import java.util.List;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class RegionSorter {
	static Comparator<Region> byPopulation() {
		return new Comparator<Region>() {

			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o1.getTotalVotes(), o2.getTotalVotes()); // sort largest counties first
			}
			
			@Override
			public String toString() {
				return "by population";
			}
		};
	}
	
	static Comparator<Region> byRepPercent() {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return Double.compare(o1.getRepPercent(), o2.getRepPercent());
			}

			@Override
			public String toString() {
				return "by percentage of republicans";
			}
		};
	}
	
	static Comparator<Region> byDemPercent() {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return Double.compare(o1.getDemPercent(), o2.getDemPercent());
			}

			@Override
			public String toString() {
				return "by percentage of democrats";
			}
		};
	}

	static Comparator<Region> byIndPercent() {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return Double.compare(o1.getIndPercent(), o2.getIndPercent());
			}

			@Override
			public String toString() {
				return "by percentage of independents";
			}
		};
	}
	
	static Comparator<Region> byRepVotes() {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o1.getRepVotes(), o2.getRepVotes());
			}

			@Override
			public String toString() {
				return "by number of republicans";
			}
		};
		
	}

	static Comparator<Region> byDemVotes() {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o1.getDemVotes(), o2.getDemVotes());
			}

			@Override
			public String toString() {
				return "by number of democrats";
			}
		};
		
	}

	static Comparator<Region> byIndVotes() {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o1.getIndVotes(), o2.getIndVotes());
			}

			@Override
			public String toString() {
				return "by number of independents";
			}
		};
		
	}

	static Comparator<Region> byName() {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return o1.getName().compareTo(o2.getName());
			}

			@Override
			public String toString() {
				return "by name";
			}
		};
		
	}
	
	static Comparator<Region> reverse(Comparator<Region> ordering) {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return ordering.compare(o2, o1);
			}
		
			@Override
			public String toString() {
				return ordering.toString() + " (largest first)";
			}
		};
	}
	
	static ArrayList<Comparator<Region>> getOrderings() {
		ArrayList<Comparator<Region>> orderingsList = new ArrayList<>();
		orderingsList.add(reverse(byPopulation()));
		orderingsList.add(reverse(byRepPercent()));
		orderingsList.add(reverse(byDemPercent()));
		orderingsList.add(reverse(byIndPercent()));
		orderingsList.add(reverse(byRepVotes()));
		orderingsList.add(reverse(byDemVotes()));
		orderingsList.add(reverse(byIndVotes()));
		orderingsList.add(byName());
		
		return orderingsList;
	}

}

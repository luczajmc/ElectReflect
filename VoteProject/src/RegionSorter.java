import java.util.List;
import java.util.concurrent.Callable;
import java.util.Comparator;

public class RegionSorter {
	static Comparator<Region> byPopulation() {
		return new Comparator<Region>() {

			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o1.getTotalVotes(), o2.getTotalVotes()); // sort largest counties first
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

		};
	}
	
	static Comparator<Region> byDemPercent() {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return Double.compare(o1.getDemPercent(), o2.getDemPercent());
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

		};
	}
	
	static Comparator<Region> byRepVotes() {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o1.getRepVotes(), o2.getRepVotes());
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

		};
		
	}

	static Comparator<Region> byIndVotes() {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o1.getIndVotes(), o2.getIndVotes());
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

		};
		
	}
	
	static Comparator<Region> reverse(Comparator<Region> ordering) {
		return new Comparator<Region>() {
			
			@Override
			public int compare(Region o1, Region o2) {
				// TODO Auto-generated method stub
				return ordering.compare(o2, o1);
			}
		
		};
	}

}

package no.met.wdb;

import java.util.Iterator;

/**
 * A wci.read query in a wdb database
 */
public class ReadQuery {

	private Iterable<String> dataProvider; 
	private String location;
	private String referenceTime; 
	private String validTime; 
	private Iterable<String> parameter; 
	private String level; 
	private Iterable<Integer> dataVersion;
	
	
	public ReadQuery(
			Iterable<String> dataProvider, 
			String location,
			String referenceTime, 
			String validTime, 
			Iterable<String> parameter, 
			String level, 
			Iterable<Integer> dataVersion) {
		
		this.dataProvider = dataProvider;
		this.location = location;
		this.referenceTime = referenceTime;
		this.validTime = validTime;
		this.parameter = parameter;
		this.level = level;
		this.dataVersion = dataVersion;
	}

	private String quote(String s) {

		// FIXME: Verify this!

		if ( s == null )
			return "NULL";

		String quote = "'";
		String between = "";
		while ( s.contains(quote) ) {
			quote = '$' + between + '$';
			between = between + 'n';
		}
		
		return quote + s + quote;
	}
	
	private void createStringArray(Iterable<String> stringList, StringBuilder out) {
		if ( stringList == null ) {
			out.append("NULL");
			return;
		}

		Iterator<String> it = stringList.iterator();
		
		out.append("ARRAY[");
		if ( it.hasNext() )
			out.append(quote(it.next()));
		while ( it.hasNext() ) {
			out.append(", ");
			out.append(quote(it.next()));
		}
		out.append("]");
	}

	private void createIntArray(Iterable<Integer> intList, StringBuilder out) {
		
		if ( intList == null ) {
			out.append("NULL");
			return;
		}

		Iterator<Integer> it = intList.iterator();
		
		out.append("ARRAY[");
		if ( it.hasNext() )
			out.append(it.next());
		while ( it.hasNext() ) {
			out.append(", ");
			out.append(it.next());
		}
		out.append("]");
	}

	
	@Override
	public String toString() {
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM wci.read(");
		
		createStringArray(dataProvider, query);
		query.append(", ");
		
		query.append(quote(location) + ", ");
		query.append(quote(referenceTime) + ", ");
		query.append(quote(validTime) + ", ");
		createStringArray(parameter, query);
		query.append(", ");
		query.append(quote(level) + ", ");
		createIntArray(dataVersion, query);
		query.append(", NULL::wci.returngid)");

		return query.toString();
	}
}

JSONArray values=timelineResponse.getJSONArray("days");
		
System.out.printf("Date\tMaxTemp\tMinTemp\tPrecip\tSource%n");
for (int i = 0; i < values.length(); i++) {
    JSONObject dayValue = values.getJSONObject(i);

    ZonedDateTime datetime=
        ZonedDateTime.ofInstant(Instant.ofEpochSecond(
            dayValue.getLong("datetimeEpoch")), zoneId);
            
    double maxtemp=dayValue.getDouble("tempmax");
    double mintemp=dayValue.getDouble("tempmin");
    double pop=dayValue.getDouble("precip");
    String source=dayValue.getString("source");
    System.out.printf("%s\t%.1f\t%.1f\t%.1f\t%s%n",         
        datetime.format(DateTimeFormatter.ISO_LOCAL_DATE),
            maxtemp, mintemp, pop,source );
}

#!/usr/bin/perl

$dir = "./import";

@resolutions = (); #("1", "5", "10", "15", "30", "60");

foreach $resolution (@resolutions){
	print "*** READING $resolution BARS ***\n";
	
	opendir($dh, $dir);
	@files = grep { /^*_$resolution.csv$/ } readdir($dh);
	close($dh);
	
	foreach $file (@files){
   		print "File: $file\n";
	}
}

print "*** READING DAILY BARS ***\n"; #^[^"]*$
opendir($dh, $dir);
@files = grep { /^*[(?!\_)].csv$/ } readdir($dh);
close($dh);

foreach $file (@files){
	print "Importing: $file\n";
#    system(qq~ mysql -pSSmxynk:: -u root autoStock -e "load data local infile '$dir/$file' into table stockHistoricalPrices fields terminated by ',' lines terminated by '\\n' ignore 1 lines (symbol, \@test, priceOpen, priceHigh, priceLow, priceClose, sizeVolume) set dateTime = str_to_date(\@test, '\%d-\%b-\%Y \%k:\%i');" ~);
}
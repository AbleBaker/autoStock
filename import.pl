#!/usr/bin/perl

$dir = "./import";

print "*** READING 1 MINUTE BARS ***\n";

$resolution = 1;
opendir($dh, $dir);
@files = grep { /^*_$resolution.csv$/ } readdir($dh);
close($dh);

foeach $file (@files){
   print "File: $file\n";
}

#foreach $file (@files){
#  print "Importing: $file\n";
#  system(qq~ mysql -pSSmxynk:: -u root autoStock -e "load data local infile '$dir/$file' into table stockHistoricalPrices fields terminated by ',' lines terminated by '\\n' ignore 1 lines (symbol, \@test, priceOpen, priceHigh, priceLow, priceClose, sizeVolume) set dateTime = str_to_date(\@test, '\%d-\%b-\%Y \%k:\%i');" ~);
#}
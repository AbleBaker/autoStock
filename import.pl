#!/usr/bin/perl

opendir($dh, "./import");
@files = grep { /^*_1.csv$/ } readdir($dh);
close($dh);

foreach $file (@files){
  print "Importing: $file\n";
  system(qq~ mysql -pSSmxynk -u root autoStock -e "load data local infile '$file' into table stockHistoricalPrices fields terminated by ',' lines terminated by '\\n' ignore 1 lines (symbol, \@test, priceOpen, priceHigh, priceLow, priceClose, sizeVolume) set dateTime = str_to_date(\@test, '\%d-\%b-\%Y \%k:\%i');" ~);
}
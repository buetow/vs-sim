#!/usr/bin/perl

use strict;
use warnings;

for (<DATA>) {
	chomp if /[a-z]/;
	print;
}

__DATA__


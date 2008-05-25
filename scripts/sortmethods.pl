#!/usr/bin/perl

# Automatically sorts the functions of a java class alphanumerical
# (C) 2008 by Paul C. Buetow

use strict;
use warnings;

use File::Find;
use re 'eval';

my @files;

sub usage () {
	return "Usage: perl $0 <sourcedir>\n";
}

sub process ($$) {
	my %class; 
	my $indent;

	$_[1] =~ 
		m<
			(?{ print "Start parsing $_[0]\n" })
			(package .*?;\n) (?{ 
					print "Found package: $1";
					$class{package} = $1;
				})
			(?:
			 	.*?
				(import .*?;\n) (?{ 
						print "Found import: $2";
						push @{$class{imports}}, $2;
					})
			)+
			.*?
			((?:(?:class)|(?:interface)) .*?) {\n+ (?{
					print "Sorting imports\n";
					@{$class{imports}} = sort @{$class{imports}};
					print "Found class prototype: $3"; 
					$class{prototype} = $3;
			})
			(?=
			 	(\s+) (?{ 
					$indent = length $4;
					print "Class indent is " . $indent . "\n";
				})
			)
			(?:
			\n?(??{'\s' x $indent})
			 (.+?{.*? \n(??{'\s' x $indent}) }) (?{
				 	print "Found [[$5]]";
				 })
			)*
		>xs;
	();
}

find(sub { push @files, $File::Find::name if /\.java$/ }, shift || die usage);

for (@files) {
	open my $file, $_ or die "$!: $_\n";
	print process $_, join '', <$file>;
	close $file;
}


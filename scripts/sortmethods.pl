#!/usr/bin/perl

# Automatically sorts the functions of a java class alphanumerical
# (C) 2008 by Paul C. Buetow

use strict;
use warnings;

use File::Find;

my @files;

sub usage () {
	return "Usage: perl $0 <sourcedir>\n";
}

sub process (@) {
	my ($file, @input) = @_;

	my $package;
	my @imports;
	my $classOrInterface;
	my @variables;
	my @constructors;
	my @methods;

	my $isMethod = 0;
	my $isInnerClass = 0;
	my $methodparant = 0;

	for (@input) {
		if (/^package/) {
			$package = $_;

		} elsif (/^import/) {
			push @imports, $_;

		} elsif (/^[^ ].*class/) {
			$classOrInterface = $_;

		} elsif (/^[^ ].*interface/) {
			$classOrInterface = $_;

		} elsif (!$isMethod && /;/ && !/{/) {
			push @variables, $_;

		} elsif (!$isMethod && !/;/ && /{/) {
			$methodparant = 0;
			++$methodparant while /{/g;
			--$methodparant while /}/g;
			my ($name) = /(\w*?\(.*) {/i;
			next unless defined $name;
			$isMethod = 1;
			my %method = (
					name => $name,
					prototype => $_,
					code => [],
					);
			push @methods, \%method;

		} elsif ($isMethod) {
			++$methodparant while /{/g;
			--$methodparant while /}/g;

			$isMethod = 0 if $methodparant == 0;
			push @{$methods[-1]->{code}}, $_;
		}
	}

	die "undef package in $file\n" unless defined $package;
	die "undef classOrInterface in $file\n" unless defined $classOrInterface;

	my @output = ();

	push @output, $package;
	push @output, "\n";

	if (@imports) {
		push @output, sort @imports;
		push @output, "\n";
	}

	push @output, $classOrInterface;

	if (@variables) {
		push @output, sort @variables;
		push @output, "\n";
	}

	if (@methods) {
		push @output, 
			 map { "@" .$_->{name} . "=>". $_->{prototype}, @{$_->{code}}, "\n" }
			 sort { $a->{name} cmp $b->{name} } @methods;
	}

	push @output, "}\n";

	return @output;
}

my $startDir = shift || die usage();
find(sub { push @files, $File::Find::name if /\.java$/ }, $startDir);

for (@files) {
	open my $file, $_ or die "$!: $_\n";
	my @input = <$file>;
	close $file;

	my @output = process($_, @input);
	print @output;

	print "=================== END $_\n";
}


=cut


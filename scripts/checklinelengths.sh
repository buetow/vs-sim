#!/bin/sh
# 
# Copyright (c) 2008 Paul C. Buetow, vs@dev.buetow.org
# 
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

MAXLEN=80

for i in `find ./sources -name \*.java | grep -v VSDefaultPrefs.java`
do
	awk -v MAXLEN=$MAXLEN -v file=$i '{
		if (length > MAXLEN)
			print file " line " NR " is " length " chars long "
	}' $i
done

#!/bin/bash

# Initialisation of our option variables:
nrWeeks=-1
nrDaysPerWeek=-1
nrSlotsPerDays=-1
rooms=()
teachers=()
courses=()
students=()
rules=()
groups=()
output=""
name=""

# Initialisation of our option variable:
reading_opt=""

while [[ $# -gt 0 ]]; do
	case $1 in
		-h|--help)
			echo "Usage: $0 --nrWeeks=<nrWeeks> --nrDaysPerWeek=<nrDaysPerWeek> --nrSlotsPerDay=<nrSlotsPerDay> --rooms <rooms files.xml> --teachers <teachers files.xml> --courses <courses files.xml> --students <students files.xml> --rules <rules files.xml> --groups <groups files.xml> --ouput=<outputfile.xml> [--name=<name>]"
			echo "Several XML files can be given after a '-- option', all the files will be included in the corresponding section."
			echo "A '--option=' requires only one argument, a '--option' requires at least one argument."
			echo "The '--option=' have also short versions:"
			echo "-w=<nrWeeks>"
			echo "-d=<nrDaysPerWeek>"
			echo "-s=<nrSlotsPerDay>"
			echo "-o=<outputfile.xml>"
			echo "-n=<name>"
			echo "The name is optional, by default it will be the outputfile (without .xml)"
			echo "Options can be given in any order."
			echo "WARNING: No verification of the XML files compatibility is done."
			exit 0
			;;
		-w=*|--nrWeeks=*)
			reading_opt=""
			nrWeeks=${1#*=}
			;;
		-d=*|--nrDaysPerWeek=*)
			reading_opt=""
			nrDaysPerWeek=${1#*=}
			;;
		-s=*|--nrSlotsPerDay=*)
			reading_opt=""
			nrSlotsPerDay=${1#*=}
			;;
		--rooms)
			reading_opt="rooms"
			;;
		--teachers)
			reading_opt="teachers"
			;;
		--courses)
			reading_opt="courses"
			;;
		--students)
			reading_opt="students"
			;;
		--rules)
			reading_opt="rules"
			;;
		--groups)
			reading_opt="groups"
			;;
		-o=*|--output=*)
			reading_opt=""
			output=${1#*=}
			;;
		-n=*|--name=*)
			reading_opt=""
			name=${1#*=}
			;;
		*)
			case $reading_opt in
				rooms)
					rooms+=("$1")
					;;
				teachers)
					teachers+=("$1")
					;;
				courses)
					courses+=("$1")
					;;
				students)
					students+=("$1")
					;;
				rules)
					rules+=("$1")
					;;
				groups)
					groups+=("$1")
					;;
			esac
			;;
	esac
	
	shift # shifts the arguments
done

if [ $nrWeeks -le 0 ]; then
	echo "Error: --nrWeeks negative" >&2
	exit 1
fi

if [ $nrDaysPerWeek -le 0 ]; then
	echo "Error: --nrDaysPerWeek negative" >&2
	exit 1
fi

if [ $nrSlotsPerDay -le 0 ]; then
	echo "Error: --nrSlotsPerDay negative" >&2
	exit 1
fi

if [ -z "$rooms" ]; then
	echo "Error: Missing --rooms" >&2
	exit 1
fi

if [ -z "$teachers" ]; then
	echo "Error: Missing --teachers" >&2
	exit 1
fi

if [ -z "$courses" ]; then
	echo "Error: Missing --courses" >&2
	exit 1
fi

if [ -z "$students" ]; then
	echo "Error: Missing --students" >&2
	exit 1
fi

if [ -z "$rules" ]; then
	echo "Error: Missing --rules" >&2
	exit 1
fi

if [ -z "$groups" ]; then
	echo "Error: Missing --groups" >&2
	exit 1
fi

if [ -z "$output" ]; then
	echo "Error: Missing --output" >&2
	exit 1
fi

if [ -z "$name" ]; then
	name=${output%.xml}
fi

echo '<?xml version="1.0"?>' > $output
echo '<!DOCTYPE timetabling SYSTEM "usp_validator_v1_0.dtd">' >> $output
echo "<timetabling xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"usp_timetabling_v1_2.xsd\" name=\"$name\" nrWeeks=\"$nrWeeks\" nrDaysPerWeek=\"$nrDaysPerWeek\" nrSlotsPerDay=\"$nrSlotsPerDay\">" >> $output

echo '<rooms>' >> $output
for file in ${rooms[@]}; do
	echo "<!-- file $file -->" >> $output
	cat $file >> $output
	echo "<!-- end file $file -->" >> $output
done
echo '</rooms>' >> $output

echo '<teachers>' >> $output
for file in ${teachers[@]}; do
	echo "<!-- file $file -->" >> $output
	cat $file >> $output
	echo "<!-- end file $file -->" >> $output
done
echo '</teachers>' >> $output

echo '<courses>' >> $output
for file in ${courses[@]}; do
	echo "<!-- file $file -->" >> $output
	cat $file >> $output
	echo "<!-- end file $file -->" >> $output
done
echo '</courses>' >> $output

# Filter out courses that are in the groups file but not in the courses
#COURSES_LIST=`grep "<course id=\".*\"" ${courses[@]} | cut -d\" -f2 | sort | uniq`
#STUDENT_COURSES_LIST=`grep "refId=\".*\"" ${students[@]} | cut -d\" -f2 | sort | uniq`
UNWANTED_COURSES_LIST=`comm -13 <(grep "<course id=\".*\"" ${courses[@]} | cut -d\" -f2 | sort | uniq) <(grep "refId=\".*\"" ${students[@]} | cut -d\" -f2 | sort | uniq)`

UNWANTED_COURSES_RE=""
for c in ${UNWANTED_COURSES_LIST[@]}; do
	UNWANTED_COURSES_RE="$UNWANTED_COURSES_RE|($c)"
done
# get rid of the first '|'
UNWANTED_COURSES_RE=${UNWANTED_COURSES_RE:1}

echo '<students>' >> $output
for file in ${students[@]}; do
	echo "<!-- file $file -->" >> $output
	if [ -z "$UNWANTED_COURSES_RE" ]; then
		cat $file >> $output
	else
		grep -vE "$UNWANTED_COURSES_RE" $file >> $output
	fi
	echo "<!-- end file $file -->" >> $output
done
echo '</students>' >> $output

echo '<rules>' >> $output
for file in ${rules[@]}; do
	echo "<!-- file $file -->" >> $output
	cat $file >> $output
	echo "<!-- end file $file -->" >> $output
done
echo '</rules>' >> $output

echo '<solution>' >> $output
echo '<groups>' >> $output
for file in ${groups[@]}; do
	echo "<!-- file $file -->" >> $output
	if [ -z "$UNWANTED_COURSES_RE" ]; then
		cat $file >> $output
	else
		grep -vE "$UNWANTED_COURSES_RE" $file >> $output
	fi
	echo "<!-- end file $file -->" >> $output
done
echo '</groups>' >> $output
echo '</solution>' >> $output

echo '</timetabling>' >> $output

exit 0

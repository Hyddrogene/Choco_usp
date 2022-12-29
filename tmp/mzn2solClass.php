<?php
function parseFileClass(string $filename) : string {
	// output var
	$out = "";
	
	// file format var
	$colSessionIndex=0;
	$colCourse=1;
	$colPart=2;
	$colClass=3;
	$colTeachers=4;
	$colRooms=5;
	$colGroups=6;
	$colSlot=7;
	
	// problem vars
	$slotsPerDay = 1440;
	$daysPerWeek = 5;
	$slotsPerWeek = $slotsPerDay * $daysPerWeek;
	
	$fhandler = fopen($filename, 'r');
	if($fhandler === false) {
		return $out;
	}
	
	while($line = fgets($fhandler)) {
		if($line[0] == "%" || $line[0] == '-') {
			continue;
		}
		
		$splitLine = preg_split('/;/', $line);
		//print_r($splitLine);
		$rank=0;
		$class="";
		$dailySlot=0;
		$day=0;
		$week=0;
		$rooms = array();
		$teachers = array();
		
		$rank = intval(preg_split('/:/', $splitLine[$colSessionIndex])[1]);
		
		$class = preg_split('/:/', $splitLine[$colClass])[1];
		
		$slotStr = preg_split('/:/', $splitLine[$colSlot])[1];
		//print_r($slotStr);
		eval("\$slot = $slotStr;"); // TODO should not use eval but faster to write ^^
		// slot in the file is an array of [startingSlot, Length]
		$slot = intval($slot[0]);
		
		$week = intval($slot / $slotsPerWeek);
		$slot -= $week * $slotsPerWeek;
		$day = intval($slot / $slotsPerDay);
		$dailySlot = $slot - $day * $slotsPerDay;
		
		$roomsStr = preg_split('/:/', $splitLine[$colRooms])[1];
		eval("\$rooms=$roomsStr;"); // TODO eval
		$teachersStr = preg_split('/:/', $splitLine[$colTeachers])[1];
		eval("\$teachers=$teachersStr;"); // TODO eval
		$groupsStr = preg_split('/:/', $splitLine[$colGroups])[1];
		eval("\$groups=$groupsStr;"); // TODO eval
		
		$out .= '<class refId="'.$class.'">'."\n"; // TODO $rank-1 temporary while bug with session ranks
		$out .= "\t".'<rooms>'."\n";
		foreach($rooms as $room) {
			$out .= "\t\t".'<room refId="'.$room.'" />'."\n";
		}
		$out .= "\t".'</rooms>'."\n";
		$out .= "\t".'<teachers>'."\n";
		foreach($teachers as $teacher) {
			$out .= "\t\t".'<teacher refId="'.$teacher.'" />'."\n";
		}
		$out .= "\t".'</teachers>'."\n";
		$out .= "\t".'<groups>'."\n";
		foreach($groups as $group) {
			$out .= "\t\t".'<group refId="'.$group.'" />'."\n";
		}
		$out .= "\t".'</groups>'."\n";
		$out .= '</class>'."\n";
	}
	
	fclose($fhandler);
	
	return $out;
}

if(count($argv) < 2) {
	die('Usage: php '.___FILE___.' MiniZincOutputFile');
}
echo parseFileClass($argv[1]);
?>

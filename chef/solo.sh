#!/bin/bash
## $ROLES should be defined (TODO: if not, check cmdline )

## We're going to build a simple JSON object in bash (ugh..)
## basically it should look like this:
##  { "run_list": "role[WEB]" }
## or
##  { "run_list": [ "role[WEB]", "role[APP]" ]  }
##
## depending on if we have 1 or 2+ roles
ROLES=(WEB APP DBMASTER)
ROLE_COUNT=${#ROLES[@]} #hideous syntax for getting size of a bash array
DNA_FILE=/tmp/temp.json
echo '{ "run_list": ' > $DNA_FILE
if [ ${ROLE_COUNT} -eq 1 ];
then
  ROLE=${ROLE[0]}
  echo '"role[$ROLE]"' >> $DNA_FILE
else
  echo '[ "role[' >> $DNA_FILE
  #spit out the equivalent of "ROLE[0]","ROLE[1]","ROLE[2]"
  SAVE_IFS=$IFS
  IFS=']","role['
  ROLEJOIN="${ROLES[*]}"
  IFS=$SAVE_IFS
  echo $ROLEJOIN >> $DNA_FILE
  echo '" ]' >> $DNA_FILE
fi
echo ' } ' >> $DNA_FILE



#!/bin/bash

OFFSET=1
CONT=1

while [ $CONT ]; do
    curl http://h100cal.appspot.com/api/data?offset=$OFFSET > /tmp/data.txt
    STATUS=$(cat /tmp/data.txt| wc -c)
    if [ $STATUS -lt 250 ]; then
      CONT=0
    else
      curl -X POST -d @/tmp/data.txt -H "Content-Type:application/json" http://localhost:7000/store > /tmp/data.out
      OFFSET=$((OFFSET + 1))
    fi
done

echo "END OFFSET: $OFFSET"

#!/usr/bin/awk -f
BEGIN {
    count = 0;
}
{
    if($3 == "running") {
        count += 1;
    }
}
END{
    print count;
}

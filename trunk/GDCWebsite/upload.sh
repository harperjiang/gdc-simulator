#!/bin/sh
rm gdc.tar.gz
cd WebContent
tar -czf ../gdc.tar.gz *
cd ..
scp gdc.tar.gz hajiang@polaris:/afs/ad.clarkson.edu/projects/gdc/public_html

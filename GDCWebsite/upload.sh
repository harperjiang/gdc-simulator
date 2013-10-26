#!/bin/sh
rm gdc.tar.gz
cd WebContent
tar -czf ../gdc.tar.gz *
cd ..
scp gdc.tar.gz hajiang@polaris.clarkson.edu:/afs/ad.clarkson.edu/projects/gdc/public_html
ssh hajiang@polaris.clarkson.edu "cd /afs/ad.clarkson.edu/projects/gdc/public_html;tar -xzf gdc.tar.gz"

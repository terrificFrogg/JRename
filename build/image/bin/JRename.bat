@echo off
set DIR="%~dp0"
set JAVA_EXEC="%DIR:"=%\javaw"



pushd %DIR% & start "com.sinaye.jrename" %JAVA_EXEC% %CDS_JVM_OPTS%  -p "%~dp0/../app" -m com.sinaye.jrename/com.sinaye.jrename.Start  %* & popd

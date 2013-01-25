@echo off

if [%1]==[] goto versionNotSet

set status=OK, processing
set version=%1
echo %status%;

set status=Creating dir for release %version%
echo %status%
svn mkdir https://choncms.googlecode.com/svn/p2/releases/%version% -m "Creating dir for release %version%"

set status=Checkout new created dir into target/site
echo %status%
svn checkout https://choncms.googlecode.com/svn/p2/releases/%version% target/site

set status=Adding new target site to SVN
echo %status%
svn add target/site/*

set status=Commit
echo %status%
svn commit target/site -m "Release ${version}"

set status=Done.
goto end

:versionNotSet
	echo Please enter new version
 	set status=Error executing the script
:end
	echo %status%

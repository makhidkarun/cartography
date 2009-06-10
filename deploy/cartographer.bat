
if "%OS%"=="Windows_NT" @setlocal


set _JAVACMD=%JAVACMD%
set LOCALCLASSPATH=%CLASSPATH%
for %%i in (*.jar) do call lcp.bat %%i

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=%JAVA_HOME%\bin\java.exe
if exist "%JAVA_HOME%\lib\tools.jar" set LOCALCLASSPATH=%JAVA_HOME%\lib\tools.jar;%LOCALCLASSPATH%
if exist "%JAVA_HOME%\lib\classes.zip" set LOCALCLASSPATH=%JAVA_HOME%\lib\classes.zip;%LOCALCLASSPATH%
goto checkJikes

:noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=java.exe
echo.
echo Warning: JAVA_HOME environment variable is not set.
echo   You will need to set the JAVA_HOME environment variable
echo   to the installation directory of java.
echo.

:checkJikes
"%_JAVACMD%" -classpath "%LOCALCLASSPATH%" com.softstart.stellar.Cartrographer 

set LOCALCLASSPATH=
set _JAVACMD=
set ANT_CMD_LINE_ARGS=

if "%OS%"=="Windows_NT" @endlocal
